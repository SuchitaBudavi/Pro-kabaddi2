package com.suchita.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.suchita.beans.Schedule;
import com.suchita.beans.ScheduleEntry;
import com.suchita.beans.Team;
import com.suchita.service.TeamService;

@Service
public class ScheduleHelper {
		
	class LocationNcount{
		int count;
		String location;
	}
	
	public Schedule createSchedule(TeamService teamService) {
		List<Integer> consecutiveEntries = new ArrayList<Integer>();
		
		
		ArrayList<Team> teamList = (ArrayList<Team>) teamService.getAllTeam();		
		
		ArrayList<ArrayList<ScheduleEntry>> scheduleMatrix = prepareScheduleMatrix(teamList);
		
		Schedule schedule = prepareSequence(scheduleMatrix,consecutiveEntries);
		setLocationNday(schedule, teamList);
		return schedule;
	}
	
	/** 
	 * 
	 * @param teamList - contains list of all teams
	 * @return - matrix of schedule entries
	 * e.g. Team size = 4<p>
	 * T1T2 T1T3 T1T4<p>
	 * T2T3 T2T4<p>
	 * T3T4
	 */
	ArrayList<ArrayList<ScheduleEntry>> prepareScheduleMatrix(ArrayList<Team> teamList){
		int numOfTeam = 0;
		numOfTeam = teamList.size();
		
		ArrayList<ArrayList<ScheduleEntry>> scheduleMatrix = new ArrayList<ArrayList<ScheduleEntry>>();
		for (int i = 0; i < numOfTeam-1; i++) {
			ArrayList<ScheduleEntry> entryRow= new ArrayList<ScheduleEntry>();
			for(int j = i+1; j < numOfTeam; j++) {
				ScheduleEntry entry = new ScheduleEntry();
				entry.setTeam1(teamList.get(i).getTeamId());
				entry.setTeam1Name(teamList.get(i).getTeamName());
				entry.setTeam2(teamList.get(j).getTeamId());
				entry.setTeam2Name(teamList.get(j).getTeamName());
				/*if(j==i+1) {
					entry.setLocation(teamList.get(i).getTeamHome());
				}*/
				entryRow.add(entry);
			}
			scheduleMatrix.add(entryRow);
		}
		
		printScheduleMatrix(scheduleMatrix);
		return scheduleMatrix;
	}
	
	/** 
	 * 
	 * @param scheduleMatrix
	 * @param consecutiveEntries
	 * @return Schedule
	 */
	Schedule prepareSequence(ArrayList<ArrayList<ScheduleEntry>> scheduleMatrix,List<Integer> consecutiveEntries) {
		Schedule schedule = new Schedule();
		
		// phase 1 - copy all first elements of matrix row, skipping adjacent element
		for(int i = 0;i<scheduleMatrix.size();) {
			schedule.getScheduleEntries().add(scheduleMatrix.get(i).get(0));
			if(isConsecutive(schedule,scheduleMatrix.get(i).get(0),schedule.getScheduleEntries().size()-2)) {
				consecutiveEntries.add(schedule.getScheduleEntries().size()-1);
			}
			
			i = i + 2;
			if(i>scheduleMatrix.size()-1) {
				i = 1;
			}
			if(schedule.getScheduleEntries().size() == scheduleMatrix.size()) {
				break;
			}
		}
		
		// phase 2 - copy all subsequent elements moving diagonally downwards. Loop till 2nd last element as last element is already copied
		//j = down, i = right
		for(int i=1;i<scheduleMatrix.size()-1;i++) {
			for(int j=0;j<scheduleMatrix.size()-1 && i<scheduleMatrix.get(j).size();j++) {
				schedule.getScheduleEntries().add(scheduleMatrix.get(j).get(i));
				if(isConsecutive(schedule,scheduleMatrix.get(j).get(i),schedule.getScheduleEntries().size()-2)) {
					consecutiveEntries.add(schedule.getScheduleEntries().size()-1);
				}
			}
		}
		schedule.getScheduleEntries().add(scheduleMatrix.get(0).get(scheduleMatrix.get(0).size()-1));
		consecutiveEntries.add(schedule.getScheduleEntries().size()-1);
		
		//phase 3 - find duplicates and its adjusting position. Key - duplicatePos, value - adjustToPos
		HashMap<Integer,Integer> adjustDuplicateMap = adjustDuplicate(schedule, consecutiveEntries);
		Schedule finalSchedule = createFinalSchedule(schedule,adjustDuplicateMap);
		
		printSchedule(finalSchedule);
		return finalSchedule;
	}
	
	/** 
	 * 
	 * @param schedule
	 * @param teamList
	 * 
	 * assign location and date to all entries of schedule
	 */
	void setLocationNday(Schedule schedule, ArrayList<Team> teamList){
		
		HashMap<Integer,LocationNcount> locationMap = setLocationCount(schedule,teamList);
		Date startDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		
		for(ScheduleEntry entry : schedule.getScheduleEntries()) {
			entry.setLocation(nextLocation(locationMap, entry));
			entry.setDay(c.getTime());
			c.add(Calendar.DATE, 1);
		}
		
	}
	
	/** 
	 * 
	 * @param locationMap
	 * @param entry
	 * @return String
	 * next available location
	 */
	String nextLocation(HashMap<Integer,LocationNcount> locationMap, ScheduleEntry entry) {
		if(locationMap.get(entry.getTeam1()).count > 0) {
			locationMap.get(entry.getTeam1()).count--;
			return locationMap.get(entry.getTeam1()).location;
		}else {
			locationMap.get(entry.getTeam2()).count--;
			return locationMap.get(entry.getTeam2()).location;
		}
	}

