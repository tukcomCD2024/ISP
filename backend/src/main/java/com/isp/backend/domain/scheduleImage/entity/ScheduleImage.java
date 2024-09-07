package com.isp.backend.domain.scheduleImage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.isp.backend.domain.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "schedule_image")
public class ScheduleImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "schedule_id")
	private Schedule schedule;
	private String path;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime saveDate;

	public ScheduleImage(Schedule schedule, LocalDateTime saveDate, String path) {
		this.schedule = schedule;
		this.saveDate = saveDate;
		this.path = path;
	}
}