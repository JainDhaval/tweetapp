package com.tweetapp.service;


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

@Service
public interface UserService {

	public ResponseEntity<RegisterUserResponseDTO> registerUser(RegisterUserRequestDTO userDTO);
	public ResponseEntity<UserLoginResponseDTO> login(UserLoginDTO userLoginDTO) throws Exception;
	public ResponseEntity<ForgotPasswordResponseDTO> forgotpassword(String username,ForgotPasswordDTO forgotPasswordDTO);
	public ResponseEntity<?> getAllUser();
	public ResponseEntity<UsersResponseDTO> getUserByUsername(String username) throws UserNotFoundException;
}
