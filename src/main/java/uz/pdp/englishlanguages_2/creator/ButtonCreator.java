package uz.pdp.englishlanguages_2.creator;

import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class ButtonCreator {


    public ReplyKeyboardMarkup sendPhoneNumberButtonCreate() {

        KeyboardButton phoneNumber = new KeyboardButton("☎️ Telfon nomerni jo'natish!");
        phoneNumber.setRequestContact(true);

        KeyboardRow buttons = new KeyboardRow();
        buttons.add(phoneNumber);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setOneTimeKeyboard(true);
        keyboard.setResizeKeyboard(true);
        keyboard.setKeyboard(List.of(buttons));

       return keyboard;
    }

    public InlineKeyboardMarkup unitButtonCreate() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        // 1 dan 30 gacha tugmalarni qo'shamiz
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText("Unit " + i); // Tugma yozuvi
            button.setCallbackData("unit_" + i); // Callback ma'lumot

            row.add(button); // Qatorga tugma qo'shamiz

            // Har 5 ta tugmadan so'ng yangi qator yaratamiz
            if (row.size() == 5) {
                keyboard.add(row); // Qatorni klaviaturaga qo'shamiz
                row = new ArrayList<>(); // Yangi qatorni boshlaymiz
            }
        }

        // Agar oxirgi qator 5 tadan kam tugma bilan qolsa, uni ham qo'shamiz
        if (!row.isEmpty()) {
            keyboard.add(row);
        }

        // Klaviaturani o'rnatamiz
        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup knowAndDontKnowButtonCreate(){
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();


        buttons.add(Arrays.asList(
                InlineKeyboardButton.builder().text("✅").callbackData("KNOW").build(),
                InlineKeyboardButton.builder().text("❌").callbackData("DONT_KNOW").build()
        ));

        keyboard.setKeyboard(buttons);
        return keyboard;
    }


    public InlineKeyboardMarkup anotherUnitButtonCreate(){
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();


        buttons.add(Arrays.asList(
                InlineKeyboardButton.builder().text("✅").callbackData("HA").build()
        ));

        keyboard.setKeyboard(buttons);
        return keyboard;
    }



}
