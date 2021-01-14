package com.dd.web.rest;

import com.dd.config.UserType;
import com.dd.domain.*;
import com.dd.repository.UserRepository;
import com.dd.security.SecurityUtils;
import com.dd.service.*;
import com.dd.service.dto.PasswordChangeDTO;
import com.dd.service.dto.UserDTO;
import com.dd.service.dto.UserExtraDTO;
import com.dd.web.rest.errors.*;
import com.dd.web.rest.errors.EmailAlreadyUsedException;
import com.dd.web.rest.errors.InvalidPasswordException;
import com.dd.web.rest.vm.KeyAndPasswordVM;
import com.dd.web.rest.vm.ManagedUserVM;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.util.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final UserExtraService userExtraService;

    private final StudentService studentService;
    private final ParentService parentService;
    private final TeacherService teacherService;


    private final MailService mailService;

    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService, UserExtraService userExtraService, StudentService studentService, ParentService parentService, TeacherService teacherService) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.userExtraService=userExtraService;
        this.studentService = studentService;
        this.parentService = parentService;
        this.teacherService = teacherService;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        UserExtra userExtra = new UserExtra();

        userExtra.setUser(user);
        userExtra.setUserType(managedUserVM.getUserType().toUpperCase());
        UserExtra userExtraSaved = userExtraService.save(userExtra);
        if(managedUserVM.getUserType().equals(UserType.PARENT.name())){
            Parent parent = new Parent();
            parent.setEmail(user.getEmail());
            parent.setFirstName(user.getFirstName());
            parent.setLastName(user.getLastName());
            parent.setCreated(Instant.now());
            parent.setCreatedBy(user.getEmail());
            parent.setPhone(managedUserVM.getPhone());
            parent.setUser(user);
            parentService.save(parent);
        }
        if(managedUserVM.getUserType().equals(UserType.STUDENT.name())){
            Student student = new Student();
            student.setEmail(user.getEmail());
            student.setFirstName(user.getFirstName());
            student.setLastName(user.getLastName());
            student.setCreated(Instant.now());
            student.setCreatedBy(user.getEmail());
            student.setPhone(managedUserVM.getPhone());
            student.setUser(user);
            studentService.save(student);
        }
        if(managedUserVM.getUserType().equals(UserType.TEACHER.name())){
            Teacher teacher = new Teacher();
            teacher.setEmail(user.getEmail());
            teacher.setFirstName(user.getFirstName());
            teacher.setLastName(user.getLastName());
            teacher.setCreated(Instant.now());
            teacher.setCreatedBy(user.getEmail());
            teacher.setPhone(managedUserVM.getPhone());
            teacher.setUser(user);
            teacherService.save(teacher);
        }
        mailService.sendActivationEmail(user);
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public UserDTO getAccount() {

        UserDTO userDTO = userService.getUserWithAuthorities()
            .map(UserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
            Optional<UserExtra> userExtra=userExtraService.findOneWithUserId(userDTO.getId());

            if(userExtra.isPresent()){
                userExtra.get().getUserType();
            }
        return userDTO;
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/accountextra")
    public UserExtraDTO getAccountWithExtraInformation() {

        UserDTO userDTO = userService.getUserWithAuthorities()
            .map(UserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
        Optional<UserExtra> userExtra=userExtraService.findOneWithUserId(userDTO.getId());

        UserExtraDTO userExtraDTO = new UserExtraDTO();
        userExtraDTO.setUserDTO(userDTO);

        if(userExtra.isPresent())
        {

            userExtraDTO.setUserType(userExtra.get().getUserType());
            if(userExtra.get().getUserType().equals(UserType.STUDENT.name())){
                Optional<Student> student= studentService.findOneWithEmail(userDTO.getEmail());
                if(student.isPresent()){
                    userExtraDTO.setUserId(student.get().getId());
                }
            }
            if(userExtra.get().getUserType().equals(UserType.TEACHER.name())){
                Optional<Teacher> teacher= teacherService.findOneWithEmail(userDTO.getEmail());
                if(teacher.isPresent()){
                    userExtraDTO.setUserId(teacher.get().getId());
                }
            }


        }
        else{
            userExtraDTO.setUserType(UserType.NO_USER_TYPE.name());
        }

            return userExtraDTO;
   }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
            userDTO.getLangKey(), userDTO.getImageUrl());
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.get());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail");
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user =
            userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
