package com.tweetapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reply {

	@Id
	private String replyId;
	@Field
	private String tweetId;
	@Field
	private String loginId;
	@Field
	private String replyDescription;
	@Field
	private String date;
}
