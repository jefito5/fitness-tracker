package database;

import java.util.ArrayList;

import models.DailyMealLog;

public interface IdailylogDB {

	int insertDailyLog(DailyMealLog ml);
	
	ArrayList<DailyMealLog> getAll();
	
	ArrayList<Double> getSum();
}
