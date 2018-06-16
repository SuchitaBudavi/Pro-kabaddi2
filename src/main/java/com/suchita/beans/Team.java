package com.suchita.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Team {

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer teamId;
	@Column(unique=true)
	private String teamName;
	private String teamHome;
	
	public Integer getTeamId() {
		return teamId;
	}
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getTeamHome() {
		return teamHome;
	}
	public void setTeamHome(String teamHome) {
		this.teamHome = teamHome;
	}	
}
