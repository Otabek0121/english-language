package uz.pdp.englishlanguages_2.services;


import org.springframework.stereotype.Service;
import uz.pdp.englishlanguages_2.entity.User;
import uz.pdp.englishlanguages_2.payload.UserReqDTO;
import uz.pdp.englishlanguages_2.payload.UserResDTO;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {


    UserResDTO create(UserReqDTO userReqDTO);

    void delete(UUID id);

    UserResDTO update(UserReqDTO userReqDTO);

    UserResDTO findById(UUID id);

    List<UserResDTO> findAll();

}
