package com.tweetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tweetapp.dto.ForgotPasswordDTO;
import com.tweetapp.dto.ForgotPasswordResponseDTO;
import com.tweetapp.dto.RegisterUserRequestDTO;
import com.tweetapp.dto.RegisterUserResponseDTO;
import com.tweetapp.dto.UserLoginDTO;
import com.tweetapp.dto.UserLoginResponseDTO;
import com.tweetapp.dto.UsersResponseDTO;
import com.tweetapp.exception.UserNotFoundException;
import com.tweetapp.service.UserService;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
@RequestMapping(value = "/api/v1.0/tweets")
@CrossOrigin(origins = "*")
public class UserController {
	
	@Autowired
	UserService userService;

	@PostMapping(value = "/register")
	private ResponseEntity<RegisterUserResponseDTO> registerUser(@RequestBody RegisterUserRequestDTO userDTO){
		log.info("Inside User Controller || registerUser Method || Start");
		log.info("Inside User Controller || registerUser Method || End");
		return userService.registerUser(userDTO);
	}
	
	@PostMapping(value = "/login")
	public ResponseEntity<UserLoginResponseDTO> loginUser(@RequestBody UserLoginDTO userLoginModel) throws Exception{
		log.info("Inside User Controller || loginUser Method || Start");
		ResponseEntity<UserLoginResponseDTO> response = userService.login(userLoginModel);
		log.info("Inside User Controller || loginUser Method || Response "+response);
		log.info("Inside User Controller || loginUser Method || End");
		return response;
	}
	
	@PostMapping(value = "/{username}/forgot")
	private ResponseEntity<ForgotPasswordResponseDTO> forgetPassword(@PathVariable String username, @RequestBody ForgotPasswordDTO forgotPasswordDTO){
		log.info("Inside User Controller || forgetPassword Method || Start");
		log.info("Inside User Controller || forgetPassword Method || End");
		return userService.forgotpassword(username, forgotPasswordDTO);
	}
	
	@GetMapping(value = "/users/all")
	private ResponseEntity<?> getAllUser(){
		log.info("Inside User Controller || getAllUser Method || Start");
		log.info("Inside User Controller || getAllUser Method || End");
		return userService.getAllUser();
	}
	
	@GetMapping(value = "user/search/{username}")
	private ResponseEntity<UsersResponseDTO> searchUserByUsername(@PathVariable String username) throws UserNotFoundException{
		log.info("Inside User Controller || searchUserByUsername Method || Start");
		log.info("Inside User Controller || searchUserByUsername Method || End");
		return userService.getUserByUsername(username);
	}
}
