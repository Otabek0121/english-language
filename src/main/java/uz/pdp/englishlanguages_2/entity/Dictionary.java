package uz.pdp.englishlanguages_2.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import uz.pdp.englishlanguages_2.entity.template.AbsUUIDEntity;
import uz.pdp.englishlanguages_2.enums.DictionaryStatusEnum;

import java.util.UUID;

@Entity
@Table(name = "dictionaries")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Dictionary extends AbsUUIDEntity {

    @Column(name = "unit_number")
    private int unitNumber;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false,name = "value_translation")
    private String valueTranslation;


    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private DictionaryStatusEnum dictionaryStatus;

}
