package com.example.app.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RaceDto {
	private Integer raceId;
	private LocalDate raceDate;
	private String place;
	private String raceName;
	private Integer distance;
	private String surface;
	private String weather;
	private String trackCondition;
}
