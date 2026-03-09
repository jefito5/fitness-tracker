package models;

import java.sql.Date;

public class Waist {
	private int waistId;
	private double waistM;
	private double waistE;
	private int userId;
	private Date date;
	private double average;
	
	public double getAverage() {
		return average;
	}
	public void setAverage(double average) {
		this.average = average;
	}
	public int getWaistId() {
		return waistId;
	}
	public void setWaistId(int waistId) {
		this.waistId = waistId;
	}
	public double getWaistM() {
		return waistM;
	}
	public void setWaistM(double mrng_wst) {
		this.waistM = mrng_wst;
	}
	public double getWaistE() {
		return waistE;
	}
	public void setWaistE(double waistE) {
		this.waistE = waistE;
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
