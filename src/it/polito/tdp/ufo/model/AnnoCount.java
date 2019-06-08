package it.polito.tdp.ufo.model;

import java.time.Year;

//Mette in relazione l'anno x con il numero di avvistamenti in tale anno
public class AnnoCount {
	private Year year;
	private Integer count;	//numero di avvistamenti
	
	public AnnoCount(Year year, Integer count) {
		super();
		this.year = year;
		this.count = count;
	}
	public Year getYear() {
		return year;
	}
	public void setYear(Year year) {
		this.year = year;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return String.format("%s (n° avvistamenti=%s)", year, count);
	}
	
	//Per ora non servono hashCode e Equals
	
	
}
