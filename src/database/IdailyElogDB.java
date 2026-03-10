package database;

import java.util.ArrayList;

import models.DailyExerciseLog;

public interface IdailyElogDB {

	int insertDailyLog(DailyExerciseLog el);
	
	ArrayList<Double> getburnSum();

}
