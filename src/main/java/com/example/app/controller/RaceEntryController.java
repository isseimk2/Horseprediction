package com.example.app.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.dto.RaceEntryDto;
import com.example.app.form.EntryForm;
import com.example.app.form.RaceResultForm;
import com.example.app.service.RaceEntryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RaceEntryController {
	private final RaceEntryService raceEntryService;

	@GetMapping("/races/{raceId}/entries/new")
	public String showEntryForm(
			@PathVariable Integer raceId,
			Model model) {
		List<RaceEntryDto> entries = raceEntryService.getEntryList(raceId);

		model.addAttribute("raceId", raceId);
		model.addAttribute("entryForm", new EntryForm());
		model.addAttribute("entries", entries);

		return "entry-form";
	}

	@PostMapping("/races/{raceId}/entries/new")
	public String registerEntry(
			@PathVariable Integer raceId,
			EntryForm form) {

		raceEntryService.registerEntry(raceId, form);

		return "redirect:/races/" + raceId + "/entries/new";
	}

	@GetMapping("/races/{raceId}/entries")
	public String showPrediction(
			@PathVariable Integer raceId,
			Model model) {

		List<RaceEntryDto> entries = raceEntryService.getPredictionList(raceId);

		model.addAttribute("entries", entries);

		if (!entries.isEmpty()) {
			model.addAttribute("race", entries.get(0));
		}

		return "race-entries";
	}

	@PostMapping("/races/{raceId}/entries/{entryId}/delete")
	public String deleteEntry(
			@PathVariable Integer raceId,
			@PathVariable Integer entryId) {

		raceEntryService.deleteEntry(entryId);

		return "redirect:/races/" + raceId + "/entries/new";
	}

	//修正画面追加
	@GetMapping("/races/{raceId}/entries/{entryId}/edit")
	public String showEditForm(
			@PathVariable Integer raceId,
			@PathVariable Integer entryId,
			Model model) {

		RaceEntryDto dto = raceEntryService.getEntry(entryId);

		EntryForm form = new EntryForm();
		form.setEntryId(dto.getEntryId());
		form.setHorseId(dto.getHorseId());
		form.setHorseName(dto.getHorseName());
		form.setAge(dto.getAge());
		form.setSex(dto.getSex());
		form.setJockey(dto.getJockey());
		form.setFrameNumber(dto.getFrameNumber());
		form.setHorseNumber(dto.getHorseNumber());
		form.setOdds(dto.getOdds());
		form.setPopularity(dto.getPopularity());
		form.setRecentResult(dto.getRecentResult());

		// 追加項目
		form.setRunningStyle(dto.getRunningStyle());
		form.setDistanceAptitude(dto.getDistanceAptitude());
		form.setJockeyScore(dto.getJockeyScore());
		form.setPredictionMark(dto.getPredictionMark());

		model.addAttribute("raceId", raceId);
		model.addAttribute("entryForm", form);

		return "entry-edit-form";
	}

	//修正内容を保存
	@PostMapping("/races/{raceId}/entries/{entryId}/edit")
	public String updateEntry(
			@PathVariable Integer raceId,
			@PathVariable Integer entryId,
			EntryForm form) {

		form.setEntryId(entryId);

		raceEntryService.updateEntry(form);

		return "redirect:/races/" + raceId + "/entries/new";
	}

	//結果入力画面を追加
	@GetMapping("/races/{raceId}/results/edit")
	public String showResultEditForm(
			@PathVariable Integer raceId,
			Model model) {

		List<RaceEntryDto> entries = raceEntryService.getPredictionList(raceId);

		RaceResultForm form = new RaceResultForm();
		form.setEntries(entries);

		model.addAttribute("raceId", raceId);
		model.addAttribute("raceResultForm", form);

		if (!entries.isEmpty()) {
			model.addAttribute("race", entries.get(0));
		}

		return "result-edit-form";
	}

	//結果更新POSTを追加
	@PostMapping("/races/{raceId}/results/edit")
	public String updateResults(
			@PathVariable Integer raceId,
			RaceResultForm raceResultForm) {

		raceEntryService.updateResultRanks(raceResultForm.getEntries());

		return "redirect:/races/" + raceId + "/entries";
	}
}
