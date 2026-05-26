package com.example.app.form;

import java.util.List;

import com.example.app.dto.RaceEntryDto;

import lombok.Data;

@Data
public class RaceResultForm {
	private List<RaceEntryDto> entries;
}
