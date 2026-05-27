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

			// 脚質点
			if (entry.getRunningStyle() != null && !entry.getRunningStyle().isBlank()) {
				if (entry.getRunningStyle().equals("逃げ")) {
					score += 6;
				} else if (entry.getRunningStyle().equals("先行")) {
					score += 8;
				} else if (entry.getRunningStyle().equals("差し")) {
					score += 5;
				} else if (entry.getRunningStyle().equals("追込")) {
					score += 3;
				}
			}

			// 距離適性点
			if (entry.getDistanceAptitude() != null
					&& !entry.getDistanceAptitude().isBlank()
					&& entry.getDistance() != null) {

				String raceDistanceType = "";

				if (entry.getDistance() >= 800 && entry.getDistance() <= 1200) {
					raceDistanceType = "短距離";
				} else if (entry.getDistance() >= 1400 && entry.getDistance() <= 1600) {
					raceDistanceType = "マイル";
				} else if (entry.getDistance() >= 1800 && entry.getDistance() <= 2400) {
					raceDistanceType = "中距離";
				} else if (entry.getDistance() >= 2500) {
					raceDistanceType = "長距離";
				}

				if (entry.getDistanceAptitude().equals(raceDistanceType)) {
					score += 15;
				} else if (entry.getDistanceAptitude().equals("万能")) {
					score += 8;
				} else if (!raceDistanceType.isBlank()) {
					score -= 5;
				}
			}

			// 騎手評価点
			if (entry.getJockeyScore() != null) {
				if (entry.getJockeyScore() == 5) {
					score += 15;
				} else if (entry.getJockeyScore() == 4) {
					score += 10;
				} else if (entry.getJockeyScore() == 3) {
					score += 5;
				} else if (entry.getJockeyScore() == 2) {
					score += 0;
				} else if (entry.getJockeyScore() == 1) {
					score -= 5;
				}
			}

			// 組み合わせ補正：短距離 × 逃げ・先行
			if (entry.getRunningStyle() != null && entry.getDistance() != null) {
				if (entry.getDistance() <= 1600
						&& (entry.getRunningStyle().equals("逃げ")
								|| entry.getRunningStyle().equals("先行"))) {
					score += 5;
				}

				// 長距離 × 差し・追込
				if (entry.getDistance() >= 2500
						&& (entry.getRunningStyle().equals("差し")
								|| entry.getRunningStyle().equals("追込"))) {
					score += 3;
				}
			}

			// 組み合わせ補正：重・不良馬場 × 逃げ・先行
			if (entry.getRunningStyle() != null && entry.getTrackCondition() != null) {
				if ((entry.getTrackCondition().equals("重")
						|| entry.getTrackCondition().equals("不良"))
						&& (entry.getRunningStyle().equals("逃げ")
								|| entry.getRunningStyle().equals("先行"))) {
					score += 5;
				}
			}

			// 組み合わせ補正：人気 × オッズ
			if (entry.getPopularity() != null && entry.getOdds() != null) {

				// 人気上位かつオッズも低い → 信頼度高い
				if (entry.getPopularity() <= 3 && entry.getOdds() <= 7.0) {
					score += 10;
				}

				// 人気が低く、オッズも高い → 厳しめに減点
				if (entry.getPopularity() >= 10 && entry.getOdds() >= 50.0) {
					score -= 10;
				}

				// 中穴候補
				if (entry.getPopularity() >= 4 && entry.getPopularity() <= 8
						&& entry.getOdds() >= 10.0 && entry.getOdds() <= 30.0) {
					score += 5;
				}
			}

			// 組み合わせ補正：直近成績 × 人気
			if (entry.getPopularity() != null && entry.getRecentResult() != null) {

				// 直近成績が良いのに人気が低い → 穴馬候補
				if (entry.getRecentResult() <= 3 && entry.getPopularity() >= 6) {
					score += 8;
				}

				// 人気は高いのに直近成績が悪い → 少し危険
				if (entry.getPopularity() <= 3 && entry.getRecentResult() >= 9) {
					score -= 8;
				}
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

	//結果、比較処理追加
	public void updateResultRanks(List<RaceEntryDto> entries) {
		for (RaceEntryDto entry : entries) {
			raceEntryMapper.updateResultRank(
					entry.getEntryId(),
					entry.getResultRank());
		}
	}
}
