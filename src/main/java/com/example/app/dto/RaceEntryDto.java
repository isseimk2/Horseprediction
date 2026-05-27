package com.example.app.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RaceEntryDto {
	//出馬情報
	private Integer entryId;
	// レース情報
	private Integer raceId;
	private String raceName;
	private LocalDate raceDate;
	private String place;
	private Integer distance;
	private String surface;
	private String weather;
	private String trackCondition;

	// 馬情報
	private Integer horseId;
	private String horseName;
	private Integer age;
	private String sex;

	// 出走情報
	private String jockey;
	private Integer frameNumber;
	private Integer horseNumber;
	private Double odds;
	private Integer popularity;
	private Integer recentResult;
	private Integer resultRank;

	// 予想用
	private Integer score;
	private Integer predictionRank;

	//脚質、距離適性、騎手
	private String runningStyle;
	private String distanceAptitude;
	private Integer jockeyScore;
}
