package com.example.app.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.app.dto.RaceEntryDto;
import com.example.app.form.EntryForm;
import com.example.app.mapper.RaceEntryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RaceEntryService {
	private final RaceEntryMapper raceEntryMapper;

	public List<RaceEntryDto> getPredictionList(Integer raceId) {

		// 1. DBから指定したレースの出走馬一覧を取得
		List<RaceEntryDto> entries = raceEntryMapper.selectByRaceId(raceId);

		// 2. 1頭ずつ予想点数を計算
		for (RaceEntryDto entry : entries) {
			int score = calculateScore(entry);
			entry.setScore(score);
		}

		// 3. 点数が高い順に並べ替え
		entries.sort(Comparator.comparing(RaceEntryDto::getScore).reversed());

		// 4. 予想順位を付ける
		int rank = 1;
		for (RaceEntryDto entry : entries) {
			entry.setPredictionRank(rank);
			rank++;
		}

		return entries;
	}

	private int calculateScore(RaceEntryDto entry) {
		int score = 0;

		// 人気が高いほど加点
		if (entry.getPopularity() != null) {
			if (entry.getPopularity() == 1) {
				score += 30;
			} else if (entry.getPopularity() <= 3) {
				score += 20;
			} else if (entry.getPopularity() <= 5) {
				score += 10;
			} else if (entry.getPopularity() <= 8) {
				score += 5;
			}
		}

		// オッズが低いほど加点
		if (entry.getOdds() != null) {
			if (entry.getOdds() <= 3.0) {
				score += 25;
			} else if (entry.getOdds() <= 10.0) {
				score += 15;
			} else if (entry.getOdds() <= 20.0) {
				score += 8;
			} else if (entry.getOdds() <= 50.0) {
				score += 3;
			}
		}

		// 直近成績が良いほど加点
		// recentResult は「前走何着か」として扱う
		if (entry.getRecentResult() != null) {
			if (entry.getRecentResult() == 1) {
				score += 25;
			} else if (entry.getRecentResult() <= 3) {
				score += 15;
			} else if (entry.getRecentResult() <= 5) {
				score += 8;
			} else if (entry.getRecentResult() <= 8) {
				score += 3;
			}
		}

		// 騎手で加点
		if (entry.getJockey() != null) {
			if (entry.getJockey().equals("狐塚走")) {
				score += 10;
			} else if (entry.getJockey().equals("米田炊")) {
				score += 8;
			} else if (entry.getJockey().equals("直線剛")) {
				score += 6;
			} else if (entry.getJockey().equals("幸英明")) {
				score += 6;
			}
		}

		// 年齢で加点
		if (entry.getAge() != null) {
			if (entry.getAge() >= 3 && entry.getAge() <= 5) {
				score += 10;
			} else if (entry.getAge() == 6) {
				score += 5;
			}
		}

		// 枠番で加点
		if (entry.getFrameNumber() != null) {
			if (entry.getFrameNumber() <= 3) {
				score += 5;
			} else if (entry.getFrameNumber() <= 6) {
				score += 3;
			} else {
				score += 1;
			}
		}

		// 馬場状態で加点
		if (entry.getTrackCondition() != null) {
			if (entry.getTrackCondition().equals("良")) {
				score += 5;
			} else if (entry.getTrackCondition().equals("稍重")) {
				score += 3;
			} else if (entry.getTrackCondition().equals("重")) {
				score += 1;
			}
		}

		// 天気で加点
		if (entry.getWeather() != null) {
			if (entry.getWeather().equals("晴")) {
				score += 5;
			} else if (entry.getWeather().equals("曇")) {
				score += 3;
			} else if (entry.getWeather().equals("雨")) {
				score += 1;
			}
		}

		// 距離で加点
		if (entry.getDistance() != null) {
			if (entry.getDistance() >= 800 && entry.getDistance() <= 1200) {
				// 短距離
				score += 5;
			} else if (entry.getDistance() >= 1400 && entry.getDistance() <= 1600) {
				// マイル
				score += 5;
			} else if (entry.getDistance() >= 1800 && entry.getDistance() <= 2400) {
				// 中距離
				score += 3;
			} else if (entry.getDistance() >= 2500) {
				// 長距離
				score += 1;
			}
		}

		return score;
	}

	//出走馬追加用メソッドを追加
	public void registerEntry(Integer raceId, EntryForm form) {

		// 1. horsesに馬を登録
		raceEntryMapper.insertHorse(form);

		// 2. race_entriesに出走情報を登録
		raceEntryMapper.insertRaceEntry(
				raceId,
				form.getHorseId(),
				form);
	}

	//削除処理追加
	public void deleteEntry(Integer entryId) {
		raceEntryMapper.deleteRaceEntry(entryId);
	}

	//修正処理追加
	public RaceEntryDto getEntry(Integer entryId) {
		return raceEntryMapper.selectByEntryId(entryId);
	}

	public void updateEntry(EntryForm form) {
		raceEntryMapper.updateHorse(form);
		raceEntryMapper.updateRaceEntry(form);
	}
}
