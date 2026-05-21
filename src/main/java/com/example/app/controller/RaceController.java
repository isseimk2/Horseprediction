package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.dto.RaceDto;
import com.example.app.form.RaceForm;
import com.example.app.service.RaceService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RaceController {
	private final RaceService raceService;

	@GetMapping("/races")
	public String showRaceList(Model model) {
		model.addAttribute("races", raceService.getRaceList());
		return "race-list";
	}

	@GetMapping("/races/new")
	public String showRaceForm(Model model) {
		model.addAttribute("raceForm", new RaceForm());
		return "race-form";
	}

	@PostMapping("/races/new")
	public String registerRace(
			RaceForm form,
			Model model) {

		if (form.getRaceDate() == null
				|| form.getPlace() == null || form.getPlace().isBlank()
				|| form.getRaceName() == null || form.getRaceName().isBlank()
				|| form.getDistance() == null
				|| form.getSurface() == null || form.getSurface().isBlank()
				|| form.getWeather() == null || form.getWeather().isBlank()
				|| form.getTrackCondition() == null || form.getTrackCondition().isBlank()) {

			model.addAttribute("raceForm", form);
			model.addAttribute("errorMessage", "未入力の項目があります。すべて入力してください。");

			return "race-form";
		}

		Integer raceId = raceService.registerRace(form);

		return "redirect:/races/" + raceId + "/entries/new";
	}

	//削除処理
	@PostMapping("/races/{raceId}/delete")
	public String deleteRace(@PathVariable Integer raceId) {

		raceService.deleteRace(raceId);

		return "redirect:/races";
	}

	//修正画面
	@GetMapping("/races/{raceId}/edit")
	public String showEditForm(
			@PathVariable Integer raceId,
			Model model) {

		RaceDto dto = raceService.getRace(raceId);

		RaceForm form = new RaceForm();
		form.setRaceId(dto.getRaceId());
		form.setRaceDate(dto.getRaceDate());
		form.setPlace(dto.getPlace());
		form.setRaceName(dto.getRaceName());
		form.setDistance(dto.getDistance());
		form.setSurface(dto.getSurface());
		form.setWeather(dto.getWeather());
		form.setTrackCondition(dto.getTrackCondition());

		model.addAttribute("raceForm", form);

		return "race-edit-form";
	}

	//修正処理
	@PostMapping("/races/{raceId}/edit")
	public String updateRace(
			@PathVariable Integer raceId,
			RaceForm form,
			Model model) {

		if (form.getRaceDate() == null
				|| form.getPlace() == null || form.getPlace().isBlank()
				|| form.getRaceName() == null || form.getRaceName().isBlank()
				|| form.getDistance() == null
				|| form.getSurface() == null || form.getSurface().isBlank()
				|| form.getWeather() == null || form.getWeather().isBlank()
				|| form.getTrackCondition() == null || form.getTrackCondition().isBlank()) {

			form.setRaceId(raceId);
			model.addAttribute("raceForm", form);
			model.addAttribute("errorMessage", "未入力の項目があります。すべて入力してください。");

			return "race-edit-form";
		}

		form.setRaceId(raceId);

		raceService.updateRace(form);

		return "redirect:/races";
	}
}
