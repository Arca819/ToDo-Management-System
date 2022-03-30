package com.dmm.task.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.form.TaskForm;
import com.dmm.task.service.AccountUserDetails;
import com.dmm.task.service.TasksService;

@Controller
public class TaskController {
	@Autowired
	private TasksService tasksService;

	@GetMapping("/main")
	public String list(@AuthenticationPrincipal AccountUserDetails user, Model model) {
		// 今月の月名を設定
		LocalDate myDate = LocalDate.now();
		int myYear = myDate.getYear();
		int myMonth = myDate.getMonthValue();
		model.addAttribute("month", myYear + "年" + myMonth + "月");

		// 今月1日の曜日
		LocalDate m1 = LocalDate.of(myYear, myMonth, 1);
		int wNo = m1.getDayOfWeek().getValue();
		// 今月1日を含む週の初日（日曜日）
		LocalDate firstDate = m1;
		if (wNo != 7) {
			firstDate = m1.minusDays(wNo);
		}
		// 今月末日の曜日
		int de = m1.lengthOfMonth();
		LocalDate me = LocalDate.of(myYear, myMonth, de);
		int we = me.getDayOfWeek().getValue();
		// 今月末日を含む週の末日（土曜日）
		LocalDate lastDate = me.plusDays(6 - (we % 7));

		// カレンダーマトリクスの作成
		List<List<LocalDate>> matrix = new ArrayList<>();
		LocalDate d1 = firstDate;
		while (d1.compareTo(lastDate) <= 0) {
			List<LocalDate> weekDays = new ArrayList<>();
			for (int i = 0; i < 7; i++) {
				weekDays.add(d1);
				d1 = d1.plusDays(1);
			}
			matrix.add(weekDays);
		}
		model.addAttribute("matrix", matrix);
		// タスクマップの作成
		Map<LocalDate, List<Tasks>> tasks = new HashMap<>();
		LocalDate day = firstDate;
		List<Tasks> taskList = new ArrayList<>();
		while (day.compareTo(lastDate) <= 0) {
			if (user.getName().equals("admin-name")) {
				taskList = tasksService.findAllByDate(day);
			} else {
				taskList = tasksService.selectByDateBetweenName(day, day, user.getName());
			}
			tasks.put(day, taskList);
			day = day.plusDays(1);
		}
		model.addAttribute("tasks", tasks);

		return "main";
	}

	@GetMapping("/create/{date}")
	public String post(@PathVariable("date") String date, @ModelAttribute TaskForm taskForm, Model model) {
		LocalDate myDate = LocalDate.parse(date);
		model.addAttribute("date", myDate);
		return "create";
	}

	@PostMapping("/create")
	public String regist(@ModelAttribute TaskForm taskForm, @AuthenticationPrincipal AccountUserDetails user) {
		Tasks task = new Tasks();
		task.setTitle(taskForm.getTitle());
		task.setName(user.getName());
		task.setText(taskForm.getText());
		task.setDate(taskForm.getDate());
		task.setDone(false);
		tasksService.insert(task);
		return "redirect:/main";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, @ModelAttribute Tasks task, Model model) {
		Optional<Tasks> taskOptional = tasksService.selectById(id);
		task = taskOptional.get();
		model.addAttribute("task", task);
		return "edit";
	}

	@PostMapping("edit/{id}")
	public String regist(@PathVariable("id") Integer id, @ModelAttribute TaskForm taskForm,
			@AuthenticationPrincipal AccountUserDetails user) {
		Tasks task = new Tasks();
		task.setId(id);
		task.setTitle(taskForm.getTitle());
		task.setName(user.getName());
		task.setText(taskForm.getText());
		task.setDate(taskForm.getDate());
		task.setDone(taskForm.getDone());
		tasksService.update(task);
		return "redirect:/main";
	}

	@PostMapping("delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		tasksService.delete(id);
		return "redirect:/main";
	}
}
