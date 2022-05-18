package com.tweetapp.model;

import java.util.List;

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
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Tweet {

	@Id
	private String tweetId;
	@Field
	private String loginId;
	@Field
	private String tweetDescription;
	@Field
	private String postDate;
	@Field
	private String tweetTag;
	@Field
	private int tweetLikes;
	@Field
	private List<String> replyId;
	@Field
	private List<String> likeId;
}
