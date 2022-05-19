package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tweetapp.dto.ForgotPasswordDTO;
import com.tweetapp.dto.RegisterUserResponseDTO;
import com.tweetapp.dto.UserLoginDTO;
import com.tweetapp.dto.UsersResponseDTO;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public ResponseEntity<RegisterUserResponseDTO> registerUser(User userDTO) {
		log.info("Inside UserServiceImpl || registerUser Method || Start");
		RegisterUserResponseDTO registerUserResponseDTO = new RegisterUserResponseDTO();
		if(!userRepository.getUserByLoginId(userDTO.getLoginId()).isPresent()) {
			if(!userRepository.save(userDTO).getFirstName().isEmpty()) {
				log.debug("Inside UserServiceImpl || registerUser Method || User Registration Suceess", userDTO.getLoginId());
				registerUserResponseDTO.setMessage("Success");
				log.info("Inside UserServiceImpl || registerUser Method || End");
				return new ResponseEntity<RegisterUserResponseDTO>(registerUserResponseDTO, HttpStatus.CREATED);
			}
			else {
				log.debug("Inside UserServiceImpl || registerUser Method || Error occur while registering the User",userDTO.getLoginId());
				registerUserResponseDTO.setMessage("Something went wrong. Please try again");
				log.info("Inside UserServiceImpl || registerUser Method || End");
				return new ResponseEntity<RegisterUserResponseDTO>(registerUserResponseDTO, HttpStatus.BAD_REQUEST);
			}
		}
		log.debug("Inside UserServiceImpl || registerUser Method || Username Already Registered", userDTO.getLoginId());
		registerUserResponseDTO.setMessage("Username already present. Please try Different Username");
		log.info("Inside UserServiceImpl || registerUser Method || End");
		return new ResponseEntity<RegisterUserResponseDTO>(registerUserResponseDTO, HttpStatus.ALREADY_REPORTED);
	}

	@Override
	public ResponseEntity<String> login(UserLoginDTO userLoginModel) {
		log.info("Inside UserServiceImpl || login Method || Start");
		if(userRepository.getUserByLoginId(userLoginModel.getUsername()).isPresent()) {
			if(userRepository.checkCredentials(userLoginModel.getUsername(), userLoginModel.getPassword()).isPresent()) {
				log.debug("Inside UserServiceImpl || login Method || Login Successful", userLoginModel.getUsername());
				log.info("Inside UserServiceImpl || login Method || End");
				return new ResponseEntity<String>("Successfully Login", HttpStatus.OK);
			}
			else {
				log.debug("Inside UserServiceImpl || login Method || Password is incorrect", userLoginModel.getUsername());
				log.info("Inside UserServiceImpl || login Method || End");
				return new ResponseEntity<String>("You have enter wrong password", HttpStatus.FORBIDDEN);
			}
		}
		log.debug("Inside UserServiceImpl || login Method || Username is incorrect", userLoginModel.getUsername());
		log.info("Inside UserServiceImpl || login Method || End");
		return new ResponseEntity<String>("Username not found. Please check username", HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<String> forgotpassword(String username, ForgotPasswordDTO forgotPasswordDTO) {
		log.info("Inside UserServiceImpl || forgotpassword Method || Start");
		if(userRepository.getUserByLoginId(username).isPresent()) {
			User user = userRepository.getUserByLoginId(username).get();
			user.setPassword(forgotPasswordDTO.getPassword());
			if(userRepository.save(user).getLoginId().equalsIgnoreCase(username)) {
				log.debug("Inside UserServiceImpl || forgotpassword Method || Password Changed", username);
				log.info("Inside UserServiceImpl || forgotpassword Method || End");
				return new ResponseEntity<String>("Password changes successfully", HttpStatus.OK);
			}
			else {
				log.debug("Inside UserServiceImpl || forgotpassword Method || Error occur while changing password", username);
				log.info("Inside UserServiceImpl || forgotpassword Method || End");
				return new ResponseEntity<String>("Something went wrong", HttpStatus.BAD_REQUEST);
			}
		}
		log.debug("Inside UserServiceImpl || forgotpassword Method || Username is incorrect", username);
		log.info("Inside UserServiceImpl || forgotpassword Method || End");
		return new ResponseEntity<String>("Username is incorrect", HttpStatus.NOT_FOUND);
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
	public ResponseEntity<?> getUserByUsername(String username) {
		log.info("Inside UserServiceImpl || getUserByUsername Method || Start");
		User user = userRepository.getUserByLoginId(username).get();
		if(!user.getFirstName().isEmpty()) {
			UsersResponseDTO usersResponseDTO = new UsersResponseDTO();
			usersResponseDTO.setUserId(user.getUserId());
			usersResponseDTO.setFirstName(user.getFirstName());
			usersResponseDTO.setLastName(user.getLastName());
			usersResponseDTO.setEmailId(user.getEmailId());
			usersResponseDTO.setMobileNumber(user.getMobileNumber());
			usersResponseDTO.setLoginId(user.getLoginId());
			log.debug("Inside UserServiceImpl || getUserByUsername Method || User Found", username);
			log.info("Inside UserServiceImpl || getUserByUsername Method || End");
			return new ResponseEntity<UsersResponseDTO>(usersResponseDTO, HttpStatus.FOUND);
		}
		log.debug("Inside UserServiceImpl || getUserByUsername Method || No User Found", username);
		log.info("Inside UserServiceImpl || getUserByUsername Method || End");
		return new ResponseEntity<String>("User not Found", HttpStatus.NOT_FOUND);
	}

}
