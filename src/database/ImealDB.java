package database;

import java.util.ArrayList;

import models.Meal;
public interface ImealDB {

Meal getById(int mealID);	
	
int insertMeal(Meal m);

int updateMeal(Meal m);

int deleteMeal(Meal m);

ArrayList<Meal> getMeal();
	
}