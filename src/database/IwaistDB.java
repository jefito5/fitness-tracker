package database;

import java.util.ArrayList;

import models.Waist;

public interface IwaistDB {
	
	Waist getById(int waistID);
	
	int insertWaist(Waist ww);
	
	int updateWaist(Waist ww);
	
	ArrayList<Waist> getWaist();
	ArrayList<Double> getLists();
}

