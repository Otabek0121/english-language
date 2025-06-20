package uz.pdp.englishlanguages_2.payload;

import lombok.*;
import uz.pdp.englishlanguages_2.enums.UserState;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResDTO {

    private UUID id;

    private Long chatId;

    private String firstName;

    private String lastName;

    private String username;

    private UserState userState;

    private String languageCode;

    private String phoneNumber;

}
