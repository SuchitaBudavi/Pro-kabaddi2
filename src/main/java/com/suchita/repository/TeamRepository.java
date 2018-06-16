package com.suchita.repository;

import org.springframework.data.repository.CrudRepository;
import com.suchita.beans.Team;

public interface TeamRepository extends CrudRepository<Team,Integer>{

	public Team findByTeamName(String teamName);
}
