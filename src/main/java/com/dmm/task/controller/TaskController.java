package com.dmm.task.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TaskController {
	@GetMapping("/main")
	public String main() {
		return "main";
	}

	@GetMapping("/main/create/{date}")
	public String create(@PathVariable("date") String date, Model model) {
		LocalDate myDate = LocalDate.parse(date);
		model.addAttribute("date", myDate);
		return "create";
	}

	@GetMapping("/main/edit/{id}")
	public String edit(@PathVariable("id") Integer id) {
		return "edit";
	}

}
