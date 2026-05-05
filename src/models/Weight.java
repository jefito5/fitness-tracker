package models;

import java.sql.Date;
public class Weight {
	private int weightId;
	private double weightM;
	private double weightE;
	private int userId;
	private Date date;
	private double average;
	private String period1;
	private String period2;
	
	public String getPeriod1() {
		return period1;
	}
	public void setPeriod1(String period1) {
		this.period1 = period1;
	}
	public String getPeriod2() {
		return period2;
	}
	public void setPeriod2(String period2) {
		this.period2 = period2;
	}
	public double getAverage() {
		return average;
	}
	public void setAverage(double average) {
		this.average = average;
	}
	public int getWeightId() {
		return weightId;
	}
	public void setWeightId(int weightId) {
		this.weightId = weightId;
	}
	public double getWeightM() {
		return weightM;
	}
	public void setWeightM(double mrng_wt) {
		this.weightM = mrng_wt;
	}
	public double getWeightE() {
		return weightE;
	}
	public void setWeightE(double weightE) {
		this.weightE = weightE;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
