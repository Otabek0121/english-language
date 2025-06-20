package uz.pdp.englishlanguages_2;

import com.voicerss.tts.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.englishlanguages_2.creator.ButtonCreator;
import uz.pdp.englishlanguages_2.entity.Dictionary;
import uz.pdp.englishlanguages_2.entity.User;
import uz.pdp.englishlanguages_2.enums.UserState;
import uz.pdp.englishlanguages_2.exceptions.RestException;
import uz.pdp.englishlanguages_2.repository.DictionaryRepository;
import uz.pdp.englishlanguages_2.repository.UserRepository;
import uz.pdp.englishlanguages_2.utils.MessageConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BotSender extends TelegramLongPollingBot {

    private final ButtonCreator buttonCreator;

    private final UserRepository userRepository;
    private final DictionaryRepository dictionaryRepository;

    private Integer unitNumber;


    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    private static final String VOICERSS_API_KEY = "a3d5c68ffd0d437192a40a5578db78ee";

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            Optional<User> optionalUser = userRepository.findByChatId(chatId);
            if(optionalUser.isEmpty() && text.equals("/start")){

                    saveUserDB(update);
                    checkUserState(update);

            }
            else if(optionalUser.isPresent() && text.equals("/start")){
                updateUser(update,UserState.SEND_PHONE_NUMBER);
                checkUserState(update);

            }
            else{
                errorMessage(update);
            }
        }

        else if(update.hasMessage() && update.getMessage().hasContact()){
            Long chatId = update.getMessage().getChatId();

            saveContactUser(update); // phone number button bosilsa uni contact ini saqlaydi

            deleteContactButton(chatId);

            checkUserState(update);
        }

        else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("unit")) {
            String[] unitArray = update.getCallbackQuery().getData().split("_");
            unitNumber=Integer.valueOf(unitArray[1]);
            Long chatId = update.hasMessage() ? update.getMessage().getChatId() : update.getCallbackQuery().getFrom().getId();

            Optional<User> byChatId = userRepository.findByChatId(chatId);
            if (byChatId.isEmpty()) {
                errorMessage(update);
            }
            else{
                updateUser(update,UserState.LEARNING_LANGUAGE);
                // TODO bu yerda agar unit tanlansa shunga mos lug'atlarni chiqarib berish kerak.
                // TODO HTML farmatda chiqarib berish kerak;
                wordsLists(update);
            }
        }
        else if(update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("YES")){
            checkUserState(update);
            handleKnowButton(update);
        }
        else if(update.hasCallbackQuery()&& update.getCallbackQuery().getData().equals("KNOW")){
            handleKnowButton(update);
        }
        else if(update.hasCallbackQuery()&& update.getCallbackQuery().getData().equals("DONT_KNOW")){



            String messageText = update.getCallbackQuery().getMessage().getText();
            Long chatId = update.hasMessage() ? update.getMessage().getChatId() : update.getCallbackQuery().getFrom().getId();

            Optional<Dictionary> byUnitNumberAndValue = dictionaryRepository.findByUnitNumberAndValue(unitNumber, messageText);
            String value="";
            if(byUnitNumberAndValue.isPresent()){
                Dictionary dictionary = byUnitNumberAndValue.get();
                value ="Example ⬇️ \n\n" + dictionary.getValue()+" ➖ "+dictionary.getValueTranslation()+"\n\n"+
                dictionary.getDescription()+" \n\n";
            }



            sendMessage(chatId,value);


            sendTextToAudio(messageText,chatId);

            handleKnowButton(update);

        }
        else if(update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("HA")){
            updateUser(update,UserState.CHOOSE_UNIT);
            checkUserState(update);
        }
        else if(update.hasCallbackQuery()){

            checkUserState(update);

        }

        else {
            errorMessage(update);
        }

        //  sendTextToAudio(update);
    }

    //-----------------------------------------------------------------------------
    private List<String> words;
    private List<String> remainingWords;
    private Random random = new Random();

    public String getRandomWord() {
        if (remainingWords.isEmpty()) {

            return null;
        }

        int index = random.nextInt(remainingWords.size());
        String word = remainingWords.get(index);
        remainingWords.remove(index);
        return word;
    }

    public void handleKnowButton(Update update) {
        String word = getRandomWord();

        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(update.getCallbackQuery().getFrom().getId());
        if (word != null) {
            InlineKeyboardMarkup keyboard = buttonCreator.knowAndDontKnowButtonCreate();

            sendMessage.setText(word);

            sendMessage.setReplyMarkup(keyboard);


        } else {
            // Barcha so'zlarni ko'rib chiqqanlik haqidagi xabarni yuboring

            StringBuilder sb = new StringBuilder();
            sb.append("<b>Barcha so'zlarni ko'rib chiqdiz.</b>").append("\n");
            sb.append("<b> Boshqa unit ga o'ting!!! </b>");


            sendMessage.setText(sb.toString());
            sendMessage.setParseMode(ParseMode.HTML);
            sendMessage.setReplyMarkup(buttonCreator.anotherUnitButtonCreate());

        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

//   -------------------------------------------------------------------------------

    private void checkUserState(Update update) {

        Long chatId = update.hasMessage() ? update.getMessage().getChatId() : update.getCallbackQuery().getFrom().getId();

        Optional<User> byChatId = userRepository.findByChatId(chatId);
        if (byChatId.isEmpty()) {
            errorMessage(update);
        }

        else{

            switch (byChatId.get().getUserState()){

                case START -> System.out.println(1);

                case SEND_PHONE_NUMBER -> contactButton(chatId);

                case CHOOSE_UNIT -> unitButton(chatId);

                case LEARNING_LANGUAGE -> learningLanguages();

                case SEND_TEXT_AUDIO -> wordsLists(update);

            }
        }


    }


    private void learningLanguages() {

            // TODO endi shu unit ichidan random qilib beraverish kerak
            //  Random beraverganda bilaman desa keyingi so'zga aks holda
            //  so'zni audiosi va uning gaplar orqali qo'llangani chiqishi kerak
            //  unit tanlagandan kn uning stateni almashtirish kerak

        List<Dictionary> allByUnitNumber = dictionaryRepository.findAllByUnitNumber(unitNumber);
        remainingWords = allByUnitNumber.stream().map(Dictionary::getValue).collect(Collectors.toList());

    }


    private void wordsLists(Update update) {

            List<Dictionary> allByUnitNumber = dictionaryRepository.findAllByUnitNumber(unitNumber);

            StringBuilder str=new StringBuilder();
            str.append("<b>   Unit  "+ unitNumber +"</b> \n");
            str.append("<b> 🧾 WORDS - SO'ZLAR 🧾 </b> \n \n");


            for (int i = 0; i < allByUnitNumber.size(); i++) {
                str.append("<b>").append((i+1)+".  ").append(allByUnitNumber.get(i).getValue()).append(" ↔️ ").append(allByUnitNumber.get(i).getValueTranslation()).append("</b>").append("\n\n");
            }

            str.append("\n\n\n");
            str.append("<b> TAYYORMISIZ </b> \n");
            str.append("<b> ARE YOU READY !!! </b> \n");
            InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();


            buttons.add(Arrays.asList(
                    InlineKeyboardButton.builder().text("YES").callbackData("YES").build()
            ));

            keyboard.setKeyboard(buttons);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getCallbackQuery().getFrom().getId());
            sendMessage.setText(str.toString());
            sendMessage.setReplyMarkup(keyboard);
            sendMessage.setParseMode(ParseMode.HTML);

            try {
                execute(sendMessage);

            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }


    }



    private void unitButton(Long chatId) {

        InlineKeyboardMarkup inlineKeyboardMarkup = buttonCreator.unitButtonCreate();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Qaysi unitlardagi so'zlarni yodlamoqchisiz?");
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(sendMessage);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }




    private void updateUser(Update update,UserState userState) {
        Long chatId = update.hasMessage() ? update.getMessage().getChatId() : update.getCallbackQuery().getFrom().getId();
        Optional<User> optionalUser=userRepository.findByChatId(chatId);
        if(optionalUser.isEmpty()){
            errorMessage(update);
        }
        else{
            User user = optionalUser.get();
            user.setUserState(userState);
            userRepository.save(user);
        }
    }

    private void deleteContactButton(Long chatId) {


        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Telefon raqamingizni ulashganingiz uchun rahmat!");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void errorMessage(Update update) {

        SendMessage sendMessage = new SendMessage();
        Long chatId = update.hasMessage() ? update.getMessage().getChatId() : update.getCallbackQuery().getFrom().getId();

        sendMessage.setChatId(chatId);
        sendMessage.setText("Bunday buyruq kiritish mumkin emas.\t Botga qaytadan /start buyrug`ini yuboring!");

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void contactButton(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        ReplyKeyboardMarkup replyKeyboardMarkup = buttonCreator.sendPhoneNumberButtonCreate();

        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText("📞 Iltimos telefon nomeringizni yuboring!");

        try {
            log.info("Phone number button created");

            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }



    private void saveUserDB(Update update) {
        Long chatId = update.getMessage().getChatId();

        User user=new User();
        user.setFirstName(update.getMessage().getChat().getFirstName());
        user.setLastName(update.getMessage().getChat().getLastName());
        user.setUsername(update.getMessage().getChat().getUserName());
        user.setUserState(UserState.SEND_PHONE_NUMBER);
        user.setChatId(chatId);
        userRepository.save(user);
    }

    private void saveContactUser(Update update) {
        Chat chat = update.getMessage().getChat();

        Long chatId = update.getMessage().getChatId();
        Contact contact = update.getMessage().getContact();

        User user = userRepository.findByChatId(chatId)
                .orElseThrow(()->new RestException(MessageConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

            user.setUserState(UserState.CHOOSE_UNIT);
            user.setPhoneNumber(contact.getPhoneNumber());
            user.setFirstName(contact.getFirstName());
            user.setLastName(user.getLastName());
            user.setUsername(chat.getUserName());
            userRepository.save(user);
    }






    //--------------------------------------------------------------------------------
    private void sendTextToAudio(String messageText, long chatId) {

        String audioFilePath = convertTextToSpeech(messageText);
        if (audioFilePath != null) {

            sendAudio(chatId, audioFilePath);
        } else {
            sendMessage(chatId, "Ovoz yaratishda xatolik yuz berdi.");
        }
    }

    private String convertTextToSpeech(String text) {
        try {
            VoiceProvider tts = new VoiceProvider(VOICERSS_API_KEY);
            VoiceParameters params = new VoiceParameters(text, Languages.English_UnitedStates);
            params.setCodec(AudioCodec.WAV);
            params.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo);
            params.setBase64(false);
            params.setSSML(false);
            params.setRate(-3);

            byte[] voice = tts.speech(params);

            String filePath = "C:\\Users\\User\\Desktop\\bot_audio\\"+text+"-"+ UUID.randomUUID()+".mp3";
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(voice, 0, voice.length);
            fos.flush();
            fos.close();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendAudio(long chatId, String filePath) {
        try {
            InputFile audioFile = new InputFile(new File(filePath));
            SendAudio sendAudio = new SendAudio();
            sendAudio.setChatId(String.valueOf(chatId));
            sendAudio.setAudio(audioFile);

            execute(sendAudio);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();

        message.setChatId(String.valueOf(chatId));
        message.setText("<b>"+text+"</b>");
        message.setParseMode(ParseMode.HTML);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public  int extractNumbers(String str) {
        StringBuilder numberBuilder = new StringBuilder();

        for (char ch : str.toCharArray()) {
            if (Character.isDigit(ch)) {
                numberBuilder.append(ch);
            }
        }

        return Integer.valueOf(numberBuilder.toString());
    }


    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken(){
        return botToken;
    }
}
