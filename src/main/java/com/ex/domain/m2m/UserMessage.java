package com.ex.domain.m2m;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ex.domain.Message;
import com.ex.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="FORUM_USER_MESSAGE")
@JsonIgnoreProperties(value={"message"})
public class UserMessage {

	@Id
	@Column(name="UM_ID")
	@SequenceGenerator(allocationSize=1,name="userMessageSeq",sequenceName="USER_MESSAGE_SEQ")
	@GeneratedValue(generator="userMessageSeq",strategy=GenerationType.SEQUENCE)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="UM_USER")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="UM_MESSAGE")
	private Message message;
	
	@Column(name="UM_USER_LIKES_MESSAGE")
	private Boolean userLikesMessage;
	
	
	public UserMessage(){}

	public UserMessage(User user, Message message, Boolean userLikesMessage) {
		super();
		this.user = user;
		this.message = message;
		this.userLikesMessage = userLikesMessage;
	}

	public UserMessage(Integer id, User user, Message message, Boolean userLikesMessage) {
		super();
		this.id = id;
		this.user = user;
		this.message = message;
		this.userLikesMessage = userLikesMessage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Boolean getUserLikesMessage() {
		return userLikesMessage;
	}

	public void setUserLikesMessage(Boolean userLikesMessage) {
		this.userLikesMessage = userLikesMessage;
	}

	@Override
	public String toString() {
		return "UserMessage [id=" + id + ", user=" + user + ", message=" + message + ", userLikesMessage="
				+ userLikesMessage + "]";
	}

	
}
