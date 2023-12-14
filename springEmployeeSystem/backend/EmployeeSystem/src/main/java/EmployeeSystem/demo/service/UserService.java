package EmployeeSystem.demo.service;

import EmployeeSystem.demo.model.User;
import EmployeeSystem.demo.model.dto.UserCreateDto;
import EmployeeSystem.demo.repository.UserRepository;
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

}
