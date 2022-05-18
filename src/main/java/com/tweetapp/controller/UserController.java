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

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/api/v1.0/tweets")
public class UserController {
	
	@Autowired
	UserService userService;

	@PostMapping(value = "/register")
	@ApiOperation(value = "To register new User", response = RegisterUserResponseDTO.class, httpMethod = "POST")
	private ResponseEntity<RegisterUserResponseDTO> registerUser(@RequestBody User userDTO){
		return userService.registerUser(userDTO);
	}
	
	@GetMapping(value = "/login")
	private ResponseEntity<String> loginUser(@RequestBody UserLoginDTO userLoginModel){
		return userService.login(userLoginModel);
	}
	
	@GetMapping(value = "/{username}/forgot")
	private ResponseEntity<String> forgetPassword(@PathVariable String username, @RequestBody ForgotPasswordDTO forgotPasswordDTO){ 
		return userService.forgotpassword(username, forgotPasswordDTO);
	}
	
	@GetMapping(value = "/users/all")
	private ResponseEntity<?> getAllUser(){
		return userService.getAllUser();
	}
	
	@GetMapping(value = "user/search/{username}")
	private ResponseEntity<?> searchUserByUsername(@PathVariable String username){
		return userService.getUserByUsername(username);
	}
}
