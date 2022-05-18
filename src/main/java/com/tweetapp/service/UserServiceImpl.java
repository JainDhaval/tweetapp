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

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public ResponseEntity<RegisterUserResponseDTO> registerUser(User userDTO) {
		RegisterUserResponseDTO registerUserResponseDTO = new RegisterUserResponseDTO();
		if(!userRepository.getUserByLoginId(userDTO.getLoginId()).isPresent()) {
			if(!userRepository.save(userDTO).getFirstName().isEmpty()) {
				registerUserResponseDTO.setMessage("Success");
				return new ResponseEntity<RegisterUserResponseDTO>(registerUserResponseDTO, HttpStatus.CREATED);
			}
			else {
				registerUserResponseDTO.setMessage("Something went wrong. Please try again");
				return new ResponseEntity<RegisterUserResponseDTO>(registerUserResponseDTO, HttpStatus.BAD_REQUEST);
			}
		}
		registerUserResponseDTO.setMessage("Username already present. Please try Different Username");
		return new ResponseEntity<RegisterUserResponseDTO>(registerUserResponseDTO, HttpStatus.ALREADY_REPORTED);
	}

	@Override
	public ResponseEntity<String> login(UserLoginDTO userLoginModel) {
		if(userRepository.getUserByLoginId(userLoginModel.getUsername()).isPresent()) {
			if(userRepository.checkCredentials(userLoginModel.getUsername(), userLoginModel.getPassword()).isPresent()) {
				return new ResponseEntity<String>("Successfully Login", HttpStatus.OK);
			}
			else {
				return new ResponseEntity<String>("You have enter wrong password", HttpStatus.FORBIDDEN);
			}
		}
		return new ResponseEntity<String>("Username not found. Please check username", HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<String> forgotpassword(String username, ForgotPasswordDTO forgotPasswordDTO) {
		if(userRepository.getUserByLoginId(username).isPresent()) {
			User user = userRepository.getUserByLoginId(username).get();
			user.setPassword(forgotPasswordDTO.getPassword());
			if(userRepository.save(user).getLoginId().equalsIgnoreCase(username)) {
				return new ResponseEntity<String>("Password changes successfully", HttpStatus.OK);
			}
			else {
				return new ResponseEntity<String>("Something went wrong", HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<String>("Username is incorrect", HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<?> getAllUser() {
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
			return new ResponseEntity<List<UsersResponseDTO>>(listUsers,HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Users List is empty", HttpStatus.NO_CONTENT);
		}
		
	}

	@Override
	public ResponseEntity<?> getUserByUsername(String username) {
		
		User user = userRepository.getUserByLoginId(username).get();
		if(!user.getFirstName().isEmpty()) {
			UsersResponseDTO usersResponseDTO = new UsersResponseDTO();
			usersResponseDTO.setUserId(user.getUserId());
			usersResponseDTO.setFirstName(user.getFirstName());
			usersResponseDTO.setLastName(user.getLastName());
			usersResponseDTO.setEmailId(user.getEmailId());
			usersResponseDTO.setMobileNumber(user.getMobileNumber());
			usersResponseDTO.setLoginId(user.getLoginId());
			
			return new ResponseEntity<UsersResponseDTO>(usersResponseDTO, HttpStatus.FOUND);
		}
		return new ResponseEntity<String>("User not Found", HttpStatus.NOT_FOUND);
	}

}
