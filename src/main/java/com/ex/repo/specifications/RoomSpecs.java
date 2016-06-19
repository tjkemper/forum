package com.ex.repo.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.util.StringUtils;

import com.ex.domain.Category;
import com.ex.domain.Room;
import com.ex.domain.m2m.RoomCategory;
import com.ex.model.RoomFilter;

public class RoomSpecs {
	
	private RoomSpecs(){}
	
	public static Specifications<Room> createRoomSpecByRoomFilter(RoomFilter roomFilter){
		
		Specification<Room> roomNameSpec = RoomSpecs.hasRoomNameLike(roomFilter.getRoomName());
		
		Specifications<Room> allSpecs = Specifications.where(roomNameSpec);
		
		for(String cat : roomFilter.getCategories()){
			allSpecs = allSpecs.and(RoomSpecs.hasCategory(cat));
		}
		
		return allSpecs;
	}
	
	public static Specification<Room> hasRoomNameLike(final String roomName){
		return new Specification<Room>() {

			@Override
			public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		        Predicate p = cb.conjunction();

		        if (StringUtils.hasText(roomName)) {
		            p.getExpressions()
		                    .add(cb.like(cb.lower(root.<String>get("name")), "%"+roomName.toLowerCase()+"%" ));
		        }
		        return p;
			}
		};
	}

	public static Specification<Room> hasCategory(final String categoryName){
		return new Specification<Room>() {

			@Override
			public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		        Predicate p = cb.conjunction();

		        if(StringUtils.hasText(categoryName)){
		        	Join<Room, RoomCategory> roomCategoryTable = root.join("roomCategory");
	        		Join<RoomCategory, Category> categoryTable = roomCategoryTable.join("category");
	        	
	        		p.getExpressions()
	        			.add(cb.equal(categoryTable.get("categoryName"), categoryName));
		        }
		        return p;
			}
		};
	}

	
}
