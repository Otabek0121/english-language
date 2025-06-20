package uz.pdp.englishlanguages_2.services;

import org.springframework.stereotype.Service;
import uz.pdp.englishlanguages_2.entity.Dictionary;
import uz.pdp.englishlanguages_2.payload.DictionaryReqDTO;
import uz.pdp.englishlanguages_2.payload.DictionaryResDTO;

import java.util.List;
import java.util.UUID;


@Service
public interface DictionaryService {

    DictionaryResDTO create(DictionaryReqDTO dictionaryReqDTO);

    void delete(UUID id);

    DictionaryResDTO update(DictionaryReqDTO dictionaryReqDTO);

    DictionaryResDTO findById(UUID id);

    List<DictionaryResDTO> findAll();

}
