package com.ninet.mapBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapBuilder {	
	
	private static final String URL = "jdbc:Mysql://127.0.0.1:3306/courseworkbygroup50?useSSL=false";
	private static final String USER = "root";
	private static final String PASSWORD = "ht201196";
	
	private List<Station> nextStation = new ArrayList<Station>();
	private List<Station> shortestStaion = new ArrayList<Station>();
	private List<Station> leavesStation = new ArrayList<Station>();
	private Map<String, String> signMap = new HashMap<String, String>();
	private String start = "";
	private int stationCounter = 0;
	private int transferStationCounter = 0;
	private float distanceCounter = 0;
	private StringBuilder searchResult = new StringBuilder();
	private int counterBR = 0;
	
	public String show(String stationTo,String stationFrom,boolean isActual) throws ClassNotFoundException, SQLException{
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
		Statement statement = connection.createStatement();
		
		ResultSet rSet = statement.executeQuery("SELECT _signName,stationName from courseworkbygroup50.sign_info");
		while(rSet.next()){
			signMap.put(rSet.getString("_signName"), rSet.getString("stationName"));
		}
		Set signSet = signMap.keySet();
		if(signSet.contains(stationTo)){
			stationTo = signMap.get(stationTo);
		}
		if(signSet.contains(stationFrom)){
			stationFrom = signMap.get(stationFrom);
		}
		
		boolean isFirstNode = true;
		while(true){
			Iterator<Station> iterator = shortestStaion.iterator();
			while(iterator.hasNext()){
				Station lastStation = iterator.next();
				if(stationTo.equals(lastStation.getName())){
					Station showStation = shortestStaion.get(shortestStaion.size() - 1);
					String line = showStation.getLine(); 
					do {
						counterBR++;
						if(counterBR >= 7){
							counterBR = 0;
							searchResult.append("<br>");
						}
						searchResult.append(showStation.getName() + " -> ");
						if(!line.equals(showStation.getLine())){
							line = showStation.getLine();
							if(showStation.getLine() != null){
								stationCounter++;
								transferStationCounter++;
								counterBR = 0;
								searchResult.append("\n<br>Transfer to the line " + line + "£º " + showStation.getName() + " ->  ");
							}							
						}
						showStation = showStation.getPrevStation();
						stationCounter++;
					} while (showStation != null);
					stationCounter--;
					distanceCounter = shortestStaion.get(shortestStaion.size() - 1).getCurrentDistance();
					searchResult = searchResult.delete(searchResult.length()-3, searchResult.length());
					String resultView = "";

					if(isActual)
						resultView = "Total stops number: " + stationCounter + "<br>Transfer stations number: " 
									+ transferStationCounter + "<br>Total distance: " + new DecimalFormat("#.###").format(distanceCounter).toString() 
									+ " km" + "<br><br>The detail route: <br>" + searchResult;
					else
						resultView = "Total stops number: " + stationCounter + "<br>Transfer stations number: " 
									+ transferStationCounter + "<br>Total stops number(not include transfer +1): " 
									+ (stationCounter - transferStationCounter)  + "<br><br>The detail route: <br>" 
									+searchResult;
					System.out.println(resultView);
					return resultView;
				}
			}
			if(isFirstNode){
				findShortestRoute(stationFrom, stationTo,isFirstNode,isActual,statement);
			}		
			else {
				findShortestRoute(start, stationTo,isFirstNode,isActual,statement);
			}
			isFirstNode = false;
		}
	}
	
	public void findShortestRoute(String stationFrom, String stationTo,boolean isFirst,boolean isActual,Statement statement) throws SQLException, ClassNotFoundException {


		ResultSet resultSet = statement.executeQuery("SELECT _s2s,line,distance from courseworkbygroup50.station_info " + "WHERE _s2s LIKE '%" + stationFrom + "%'");

		if(isFirst){
			Station stationParent= new Station(stationFrom);
			stationParent.setCurrentDistance(0);
			stationParent.setPrevStation(null);
			while(resultSet.next()){
				String s2s = resultSet.getString("_s2s");
				String[] stations = s2s.split("-");
				String station1 = stations[0];
				String station2 = stations[1];
				String line = resultSet.getString("line");
				float distance = 0;
				if(isActual){
					distance = resultSet.getFloat("distance");
				}else if(isActual == false){
					distance = 1;
				}
				
				if(!station1.equals(stationParent.getName())){
					Station s1 = new Station(station1);
					s1.setPrevStation(stationParent);
					s1.setLine(line);
					s1.setCurrentDistance(s1.getPrevStation().getCurrentDistance() + distance);
					nextStation.add(s1);
				}else if(!station2.equals(stationParent.getName())){
					Station s2 = new Station(station2);
					s2.setPrevStation(stationParent);
					s2.setLine(line);
					s2.setCurrentDistance(s2.getPrevStation().getCurrentDistance() + distance);
					nextStation.add(s2);
				}
				}
			shortestStaion.add(stationParent);
			}
		
			else{
				while(resultSet.next()){
					String s2s = resultSet.getString("_s2s");
					String[] stations = s2s.split("-");
					String station1 = stations[0];
					String station2 = stations[1];
					String line = resultSet.getString("line");
					float distance = 0;
					if(isActual){
						distance = resultSet.getFloat("distance");
					}else if(isActual == false){
						distance = 1;
					}
					if(!station1.equals(stationFrom)){
						Station s1 = new Station(station1);
						Iterator<Station> iterator = shortestStaion.iterator();
						while(iterator.hasNext()){
							Station ss = iterator.next();
							if(ss.getName().equals(stationFrom)){
								s1.setPrevStation(ss);
							}
						}
						s1.setLine(line);
						s1.setCurrentDistance(s1.getPrevStation().getCurrentDistance() + distance);
						nextStation.add(s1);
					}else if(!station2.equals(stationFrom)){
						Station s2 = new Station(station2);
						Iterator<Station> iterator = shortestStaion.iterator();
						while(iterator.hasNext()){
							Station ss = iterator.next();
							if(ss.getName().equals(stationFrom)){
								s2.setPrevStation(ss);
							}
						}
						s2.setLine(line);
						s2.setCurrentDistance(s2.getPrevStation().getCurrentDistance() + distance);
						nextStation.add(s2);
					}
				}
			}		

		Iterator<Station> iterator = nextStation.iterator();
		while(iterator.hasNext()){
			Station next = iterator.next();
			List<String> shortestName = new ArrayList<String>();
			List<String> leavesName = new ArrayList<String>();
			Iterator<Station> iterator5 = shortestStaion.iterator();
			while(iterator5.hasNext()){
				shortestName.add(iterator5.next().getName());
			}
			Iterator<Station> iterator6 = leavesStation.iterator();
			while(iterator6.hasNext()){
				leavesName.add(iterator6.next().getName());
			}
			
			
			if(!shortestName.contains(next.getName())){
				if(!leavesName.contains(next.getName())){
					leavesStation.add(next);
				}else{
					Iterator<Station> iterator2 = leavesStation.iterator();

					List<Station> removeList = new ArrayList<Station>();
					List<Station> nextList = new ArrayList<Station>(); 
					for(Station leaves:leavesStation){
						if(leaves.getName().equals(next.getName())){
							if(next.getCurrentDistance() <= leaves.getCurrentDistance()){
								nextList.add(next);
								removeList.add(leaves);
							}
						}
					}
					leavesStation.addAll(nextList);
					leavesStation.removeAll(removeList);
				}		
			}
		}
		nextStation.clear();
		float min = Float.MAX_VALUE;
		Station shortestSta = null;
		for(Station s:leavesStation){
			if(s.getCurrentDistance() < min){
				min = s.getCurrentDistance();
				shortestSta = s;
			}
		}
		leavesStation.remove(shortestSta);
		shortestStaion.add(shortestSta);
		Station parentStation = shortestStaion.get(shortestStaion.size() - 1);
		start = parentStation.getName();
	}
		
}
