package com.ex.domain;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ex.domain.m2m.UserMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="FORUM_MESSAGE")
@JsonIgnoreProperties(value={"room"})
public class Message {
	
	@Id
	@Column(name="MESSAGE_ID")
	@SequenceGenerator(allocationSize=1,name="messageSeq",sequenceName="MESSAGE_SEQ")
	@GeneratedValue(generator="messageSeq",strategy=GenerationType.SEQUENCE)
	private Integer id;
	
	@Column(name="MESSAGE_MESSAGE", length=4000)
	private String message;
	
	@ManyToOne
	@JoinColumn(name="MESSAGE_OWNER")
	private User owner;
	
	@Column(name="MESSAGE_CREATED")
	private Timestamp created;
	
	@ManyToOne
	@JoinColumn(name="MESSAGE_ROOM")
	private Room room;
	
	@OneToMany(mappedBy="message", fetch=FetchType.EAGER)
	private Set<UserMessage> userMessage;
	
	@Transient
	private UserMessage authUserMessage;

	
	public Message(){}

	public Message(Integer id, String message, User owner, Room room) {
		super();
		this.id = id;
		this.message = message;
		this.owner = owner;
		this.room = room;
	}

	public Message(String message, User owner, Room room) {
		super();
		this.message = message;
		this.owner = owner;
		this.room = room;
	}	
	
	public Message(String message, User owner, Timestamp created, Room room) {
		super();
		this.message = message;
		this.owner = owner;
		this.created = created;
		this.room = room;
	}	

	public Message(Integer id, String message, User owner, Timestamp created, Room room) {
		super();
		this.id = id;
		this.message = message;
		this.owner = owner;
		this.created = created;
		this.room = room;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}
	
	public Set<UserMessage> getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(Set<UserMessage> userMessage) {
		this.userMessage = userMessage;
	}
	
	public UserMessage getAuthUserMessage() {
		return authUserMessage;
	}

	public void setAuthUserMessage(UserMessage authUserMessage) {
		this.authUserMessage = authUserMessage;
	}

	
	@Override
	public String toString() {
		return "Message [id=" + id + ", message=" + message + ", owner=" + owner + ", room=" + room + "]";
	}	
}
