package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.*;

import it.polito.tdp.ufo.model.AnnoCount;
import it.polito.tdp.ufo.model.Sighting;

public class SightingsDAO {
	
	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Sighting> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Sighting(res.getInt("id"),
							res.getTimestamp("datetime").toLocalDateTime(),
							res.getString("city"), 
							res.getString("state"), 
							res.getString("country"),
							res.getString("shape"),
							res.getInt("duration"),
							res.getString("duration_hm"),
							res.getString("comments"),
							res.getDate("date_posted").toLocalDate(),
							res.getDouble("latitude"), 
							res.getDouble("longitude"))) ;
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	
	public List<AnnoCount> getAnni() {
		String sql = "SELECT YEAR (DATETIME) as anno, COUNT(id) AS cnt " + 
				"FROM sighting " + 
				"WHERE country='us' " + 
				"GROUP BY YEAR (DATETIME) " ;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			List<AnnoCount> anni = new LinkedList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				anni.add(new AnnoCount(Year.of(res.getInt("anno")), res.getInt("cnt")));
			}
			
			conn.close();
			return anni ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	
	public List<String> getStati(Year anno) {
		String sql = "SELECT DISTINCT state " + 
				"FROM sighting " + 
				"WHERE country='us' "
				+ "and year(datetime) = ? ";
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			//Non c'e bisogno di creare una classe stati, ma basta usare String
			st.setInt(1, anno.getValue());
			
			List<String> stati = new LinkedList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				stati.add(res.getString("state"));
			}
			
			conn.close();
			return stati ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}


	public boolean esistenzaArco(String s1, String s2, Year anno) {
		// TODO Auto-generated method stub
		boolean flag=false;
		String sql ="SELECT COUNT(*) as cnt " + 
				"FROM Sighting s1, sighting s2 " + 
				"WHERE YEAR(s1.DATETIME)=year(s2.DATETIME) " + 
				"AND YEAR(s1.DATETIME)= ? " + 
				"AND s1.state=? " + 
				"AND s2.state=? " + 
				"AND s1.country='us' " + 
				"AND s2.country='us' " + 
				"AND s2.DATETIME > s1.DATETIME ";
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			//Non c'e bisogno di creare una classe stati, ma basta usare String
			st.setInt(1, anno.getValue());
			st.setString(2, s1);
			st.setString(3, s2);
			List<String> stati = new LinkedList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				if(res.getInt("cnt")>0)
					flag=true;
				else
					flag=false;
			}
			
			conn.close();
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		return flag;
	}
}
