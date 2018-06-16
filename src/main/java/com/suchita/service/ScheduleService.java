package com.suchita.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suchita.beans.Schedule;
import com.suchita.helper.ScheduleHelper;
import com.suchita.repository.ScheduleRepository;


@Service
public class ScheduleService {

	@Autowired
	ScheduleRepository scheduleRepo;
	
	@Autowired
	TeamService teamService;
	
	public Schedule getSchedule() {
		ArrayList<Schedule> scheduleList = new ArrayList<Schedule>(); 
		Iterable<Schedule> i = scheduleRepo.findAll();
		for (Schedule schedule : i) {
			scheduleList.add(schedule);
		}
		if(scheduleList.isEmpty()) {
			return null;
		}else {
			return scheduleList.get(0);
		}
	}
	
	public Schedule createSchedule() {
		ScheduleHelper scheduleHelper= new ScheduleHelper();
		Schedule schedule = scheduleHelper.createSchedule(teamService);
		
		scheduleRepo.save(schedule);
		
		return schedule;
	}
	
	public void deleteSchedule(int scheduleId) {
		scheduleRepo.delete(scheduleId);
	}
}
