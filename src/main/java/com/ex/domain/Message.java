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
	
	@Override
	public String toString() {
		return "Message [id=" + id + ", message=" + message + ", owner=" + owner + ", room=" + room + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((room == null) ? 0 : room.hashCode());
		result = prime * result + ((userMessage == null) ? 0 : userMessage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (room == null) {
			if (other.room != null)
				return false;
		} else if (!room.equals(other.room))
			return false;
		if (userMessage == null) {
			if (other.userMessage != null)
				return false;
		} else if (!userMessage.equals(other.userMessage))
			return false;
		return true;
	}
	
}
