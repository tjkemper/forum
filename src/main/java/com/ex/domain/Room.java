package com.ex.domain;

import java.sql.Timestamp;
import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="FORUM_ROOM")
@JsonIgnoreProperties(value={"messages"})
public class Room {
	
	@Id
	@Column(name="ROOM_ID")
	@SequenceGenerator(allocationSize=1,name="roomSeq",sequenceName="ROOM_SEQ")
	@GeneratedValue(generator="roomSeq",strategy=GenerationType.SEQUENCE)
	private Integer id;
	
	@Column(name="ROOM_NAME", unique=true)
	private String name;
	
	@Column(name="ROOM_DESCRIPTION", length=4000)
	private String description;
	
	@ManyToOne
	@JoinColumn(name="ROOM_OWNER")
	private User owner;
	
	@Column(name="ROOM_CREATED")
	private Timestamp created;
	
	@Column(name="ROOM_CLOSED")
	private Timestamp closed;
	
	@OneToMany(mappedBy="room", fetch=FetchType.LAZY)
	private List<Message> messages;
	
	public Room(){}

	public Room(String name, String description, User owner, Timestamp created) {
		super();
		this.name = name;
		this.description = description;
		this.owner = owner;
		this.created = created;
	}
	
	public Room(Integer id, String name, String description, User owner, Timestamp created, Timestamp closed) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.owner = owner;
		this.created = created;
		this.closed = closed;
	}

	public Room(Integer id, String name, String description, User owner, Timestamp created, Timestamp closed,
			List<Message> messages) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.owner = owner;
		this.created = created;
		this.closed = closed;
		this.messages = messages;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getClosed() {
		return closed;
	}

	public void setClosed(Timestamp closed) {
		this.closed = closed;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	@Override
	public String toString() {
		return "Room [id=" + id + ", name=" + name + ", description=" + description + ", owner=" + owner + ", created="
				+ created + ", closed=" + closed + "]";
	}
	
}
