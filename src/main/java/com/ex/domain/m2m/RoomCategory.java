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

import com.ex.domain.Category;
import com.ex.domain.Room;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="FORUM_ROOM_CATEGORY")
@JsonIgnoreProperties(value={"room"})
public class RoomCategory {
	
	@Id
	@Column(name="RC_ID")
	@SequenceGenerator(allocationSize=1,name="roomCategorySeq",sequenceName="ROOM_CATEGORY_SEQ")
	@GeneratedValue(generator="roomCategorySeq",strategy=GenerationType.SEQUENCE)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="RC_USER")
	private Room room;
	
	@ManyToOne
	@JoinColumn(name="RC_CATEGORY")
	private Category category;
	
	public RoomCategory(){}

	public RoomCategory(Room room, Category category) {
		super();
		this.room = room;
		this.category = category;
	}
	
	public RoomCategory(Integer id, Room room, Category category) {
		super();
		this.id = id;
		this.room = room;
		this.category = category;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "RoomCategory [id=" + id + ", room=" + room + ", category=" + category + "]";
	}
	
}
