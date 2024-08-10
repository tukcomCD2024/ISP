package com.isp.backend.domain.scheduleImage.entity;

import com.isp.backend.domain.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	public ScheduleImage(Schedule schedule, String path) {
		this.schedule = schedule;
		this.path = path;
	}
}