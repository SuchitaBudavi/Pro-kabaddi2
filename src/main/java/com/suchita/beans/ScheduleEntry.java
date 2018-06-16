package com.suchita.beans;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ScheduleEntry {
	
	@Id @GeneratedValue
	private Integer entryId;
	private Integer team1;
	private String team1Name;
	private Integer team2;
	private String team2Name;
	private String location;
	private Date day;
	
	
	
	public String getTeam1Name() {
		return team1Name;
	}
	public void setTeam1Name(String team1Name) {
		this.team1Name = team1Name;
	}
	public String getTeam2Name() {
		return team2Name;
	}
	public void setTeam2Name(String team2Name) {
		this.team2Name = team2Name;
	}
	public Integer getEntryId() {
		return entryId;
	}
	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}
	public Integer getTeam1() {
		return team1;
	}
	public void setTeam1(Integer team1) {
		this.team1 = team1;
	}
	public Integer getTeam2() {
		return team2;
	}
	public void setTeam2(Integer team2) {
		this.team2 = team2;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Date getDay() {
		return day;
	}
	public void setDay(Date day) {
		this.day = day;
	}
	
	@Override
	public String toString() {
		return "Team1: "+team1+" Team2: "+team2+" Location: "+location+" Day: "+day;
	}
}
