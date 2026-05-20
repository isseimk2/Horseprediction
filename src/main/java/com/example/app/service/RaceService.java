package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.app.dto.RaceDto;
import com.example.app.form.RaceForm;
import com.example.app.mapper.RaceMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RaceService {
	private final RaceMapper raceMapper;

	public Integer registerRace(RaceForm form) {
		raceMapper.insertRace(form);
		return form.getRaceId();
	}

	public List<RaceDto> getRaceList() {
		return raceMapper.selectAll();
	}

	public void deleteRace(Integer raceId) {
		raceMapper.deleteRaceEntriesByRaceId(raceId);
		raceMapper.deleteRace(raceId);
	}
}
