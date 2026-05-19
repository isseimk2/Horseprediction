package com.example.app.form;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RaceForm {
	private Integer raceId;

	private LocalDate raceDate;
	private String place;
	private String raceName;
	private Integer distance;
	private String surface;
	private String weather;
	private String trackCondition;
}
