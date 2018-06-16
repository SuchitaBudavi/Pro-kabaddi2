package com.suchita.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suchita.beans.Team;
import com.suchita.repository.TeamRepository;

@Service
public class TeamService {

	@Autowired
	TeamRepository teamRepo;
	
	public Team getTeamByName(String teamName) {
		return teamRepo.findByTeamName(teamName);
	}
	
	public List<Team> getAllTeam() {
		ArrayList<Team> teams = new ArrayList<Team>(); 
		Iterable<Team> i = teamRepo.findAll();
		for (Team team : i) {
			teams.add(team);
		}
		return teams;
	}
	
	public void addTeam(Team t) {
		teamRepo.save(t);
	}
	
	public void deleteTeam(Integer id) {
		teamRepo.delete(id);
	}
	
	public void updateTeam(Team t) {
		teamRepo.save(t);
	}
	
}
