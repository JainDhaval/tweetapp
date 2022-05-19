package com.tweetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tweetapp.dto.ForgotPasswordDTO;
import com.tweetapp.dto.RegisterUserResponseDTO;
import com.tweetapp.dto.UserLoginDTO;
import com.tweetapp.model.User;
import com.tweetapp.service.UserService;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
@RequestMapping(value = "/api/v1.0/tweets")
public class UserController {
	
	@Autowired
	UserService userService;

	@PostMapping(value = "/register")
	private ResponseEntity<RegisterUserResponseDTO> registerUser(@RequestBody User userDTO){
		log.info("Inside User Controller || registerUser Method || Start");
		log.info("Inside User Controller || registerUser Method || End");
		return userService.registerUser(userDTO);
	}
	
	@GetMapping(value = "/login")
	private ResponseEntity<String> loginUser(@RequestBody UserLoginDTO userLoginModel){
		log.info("Inside User Controller || loginUser Method || Start");
		log.info("Inside User Controller || loginUser Method || End");
		return userService.login(userLoginModel);
	}
	
	@GetMapping(value = "/{username}/forgot")
	private ResponseEntity<String> forgetPassword(@PathVariable String username, @RequestBody ForgotPasswordDTO forgotPasswordDTO){
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
	private ResponseEntity<?> searchUserByUsername(@PathVariable String username){
		log.info("Inside User Controller || searchUserByUsername Method || Start");
		log.info("Inside User Controller || searchUserByUsername Method || End");
		return userService.getUserByUsername(username);
	}
}
