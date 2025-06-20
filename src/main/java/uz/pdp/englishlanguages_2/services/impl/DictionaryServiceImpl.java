package uz.pdp.englishlanguages_2.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.englishlanguages_2.entity.Dictionary;
import uz.pdp.englishlanguages_2.payload.DictionaryReqDTO;
import uz.pdp.englishlanguages_2.payload.DictionaryResDTO;
import uz.pdp.englishlanguages_2.repository.DictionaryRepository;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DictionaryServiceImpl {

    private final DictionaryRepository dictionaryRepository;

    DictionaryResDTO create(DictionaryReqDTO dictionaryReqDTO){
        return null;
    }

    void delete(UUID id){

    }

    DictionaryResDTO update(DictionaryReqDTO dictionaryReqDTO){
        return null;
    }

    DictionaryResDTO findById(UUID id){
        return null;
    }

    List<DictionaryResDTO> findAll(){
        return null;
    }

    public void saveDictionaries(List<Dictionary> dictionaries) {
        for (Dictionary dictionary : dictionaries) {
            dictionaryRepository.save(dictionary);
        }
    }
}
