package com.dmm.task.data.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table(name = "tasks")
@Data
public class Tasks {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String title;
	private String name;
	private String text;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	private Boolean done;
}
