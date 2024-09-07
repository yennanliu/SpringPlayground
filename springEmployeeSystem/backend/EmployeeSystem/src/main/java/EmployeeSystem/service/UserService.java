package EmployeeSystem.service;

import static EmployeeSystem.config.MessageStrings.USER_CREATED;

import EmployeeSystem.config.MessageStrings;
import EmployeeSystem.enums.ResponseStatus;
import EmployeeSystem.enums.Role;
import EmployeeSystem.exception.AuthenticationFailException;
import EmployeeSystem.exception.CustomException;
import EmployeeSystem.model.AuthenticationToken;
import EmployeeSystem.model.User;
import EmployeeSystem.model.dto.*;
import EmployeeSystem.repository.UserRepository;
import EmployeeSystem.util.Helper;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
//import javax.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserService {

  @Autowired UserRepository userRepository;

  @Autowired AuthenticationService authenticationService;

  public ResponseDto signUp(SignupDto signupDto) throws CustomException {

    // Check to see if the current email address has already been registered
    if (Helper.notNull(userRepository.findByEmail(signupDto.getEmail()))) {
      // If the email address has been registered then throw an exception.
      throw new CustomException("User already exists");
    }

    String encryptedPassword = signupDto.getPassword();
    try {
      // Step 1) encrypt password
      encryptedPassword = hashPassword(signupDto.getPassword());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      log.error("hashing password failed {}", e.getMessage());
    }

    // Step 2) save to DB, generate token
    // prepare user instance
    User user =
        new User(
            signupDto.getFirstName(),
            signupDto.getLastName(),
            signupDto.getEmail(),
            Role.USER,
            encryptedPassword);

    Mono<User> createdUser = null;
    try {

      // save the User
      user.setManagerId(0); // set manager ID = 0 as default
      createdUser = userRepository.save(user);

      /** generate token for user */
      final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser.block());

      // save token to DB
      authenticationService.saveConfirmationToken(authenticationToken);

      // return success msg
      // success in creating
      return new ResponseDto(ResponseStatus.SUCCESS.toString(), USER_CREATED);

    } catch (Exception e) {
      // handle signup error
      throw new CustomException(e.getMessage());
    }
  }

  public SignInResponseDto signIn(SignInDto signInDto) throws CustomException {

    // first find User by email
    User user = userRepository.findByEmail(signInDto.getEmail());
    if (!Helper.notNull(user)) {
      throw new AuthenticationFailException("user NOT existed");
    }
    // check if password is correct
    if (!user.getPassword().equals(signInDto.getPassword())) {
      // password NOT match
      //                log.info("user.getPassword() = " + user.getPassword());
      //                log.info("hashPassword(signInDto.getPassword()) = " +
      // hashPassword(signInDto.getPassword()));
      log.error("password not match");
      throw new AuthenticationFailException(MessageStrings.WRONG_PASSWORD + " wrong password");
    }

    log.info(">>> (authenticationService.getToken) user = {}", user);
    AuthenticationToken token = authenticationService.getToken(user);

    if (!Helper.notNull(token)) {
      // token not present
      throw new CustomException("token NOT present");
    }

    return new SignInResponseDto("success", token.getToken());
  }

  // local method

  // TODO !!! double check below logic
  String hashPassword(String password) throws NoSuchAlgorithmException {

    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(password.getBytes());
    byte[] digest = md.digest();

    // TODO : fix below
    //String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
    String myHash = "";
    return myHash;
  }

  public Flux<User> getUsers() {

    return userRepository.findAll();
  }

  public User getUserById(Integer id) {

    User user = userRepository.findById(id).block();
    if (user != null) {
      return user;
    }
    log.warn("No user with id = " + id);
    return null;
  }

  public void addUser(UserCreateDto userCreateDto) {

    userRepository.save(getUserFromUserCreateDto(userCreateDto));
  }

  // TODO : update below with needed attr
  private User getUserFromUserCreateDto(UserCreateDto userCreateDto) {

    User user = new User();
    BeanUtils.copyProperties(userCreateDto, user);
    return user;
  }

  public void updateUser(UserCreateDto userCreateDto) {

    User updatedUser = new User();
    BeanUtils.copyProperties(userCreateDto, updatedUser);
    userRepository.save(updatedUser);
  }

  // get subordinates under manager
  public List<User> getSubordinatesById(Integer managerId) {

    // TODO : do select logic in repository
    // List<User> subordinates = userRepository.getSubordinates();

    List<User> users = userRepository.findAll().toStream().collect(Collectors.toList());
    List<User> subordinates =
        users.stream()
            .filter(
                x -> {
                  return x.getManagerId().equals(managerId);
                })
            .collect(Collectors.toList());

    if (subordinates != null && subordinates.size() > 0) {
      return subordinates;
    }

    log.warn("No subordinates with managerId = " + managerId);
    return new ArrayList<>();
  }
}
