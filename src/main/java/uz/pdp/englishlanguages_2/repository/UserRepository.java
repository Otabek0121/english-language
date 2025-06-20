package uz.pdp.englishlanguages_2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.englishlanguages_2.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByChatId(long chatId);

    Optional<User> findUserByPhoneNumber(String phoneNumber);

}
