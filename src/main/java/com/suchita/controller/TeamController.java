package com.suchita.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.suchita.beans.Team;
import com.suchita.service.TeamService;

@RestController
public class TeamController {
	
	@Autowired
	TeamService teamService;

	/** 
	 * get details of a particular team
	 */
	@RequestMapping(value="/team/{teamName}")
	public Team getATeam(@PathVariable("teamName") String teamName) {
		return teamService.getTeamByName(teamName);
	}
	
	/** 
	 * get details of all teams
	 */
	@RequestMapping(value="/teams")
	public List<Team> getAllTeam() {
		return teamService.getAllTeam();
	}
	
	@RequestMapping(value="/team",method=RequestMethod.POST)
	public void addTeam(@RequestBody Team t) {
		teamService.addTeam(t);
	}
		
	@RequestMapping(value="/team/{teamId}",method=RequestMethod.DELETE)
	public void deleteTeam(@PathVariable("teamId") String teamId) {
		teamService.deleteTeam(Integer.parseInt(teamId));
	}
	
	
	@RequestMapping(value="/team",method=RequestMethod.PUT)
	public void updateTeam(@RequestBody Team t) {
		teamService.updateTeam(t);
	}
}