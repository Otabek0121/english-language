package uz.pdp.englishlanguages_2.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DictionaryResDTO {

    private UUID id;

    private String name;

    private String description;

    private LocalDateTime createdAt;

}
