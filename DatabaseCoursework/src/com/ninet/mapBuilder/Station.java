package com.ninet.mapBuilder;

public class Station {
	
	private String name;
	private String line;
	private float currentDistance;
	private Station prevStation;
	
	public Station(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public float getCurrentDistance() {
		return currentDistance;
	}
	public void setCurrentDistance(float currentDistance) {
		this.currentDistance = currentDistance;
	}
	public Station getPrevStation() {
		return prevStation;
	}
	public void setPrevStation(Station prevStation) {
		this.prevStation = prevStation;
	}
	
}
