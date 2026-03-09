package database;
import java.util.ArrayList;
import models.Weight;

public interface IweightDB {
	
	int insertWeight(Weight w);
	
	int updateWeight(Weight w);
	
	ArrayList<Double> getWeight(Weight wt);
	
	ArrayList<Double> getAverages();
	ArrayList<Double> getList();
	ArrayList<Double> getStartEnd();
		
}
