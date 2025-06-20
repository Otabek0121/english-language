package uz.pdp.englishlanguages_2.entity.template;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Localizable implements Serializable {

    @NotNull
    private String uz;

    @NotNull
    private String en;

    @NotNull
    private String ru;

    public static Localizable make(String uz, String en, String ru) {
        return new Localizable(uz, en, ru);
    }


//    public static Localizable fromJsonString(String jsonString) {
//        try {
//            return RestConstants.objectMapper.readValue(jsonString, Localizable.class);
//        } catch (Exception e) {
//            // Handle the exception based on your requirements
//            e.printStackTrace();
//            return null;
//        }
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Localizable that = (Localizable) o;
        return Objects.equals(uz, that.uz) && Objects.equals(ru, that.ru) && Objects.equals(en, that.en);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uz, ru, en);
    }
}
