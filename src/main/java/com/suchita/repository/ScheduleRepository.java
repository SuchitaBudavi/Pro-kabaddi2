package com.suchita.repository;
import org.springframework.data.repository.CrudRepository;

import com.suchita.beans.Schedule;

public interface ScheduleRepository extends CrudRepository<Schedule, Integer> {

}
