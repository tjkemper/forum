package com.ex.repo.m2m;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.domain.m2m.RoomCategory;
import com.ex.domain.m2m.UserMessage;

public interface RoomCategoryRepo extends JpaRepository<RoomCategory, Integer> {

	public RoomCategory findOneByRoomNameAndCategoryCategoryName(String name, String categoryName);
	public Integer deleteOneByRoomNameAndCategoryCategoryName(String name, String categoryName);

}
