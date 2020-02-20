package com.example.demo.helper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;

import com.example.demo.constants.DoctorQueryStringConstants;

public class DateHelper {
	
	private DateHelper(){}
	
	public static List<String> fromAvailability(String availability,int maxDaysAdded) throws Throwable {
				
		String startingDate;
		String endingDate;
		
		switch(availability) {
		case DoctorQueryStringConstants.THREE_DAYS_AVAILABILITY:
			startingDate=LocalDate.now().toString();
			endingDate=LocalDate.now().plusDays(3).toString();
			break;
		case DoctorQueryStringConstants.TODAY_AVAILABILITY:
			startingDate=LocalDate.now().toString();
			endingDate=LocalDate.now().toString();
			break;			
		case DoctorQueryStringConstants.ONE_DAY_AVAILABILITY:
			startingDate=LocalDate.now().toString();
			endingDate=LocalDate.now().plusDays(maxDaysAdded).toString();
			break;
		case DoctorQueryStringConstants.WEEKEND_AVAILABILITY:
			startingDate=LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SATURDAY)).toString();
			endingDate=LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).toString();
			break;
		default: throw new Throwable("No correct availability specified");
		}
		
		return Arrays.asList(startingDate,endingDate);
	}
}
