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
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    User createdUser = null;
    try {

      // save the User
      user.setManagerId(0); // set manager ID = 0 as default
      createdUser = userRepository.save(user);

      /** generate token for user */
      final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);

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
    
    // hash the input password for comparison
    String hashedInputPassword = "";
    try {
      hashedInputPassword = hashPassword(signInDto.getPassword());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      log.error("hashing password failed {}", e.getMessage());
      throw new CustomException("Password hashing failed");
    }
    
    // check if password is correct
    if (!user.getPassword().equals(hashedInputPassword)) {
      // password NOT match
      log.error("password not match");
      throw new AuthenticationFailException(MessageStrings.WRONG_PASSWORD + " wrong password");
    }

    log.info(">>> (authenticationService.getToken) user = {}", user);
    
    // Try to get existing token first
    AuthenticationToken token = authenticationService.getToken(user);
    
    // If no token exists, create a new one
    if (!Helper.notNull(token)) {
      log.info("No existing token found, creating new token for user: {}", user.getEmail());
      token = new AuthenticationToken(user);
      authenticationService.saveConfirmationToken(token);
    }

    return new SignInResponseDto("success", token.getToken());
  }

  // local method

  // TODO !!! double check below logic
  String hashPassword(String password) throws NoSuchAlgorithmException {

    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(password.getBytes());
    byte[] digest = md.digest();
    String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
    return myHash;
  }

  public List<User> getUsers() {

    return userRepository.findAll();
  }

  public User getUserById(Integer id) {

    if (userRepository.findById(id).isPresent()) {
      return userRepository.findById(id).get();
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

    Optional<User> optionalUser = userRepository.findById(userCreateDto.getId());
    if (optionalUser.isPresent()) {
      User existingUser = optionalUser.get();
      
      // Update fields using helper method
      updateUserFields(existingUser, userCreateDto);
      
      userRepository.save(existingUser);
      log.info("User updated successfully with ID: {}", userCreateDto.getId());
    } else {
      log.error("User with ID {} not found", userCreateDto.getId());
      throw new RuntimeException("User not found with ID: " + userCreateDto.getId());
    }
  }

  public void updateUserWithPhoto(UserCreateDto userCreateDto, MultipartFile photo) {
    
    Optional<User> optionalUser = userRepository.findById(userCreateDto.getId());
    if (optionalUser.isPresent()) {
      User existingUser = optionalUser.get();
      
      // Update basic fields using helper method
      updateUserFields(existingUser, userCreateDto);

      // Handle photo upload
      if (photo != null && !photo.isEmpty()) {
        try {
          byte[] photoBytes = photo.getBytes();
          existingUser.setPhoto(photoBytes);
          log.info("Photo updated for user ID: {}, size: {} bytes", userCreateDto.getId(), photoBytes.length);
        } catch (IOException e) {
          log.error("Error processing photo for user ID: {}", userCreateDto.getId(), e);
          throw new RuntimeException("Failed to process photo: " + e.getMessage());
        }
      }
      
      userRepository.save(existingUser);
      log.info("User updated successfully with ID: {}", userCreateDto.getId());
    } else {
      log.error("User with ID {} not found", userCreateDto.getId());
      throw new RuntimeException("User not found with ID: " + userCreateDto.getId());
    }
  }

  /**
   * Helper method to update user fields from UserCreateDto.
   * Only updates fields that are not null, preserving existing values for null fields.
   * 
   * @param existingUser The existing user entity to update
   * @param userCreateDto The DTO containing the new values
   */
  private void updateUserFields(User existingUser, UserCreateDto userCreateDto) {
    if (userCreateDto.getFirstName() != null) {
      existingUser.setFirstName(userCreateDto.getFirstName());
    }
    if (userCreateDto.getLastName() != null) {
      existingUser.setLastName(userCreateDto.getLastName());
    }
    if (userCreateDto.getEmail() != null) {
      existingUser.setEmail(userCreateDto.getEmail());
    }
    if (userCreateDto.getRole() != null) {
      existingUser.setRole(userCreateDto.getRole());
    }
    if (userCreateDto.getDepartementId() != null) {
      existingUser.setDepartementId(userCreateDto.getDepartementId());
    }
    if (userCreateDto.getManagerId() != null) {
      existingUser.setManagerId(userCreateDto.getManagerId());
    }
  }

  // get subordinates under manager
  public List<User> getSubordinatesById(Integer managerId) {

    // TODO : do select logic in repository
    // List<User> subordinates = userRepository.getSubordinates();

    List<User> users = userRepository.findAll();
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
