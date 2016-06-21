package com.ex.model;

import java.sql.Timestamp;
import java.util.List;

public class RoomFilter {
	
	private String roomName;
	private List<String> categories;
	
	private String ownerUsername;
	private Timestamp after;
	private Timestamp before;
	private boolean hideClosedRooms;
	
	//TODO: sortings
	
	public RoomFilter(){}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public String getOwnerUsername() {
		return ownerUsername;
	}

	public void setOwnerUsername(String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}

	public Timestamp getAfter() {
		return after;
	}

	public void setAfter(Timestamp after) {
		this.after = after;
	}

	public Timestamp getBefore() {
		return before;
	}

	public void setBefore(Timestamp before) {
		this.before = before;
	}

	public boolean isHideClosedRooms() {
		return hideClosedRooms;
	}

	public void setHideClosedRooms(boolean hideClosedRooms) {
		this.hideClosedRooms = hideClosedRooms;
	}

	@Override
	public String toString() {
		return "RoomFilter [roomName=" + roomName + ", categories=" + categories + ", ownerUsername=" + ownerUsername
				+ ", after=" + after + ", before=" + before + ", hideClosedRooms=" + hideClosedRooms + "]";
	}
	
}
