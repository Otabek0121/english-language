package uz.pdp.englishlanguages_2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.englishlanguages_2.entity.Dictionary;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, UUID> {

    Optional<Dictionary> findByValue(String value);

    List<Dictionary> findAllByUnitNumber(int unitNumber);

   // @Query("SELECT d.description FROM Dictionary d WHERE d.unitNumber = :unitNumber AND d.value = :value")
    Optional<Dictionary> findByUnitNumberAndValue(Integer unitNumber, String value);
}
