package EmployeeSystem.service;

import EmployeeSystem.model.User;
import EmployeeSystem.model.dto.UserCreateDto;
import EmployeeSystem.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Integer id){
        return userRepository.findById(id).get();
    }

    public void addUser(UserCreateDto userCreateDto) {

        userRepository.save(getUserFromUserCreateDto(userCreateDto));
    }

    // TODO : update below with needed attr
    private User getUserFromUserCreateDto(UserCreateDto userCreateDto){

        User user = new User();
        BeanUtils.copyProperties(userCreateDto, user);
        return user;
    }

    public void updateUser(UserCreateDto userCreateDto) {

        User updatedUser = new User();
        BeanUtils.copyProperties(userCreateDto, updatedUser);
        userRepository.save(updatedUser);
    }

}
