package uz.pdp.englishlanguages_2.entity;


import jakarta.persistence.*;
import lombok.*;
import uz.pdp.englishlanguages_2.entity.template.AbsUUIDEntity;
import uz.pdp.englishlanguages_2.enums.UserState;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbsUUIDEntity {

    @Column(nullable = false)
    private Long chatId;

    private String firstName;

    private String lastName;

    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserState userState;

    private String languageCode;

    private String phoneNumber;
}
