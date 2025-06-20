package uz.pdp.englishlanguages_2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.pdp.englishlanguages_2.entity.Dictionary;
import uz.pdp.englishlanguages_2.services.impl.DictionaryServiceImpl;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class EnglishLanguages2Application {

    public static void main(String[] args) {
        SpringApplication.run(EnglishLanguages2Application.class, args);
    }


    @Bean
    public TelegramBotsApi telegramBotsApi(BotSender botSender) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(botSender);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return botsApi;
    }


    @Bean
    CommandLineRunner initData(DictionaryServiceImpl dictionaryService) {
        return args -> {
            List<Dictionary> dictionaries = Arrays.asList(
                     Dictionary.builder().unitNumber(1).value("Mountain").valueTranslation("Tog'").description("The mountain is high. They climbed the mountain.").build(),
                     Dictionary.builder().unitNumber(2).value("River").valueTranslation("Daryo").description("The river flows fast. They swam in the river.").build(),
                     Dictionary.builder().unitNumber(3).value("Sky").valueTranslation("Osmon").description("The sky is clear today. I love looking at the sky.").build(),
                     Dictionary.builder().unitNumber(4).value("Garden").valueTranslation("Bog'").description("The garden is beautiful. I planted flowers in the garden.").build(),
                     Dictionary.builder().unitNumber(5).value("School").valueTranslation("Maktab").description("She goes to school daily. School starts at 8 AM.").build(),
                     Dictionary.builder().unitNumber(1).value("Rain").valueTranslation("Yomg'ir").description("It is raining. I love the sound of rain.").build(),
                     Dictionary.builder().unitNumber(2).value("Sun").valueTranslation("Quyosh").description("The sun is shining brightly. The sun sets in the west.").build(),
                     Dictionary.builder().unitNumber(3).value("Moon").valueTranslation("Oy").description("The moon is bright tonight. I can see the moon clearly.").build(),
                     Dictionary.builder().unitNumber(4).value("Star").valueTranslation("Yulduz").description("The stars are twinkling. There are many stars in the sky.").build(),
                     Dictionary.builder().unitNumber(5).value("Ocean").valueTranslation("Okean").description("The ocean is vast. We sailed across the ocean.").build(),
                     Dictionary.builder().unitNumber(1).value("Desert").valueTranslation("Cho'l").description("The desert is very dry. Few plants grow in the desert.").build(),
                     Dictionary.builder().unitNumber(2).value("Forest").valueTranslation("O'rmon").description("The forest is dense. Animals live in the forest.").build(),
                     Dictionary.builder().unitNumber(3).value("Island").valueTranslation("Orol").description("They visited a small island. The island is surrounded by water.").build(),
                     Dictionary.builder().unitNumber(4).value("Lake").valueTranslation("Ko'l").description("The lake is calm and peaceful. We went fishing at the lake.").build(),
                     Dictionary.builder().unitNumber(5).value("Field").valueTranslation("Dala").description("Farmers work in the field. The field is green with crops.").build(),
                     Dictionary.builder().unitNumber(1).value("Road").valueTranslation("Yo'l").description("The road is long. I walked down the road.").build(),
                     Dictionary.builder().unitNumber(2).value("Bridge").valueTranslation("Ko'prik").description("The bridge connects two cities. We crossed the bridge.").build(),
                     Dictionary.builder().unitNumber(3).value("Valley").valueTranslation("Vodiy").description("The valley is beautiful. The river runs through the valley.").build(),
                     Dictionary.builder().unitNumber(4).value("Cave").valueTranslation("G'or").description("They explored the dark cave. The cave is deep and mysterious.").build(),
                     Dictionary.builder().unitNumber(5).value("Hill").valueTranslation("Tepalik").description("The hill is steep. They climbed to the top of the hill.").build()
            );

            dictionaryService.saveDictionaries(dictionaries);
        };
    }

}
