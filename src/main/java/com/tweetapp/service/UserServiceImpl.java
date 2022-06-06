package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tweetapp.dto.ForgotPasswordDTO;
import com.tweetapp.dto.ForgotPasswordResponseDTO;
import com.tweetapp.dto.RegisterUserRequestDTO;
import com.tweetapp.dto.RegisterUserResponseDTO;
import com.tweetapp.dto.UserLoginDTO;
import com.tweetapp.dto.UserLoginResponseDTO;
import com.tweetapp.dto.UsersResponseDTO;
import com.tweetapp.exception.UserNotFoundException;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public ResponseEntity<RegisterUserResponseDTO> registerUser(RegisterUserRequestDTO userDTO) {
		log.info("Inside UserServiceImpl || registerUser Method || Start");
		RegisterUserResponseDTO registerUserResponseDTO = new RegisterUserResponseDTO();
		if(!userRepository.getUserByLoginId(userDTO.getLoginId()).isPresent()) {
			if(userRepository.checkMobileNbrAndEmailId(userDTO.getMobileNumber(), userDTO.getEmailId()).get().size() > 0) {
				log.info("Inside UserServiceImpl || registerUser Method || Mobile Number or Email is already Register");
				registerUserResponseDTO.setMessage("Mobile Number or Email is already Register");
				log.info("Inside UserServiceImpl || registerUser Method || End");
				return new ResponseEntity<RegisterUserResponseDTO>(registerUserResponseDTO, HttpStatus.ALREADY_REPORTED);
			}
			User user = new User();
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());
			user.setEmailId(userDTO.getEmailId());
			user.setMobileNumber(userDTO.getMobileNumber());
			user.setLoginId(userDTO.getLoginId());
			user.setPassword(userDTO.getPassword());
			if(!userRepository.save(user).getFirstName().isEmpty()) {
				log.info("Inside UserServiceImpl || registerUser Method || User Registration Suceess", userDTO.getLoginId());
				registerUserResponseDTO.setMessage("Congratulation "+userDTO.getLoginId()+" you have been registered.");
				log.info("Inside UserServiceImpl || registerUser Method || End");
				return new ResponseEntity<RegisterUserResponseDTO>(registerUserResponseDTO, HttpStatus.CREATED);
			}
			else {
				log.info("Inside UserServiceImpl || registerUser Method || Error occur while registering the User",userDTO.getLoginId());
				registerUserResponseDTO.setMessage("Something went wrong. Please try again");
				log.info("Inside UserServiceImpl || registerUser Method || End");
				return new ResponseEntity<RegisterUserResponseDTO>(registerUserResponseDTO, HttpStatus.BAD_REQUEST);
			}
		}
		log.info("Inside UserServiceImpl || registerUser Method || Username Already Registered", userDTO.getLoginId());
		registerUserResponseDTO.setMessage("Username already present. Please try Different Username");
		log.info("Inside UserServiceImpl || registerUser Method || End");
		return new ResponseEntity<RegisterUserResponseDTO>(registerUserResponseDTO, HttpStatus.ALREADY_REPORTED);
	}

	@Override
	public ResponseEntity<UserLoginResponseDTO> login(UserLoginDTO userLoginModel) throws Exception {
		log.info("Inside UserServiceImpl || login Method || Start");
		UserLoginResponseDTO userLoginResponseDTO = new UserLoginResponseDTO();
		Optional<User> optional = userRepository.getUserByLoginId(userLoginModel.getUsername());
		if(optional.isPresent()) {
			if(userRepository.checkCredentials(userLoginModel.getUsername(), userLoginModel.getPassword()).isPresent()) {
				log.info("Inside UserServiceImpl || login Method || Login Successful", userLoginModel.getUsername());
				log.info("Inside UserServiceImpl || login Method || End");
				User user = optional.get();
				userLoginResponseDTO.setUserId(user.getUserId());
				userLoginResponseDTO.setFirstName(user.getFirstName());
				userLoginResponseDTO.setLastName(user.getLastName());
				userLoginResponseDTO.setEmailId(user.getEmailId());
				userLoginResponseDTO.setMobileNumber(user.getMobileNumber());
				userLoginResponseDTO.setLoginId(user.getLoginId());
				return new ResponseEntity<UserLoginResponseDTO>(userLoginResponseDTO, HttpStatus.OK);
			}
			else {
				log.info("Inside UserServiceImpl || login Method || Password is incorrect", userLoginModel.getUsername());
				log.info("Inside UserServiceImpl || login Method || End");
				throw new Exception("Password was incorrect");
			}
		}
		log.info("Inside UserServiceImpl || login Method || Username is incorrect", userLoginModel.getUsername());
		log.info("Inside UserServiceImpl || login Method || End");
		throw new Exception("Username not found. Please check username");
	}

	@Override
	public ResponseEntity<ForgotPasswordResponseDTO> forgotpassword(String username, ForgotPasswordDTO forgotPasswordDTO) {
		log.info("Inside UserServiceImpl || forgotpassword Method || Start");
		ForgotPasswordResponseDTO forgotPasswordResponseDTO = new ForgotPasswordResponseDTO();
		if(userRepository.getUserByLoginId(username).isPresent()) {
			User user = userRepository.getUserByLoginId(username).get();
			user.setPassword(forgotPasswordDTO.getPassword());
			if(userRepository.save(user).getLoginId().equalsIgnoreCase(username)) {
				log.info("Inside UserServiceImpl || forgotpassword Method || Password Changed", username);
				forgotPasswordResponseDTO.setMessage("Password changes successfully");
				log.info("Inside UserServiceImpl || forgotpassword Method || End");
				return new ResponseEntity<ForgotPasswordResponseDTO>(forgotPasswordResponseDTO, HttpStatus.OK);
			}
			else {
				log.info("Inside UserServiceImpl || forgotpassword Method || Error occur while changing password", username);
				forgotPasswordResponseDTO.setMessage("Something went wrong");
				log.info("Inside UserServiceImpl || forgotpassword Method || End");
				return new ResponseEntity<ForgotPasswordResponseDTO>(forgotPasswordResponseDTO, HttpStatus.BAD_REQUEST);
			}
		}
		log.info("Inside UserServiceImpl || forgotpassword Method || Username is incorrect", username);
		forgotPasswordResponseDTO.setMessage("Username is incorrect");
		log.info("Inside UserServiceImpl || forgotpassword Method || End");
		return new ResponseEntity<ForgotPasswordResponseDTO>(forgotPasswordResponseDTO, HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<?> getAllUser() {
		log.info("Inside UserServiceImpl || getAllUser Method || Start");
		List<User> users = userRepository.findAll();
		if(users.size() > 0) {
			List<UsersResponseDTO> listUsers = new ArrayList<>();
			for (User user : users) {
				UsersResponseDTO usersResponseDTO = new UsersResponseDTO();
				usersResponseDTO.setUserId(user.getUserId());
				usersResponseDTO.setFirstName(user.getFirstName());
				usersResponseDTO.setLastName(user.getLastName());
				usersResponseDTO.setEmailId(user.getEmailId());
				usersResponseDTO.setMobileNumber(user.getMobileNumber());
				usersResponseDTO.setLoginId(user.getLoginId());
				listUsers.add(usersResponseDTO);
			}
			log.debug("Inside UserServiceImpl || getAllUser Method || Users Found",listUsers);
			log.info("Inside UserServiceImpl || getAllUser Method || End");
			return new ResponseEntity<List<UsersResponseDTO>>(listUsers,HttpStatus.OK);
		}else {
			log.debug("Inside UserServiceImpl || getAllUser Method || No User Found");
			log.info("Inside UserServiceImpl || getAllUser Method || End");
			return new ResponseEntity<>("Users List is empty", HttpStatus.NO_CONTENT);
		}
		
	}

	@Override
	public ResponseEntity<UsersResponseDTO> getUserByUsername(String username) throws UserNotFoundException {
		log.info("Inside UserServiceImpl || getUserByUsername Method || Start");
		Optional<User> optional = userRepository.getUserByLoginId(username);
		if(optional.isPresent()) {
			User user = optional.get();
			UsersResponseDTO usersResponseDTO = new UsersResponseDTO();
			usersResponseDTO.setUserId(user.getUserId());
			usersResponseDTO.setFirstName(user.getFirstName());
			usersResponseDTO.setLastName(user.getLastName());
			usersResponseDTO.setEmailId(user.getEmailId());
			usersResponseDTO.setMobileNumber(user.getMobileNumber());
			usersResponseDTO.setLoginId(user.getLoginId());
			log.info("Inside UserServiceImpl || getUserByUsername Method || User Found", username);
			log.info("Inside UserServiceImpl || getUserByUsername Method || End");
			return new ResponseEntity<UsersResponseDTO>(usersResponseDTO, HttpStatus.OK);
		}
		log.info("Inside UserServiceImpl || getUserByUsername Method || No User Found", username);
		log.info("Inside UserServiceImpl || getUserByUsername Method || End");
		throw new UserNotFoundException("No User Found");
	}

}
