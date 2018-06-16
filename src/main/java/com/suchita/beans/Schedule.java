package com.suchita.beans;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@Entity
public class Schedule {

	@Id @GeneratedValue
	private Integer scheduleId;
	@OneToMany(cascade=CascadeType.ALL)
	private List<ScheduleEntry> scheduleEntries = new ArrayList<ScheduleEntry>();
	
	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
	public List<ScheduleEntry> getScheduleEntries() {
		return scheduleEntries;
	}
	public void setScheduleEntries(List<ScheduleEntry> scheduleEntries) {
		this.scheduleEntries = scheduleEntries;
	}
}
