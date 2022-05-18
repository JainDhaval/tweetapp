package com.tweetapp.dto;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersResponseDTO {

	private String userId;
	private String firstName;
	private String lastName;
	private BigInteger mobileNumber;
	private String emailId;
	private String loginId;
}