	/** 
	 * 
	 * @param schedule
	 * @param teamList
	 * @return return Map of location and its count occurance
	 */
	HashMap<Integer,LocationNcount> setLocationCount(Schedule schedule, ArrayList<Team> teamList){
		HashMap<Integer,LocationNcount> locationMap = new HashMap<Integer,LocationNcount>();
		
		int count = schedule.getScheduleEntries().size() / teamList.size();
		int remainder = schedule.getScheduleEntries().size() % teamList.size();
		
		for (int i = 0; i < teamList.size(); i++) {
			LocationNcount lcount = new LocationNcount();
			lcount.count = count;
			lcount.location = teamList.get(i).getTeamHome();
			locationMap.put(i+1, lcount);
		}
		for(int i = 1; i <= remainder; i++) {
			locationMap.get(i).count = count + 1;
		}
		return locationMap;
	}
	
	
	/** 
	 * 
	 * @param schedule
	 * @param consecutiveEntries
	 * @return Map<Integer,Integer> - key duplicatePos, value adjustToPos
	 */
	HashMap<Integer,Integer> adjustDuplicate(Schedule schedule, List<Integer> consecutiveEntries) {
		HashMap<Integer,Integer> adjustDuplicateMap = new HashMap<Integer,Integer>();
		boolean topFlag = false, belowFlag = false;
		int i;
		for(Integer duplicatePos : consecutiveEntries) {
			for(i=0; i<schedule.getScheduleEntries().size();i++) {
				topFlag = isConsecutive(schedule, schedule.getScheduleEntries().get(duplicatePos), i);
				belowFlag = isConsecutive(schedule, schedule.getScheduleEntries().get(duplicatePos), i+1);
				if(topFlag == false && belowFlag == false) {
					if(i > duplicatePos) {
						adjustDuplicateMap.put(duplicatePos, i);
					}else {
						adjustDuplicateMap.put(duplicatePos, i+1);
					}
					break;
				}
			}
			if(i == schedule.getScheduleEntries().size()) {
				//adjustDuplicateMap.put(duplicatePos,i);
				//it cant be adjusted, introduce pause days
			}
		}
		return adjustDuplicateMap;
	}
	
	/** 
	 * 
	 * @param schedule
	 * @param adjustDuplicateMap
	 * @return schedule with corrected positions
	 * 
	 * element at duplicatePos will be inserted into adjustToPos and shift other elements
	 * 
	 */
	Schedule createFinalSchedule(Schedule schedule, HashMap<Integer,Integer> adjustDuplicateMap) {
		Schedule finalSchedule = new Schedule();
		Schedule tempSchedule = new Schedule();
		List<ScheduleEntry> scheduleEntries = schedule.getScheduleEntries();
		tempSchedule.getScheduleEntries().addAll(schedule.getScheduleEntries());
		
		if(adjustDuplicateMap.isEmpty()) {
			return schedule;
		}else {
			for(Map.Entry<Integer, Integer> entry : adjustDuplicateMap.entrySet()) {
				if(!finalSchedule.getScheduleEntries().isEmpty()) {
					schedule.getScheduleEntries().clear();
					schedule.getScheduleEntries().addAll(finalSchedule.getScheduleEntries());
					finalSchedule.getScheduleEntries().clear();
				}
				for(int i = 0;i<entry.getValue();i++) {
					if(i != entry.getKey()) {
						finalSchedule.getScheduleEntries().add(schedule.getScheduleEntries().get(i));
					}
				}
				
				finalSchedule.getScheduleEntries().add(scheduleEntries.get(entry.getKey()));
				
				if(finalSchedule.getScheduleEntries().size() != scheduleEntries.size()) {	
					for(int i=entry.getValue();i<scheduleEntries.size();i++) {
						if(i != entry.getKey()) {
							finalSchedule.getScheduleEntries().add(scheduleEntries.get(i));
						}
					}
				}
			}
			return finalSchedule;
		}
	}
	
	/** 
	 * 
	 * @param schedule
	 * @param entry
	 * @param pos
	 * @return true - if any of the team in entry matches any of the team in pos
	 * other wise false
	 */
	boolean isConsecutive(Schedule schedule, ScheduleEntry entry,int pos) {
		if(pos >= 0 && pos < schedule.getScheduleEntries().size()) {
			ScheduleEntry compareToEntry = schedule.getScheduleEntries().get(pos);
			if(entry.getTeam1() == compareToEntry.getTeam1() || entry.getTeam1() == compareToEntry.getTeam2()
					|| entry.getTeam2() == compareToEntry.getTeam1() || entry.getTeam2() == compareToEntry.getTeam2()) {
				return true;
			}
		}
		return false;
	}
	
	void printScheduleMatrix(ArrayList<ArrayList<ScheduleEntry>> scheduleMatrix) {
		System.out.println("<---------- Schedule Matrix -------------->");
		for(ArrayList<ScheduleEntry> entryRow : scheduleMatrix) {
			System.out.println("");
			for(ScheduleEntry entry : entryRow) {
				System.out.println(entry);
			}
		}
	}
	
	void printSchedule(Schedule schedule) {
		System.out.println("<---------- Schedule -------------->");
		for(ScheduleEntry entry : schedule.getScheduleEntries()) {
			System.out.println(entry);
		}
	}
}
