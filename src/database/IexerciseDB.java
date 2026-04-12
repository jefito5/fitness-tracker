package database;

import java.util.ArrayList;

import models.Exercise;
//import models.Meal;

public interface IexerciseDB {
	
	Exercise getById(int exerciseID);
	
	int insertExercise(Exercise m);

	int updateExercise(Exercise m);

	int deleteExercise(Exercise m);

	ArrayList<Exercise> getExercise();		
}