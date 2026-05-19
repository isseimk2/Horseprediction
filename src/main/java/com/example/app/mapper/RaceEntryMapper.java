package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.dto.RaceEntryDto;
import com.example.app.form.EntryForm;

@Mapper
public interface RaceEntryMapper {
	List<RaceEntryDto> selectByRaceId(@Param("raceId") Integer raceId);

	//登録用メソッドを追加
	void insertHorse(EntryForm form);

	void insertRaceEntry(
			@Param("raceId") Integer raceId,
			@Param("horseId") Integer horseId,
			@Param("form") EntryForm form);

	//削除メソッドを追加
	void deleteRaceEntry(@Param("entryId") Integer entryId);

	//修正メソッド追加
	RaceEntryDto selectByEntryId(@Param("entryId") Integer entryId);

	void updateHorse(EntryForm form);

	void updateRaceEntry(EntryForm form);
}
