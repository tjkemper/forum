package com.ex.model;

import java.util.List;

public class RoomFilter {
	
	private String roomName;
	private List<String> categories;
	
	//TODO: other filters
	private String ownerUsername;
//	private <class> after;
//	private <class> before;
//	private String status;
	
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

	@Override
	public String toString() {
		return "RoomFilter [roomName=" + roomName + ", categories=" + categories + ", ownerUsername=" + ownerUsername
				+ "]";
	}
	
}
