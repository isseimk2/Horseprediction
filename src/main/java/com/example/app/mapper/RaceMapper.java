package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.dto.RaceDto;
import com.example.app.form.RaceForm;

@Mapper
public interface RaceMapper {
	void insertRace(RaceForm form);

	List<RaceDto> selectAll();
}
