package uz.pdp.englishlanguages_2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.pdp.englishlanguages_2.entity.User;
import uz.pdp.englishlanguages_2.payload.UserReqDTO;
import uz.pdp.englishlanguages_2.payload.UserResDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResDTO userToUserResDTO(User user);

    User dtoToUser(UserReqDTO userReqDTO);

    List<UserResDTO> usersToUserResDTOs(List<User> users);

    @Mapping(target = "id", ignore = true)
    void update(UserReqDTO userReqDTO, @MappingTarget User user);

}
