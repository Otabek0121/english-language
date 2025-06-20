package uz.pdp.englishlanguages_2.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.pdp.englishlanguages_2.entity.User;
import uz.pdp.englishlanguages_2.exceptions.RestException;
import uz.pdp.englishlanguages_2.mapper.UserMapper;
import uz.pdp.englishlanguages_2.payload.UserReqDTO;
import uz.pdp.englishlanguages_2.payload.UserResDTO;
import uz.pdp.englishlanguages_2.repository.UserRepository;
import uz.pdp.englishlanguages_2.services.UserService;
import uz.pdp.englishlanguages_2.utils.MessageConstants;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserResDTO create(UserReqDTO userReqDTO) {

        Optional<User> byChatId = userRepository.findByChatId(userReqDTO.getChatId());
        if(byChatId.isPresent()) {
            throw new RestException(MessageConstants.USER_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        User user = userMapper.dtoToUser(userReqDTO);
        userRepository.save(user);

        return userMapper.userToUserResDTO(user);
    }

    @Override
    public void delete(UUID id) {

        Optional<User> byId = userRepository.findById(id);
        if(byId.isPresent()) {
            User user = byId.get();
            user.setDeleted(true);
            userRepository.save(user);
        }

    }

    @Override
    public UserResDTO update(UserReqDTO userReqDTO) {

        Optional<User> byChatId = userRepository.findByChatId(userReqDTO.getChatId());
        if(byChatId.isEmpty()) {
            throw new RestException(MessageConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        User user = byChatId.get();

        user.setChatId(userReqDTO.getChatId());
        user.setUserState(userReqDTO.getUserState());
        user.setFirstName(userReqDTO.getFirstName());
        user.setLastName(userReqDTO.getLastName());
        user.setPhoneNumber(userReqDTO.getPhoneNumber());
        user.setLanguageCode(userReqDTO.getLanguageCode());
        user.setUsername(userReqDTO.getUsername());
        userRepository.save(user);

        return userMapper.userToUserResDTO(userMapper.dtoToUser(userReqDTO));
    }

    @Override
    public UserResDTO findById(UUID id) {
        Optional<User> byId = userRepository.findById(id);
        if(byId.isEmpty()) {
            throw new RestException(MessageConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return userMapper.userToUserResDTO(byId.get());
    }

    @Override
    public List<UserResDTO> findAll() {
        List<User> users = userRepository.findAll();
        return userMapper.usersToUserResDTOs(users);
    }
}
