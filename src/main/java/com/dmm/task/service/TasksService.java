package com.dmm.task.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TasksRepository;

@Service
@Transactional
public class TasksService {

	@Autowired
	private TasksRepository tasksRepository;

	public void insert(Tasks tasks) {
		tasksRepository.save(tasks);
	}

	public void update(Tasks tasks) {
		tasksRepository.save(tasks);
	}

	public void delete(Integer id) {
		tasksRepository.deleteById(id);
	}

	public Optional<Tasks> selectById(Integer id) {
		return tasksRepository.findById(id);
	}

	public List<Tasks> selectByDateBetween(LocalDate from, LocalDate to) {
		return tasksRepository.findByDateBetween(from, to);
	}

	public List<Tasks> selectByDateBetweenName(LocalDate from, LocalDate to, String name) {
		return tasksRepository.findByDateBetweenName(from, to, name);
	}

	public List<Tasks> findAllByDate(LocalDate date) {
		return tasksRepository.findAllByDate(date);
	}

}
