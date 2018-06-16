package com.suchita.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.suchita.beans.Schedule;
import com.suchita.service.ScheduleService;

@RestController
public class ScheduleController {

	@Autowired
	ScheduleService scheduleService;
	
	@RequestMapping(value="/schedule")
	public Schedule getSchedule() {
		return scheduleService.getSchedule();
	}	
	
	@RequestMapping(value="/createSchedule")
	public Schedule createSchedule() {
		return scheduleService.createSchedule();
	}
	
	@RequestMapping(value="/schedule/{scheduleId}",method=RequestMethod.DELETE)
	public void deleteSchedule(@PathVariable("scheduleId") String scheduleId) {
		scheduleService.deleteSchedule(Integer.parseInt(scheduleId));
	}
}
