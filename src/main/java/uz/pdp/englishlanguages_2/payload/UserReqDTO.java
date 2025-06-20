package uz.pdp.englishlanguages_2.payload;

import lombok.*;
import uz.pdp.englishlanguages_2.enums.UserState;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReqDTO {

    private Long chatId;

    private String firstName;

    private String lastName;

    private String username;

    private UserState userState;

    private String languageCode;

    private String phoneNumber;
}
