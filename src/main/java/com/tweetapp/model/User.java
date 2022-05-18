package com.tweetapp.model;

import java.math.BigInteger;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class User {

	@Id
	private String userId;
	@Field
	private String firstName;
	@Field
	private String lastName;
	@Field
	private BigInteger mobileNumber;
	@Field
	private String emailId;
	@Field
	private String loginId;
	@Field
	private String password;
}
