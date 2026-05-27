package com.example.app.form;

import lombok.Data;

@Data
public class EntryForm {
	private Integer entryId;
	private Integer horseId;

	private String horseName;
	private Integer age;
	private String sex;

	private String jockey;
	private Integer frameNumber;
	private Integer horseNumber;
	private Double odds;
	private Integer popularity;
	private Integer recentResult;

	private String runningStyle;
	private String distanceAptitude;
	private Integer jockeyScore;
}
