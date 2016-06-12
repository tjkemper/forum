package com.ex.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ex.domain.m2m.RoomCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="FORUM_CATEGORY")
@JsonIgnoreProperties(value={"roomCategory"})
public class Category {
	
	@Id
	@Column(name="CATEGORY_ID")
	@SequenceGenerator(allocationSize=1,name="categorySeq",sequenceName="CATEGORY_SEQ")
	@GeneratedValue(generator="categorySeq",strategy=GenerationType.SEQUENCE)
	private Integer id;
	
	@Column(name="CATEGORY_NAME", unique=true)
	private String categoryName;
	
	@OneToMany(mappedBy="category", fetch=FetchType.LAZY)
	private Set<RoomCategory> roomCategory;
	
	public Category(){}

	public Category(String categoryName) {
		super();
		this.categoryName = categoryName;
	}
	
	public Category(Integer id, String categoryName) {
		super();
		this.id = id;
		this.categoryName = categoryName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Set<RoomCategory> getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(Set<RoomCategory> roomCategory) {
		this.roomCategory = roomCategory;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", categoryName=" + categoryName + "]";
	}
	
}
