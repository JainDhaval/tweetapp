package com.tweetapp.service;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tweetapp.dto.ForgotPasswordDTO;
import com.tweetapp.dto.RegisterUserResponseDTO;
import com.tweetapp.dto.UserLoginDTO;
import com.tweetapp.model.User;

@Service
public interface UserService {

	public ResponseEntity<RegisterUserResponseDTO> registerUser(User userDTO);
	public ResponseEntity<String> login(UserLoginDTO userLoginDTO);
	public ResponseEntity<String> forgotpassword(String username,ForgotPasswordDTO forgotPasswordDTO);
	public ResponseEntity<?> getAllUser();
	public ResponseEntity<?> getUserByUsername(String username);
}
