package uz.pdp.englishlanguages_2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.pdp.englishlanguages_2.entity.Dictionary;
import uz.pdp.englishlanguages_2.entity.User;
import uz.pdp.englishlanguages_2.payload.DictionaryReqDTO;
import uz.pdp.englishlanguages_2.payload.DictionaryResDTO;
import uz.pdp.englishlanguages_2.payload.UserReqDTO;


import java.util.List;

@Mapper(componentModel = "spring")
public interface DictionaryMapper {

    DictionaryResDTO userToDictionaryResDTO(Dictionary dictionary);

    User dtoToUser(UserReqDTO userReqDTO);

    List<DictionaryResDTO> dictionariesToDictionaryResDTOs(List<Dictionary> dictionaries);

    @Mapping(target = "id", ignore = true)
    void update(DictionaryReqDTO dictionaryReqDTO, @MappingTarget Dictionary dictionary);
}
