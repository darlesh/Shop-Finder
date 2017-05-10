package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBHelper {
	public static ArrayList<Double> lat, lon;

	//Getting Connection
	private static Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			Class.forName("org.h2.Driver");

		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			dbConnection = DriverManager.getConnection("jdbc:h2:~/test", "", "");
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}

	//Createing Schema
	public static int CreateTable() {
		Connection connection = getDBConnection();
		Statement stmt = null;
		try {
			connection.setAutoCommit(false);
			stmt = connection.createStatement();
			//stmt.execute("DROP TABLE Shops");
			stmt.execute(
					"CREATE TABLE IF NOT EXISTS Shops(shopName varchar(255) primary key, address varchar(255), lon varchar(25),lat varchar(25))");

			stmt.close();
			connection.commit();
			return 1;
		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return 0;
	}

	//Checking Shop Is Present or not 
	public static String[] CheckShopPresent(String ShopName) throws SQLException {
		Connection connection = getDBConnection();
		Statement stmt = null;
		int updateFlag = 0;
		String lastAddress = null;
		try {
			connection.setAutoCommit(true);
			stmt = connection.createStatement();


			ResultSet rs = stmt.executeQuery("select * from Shops where shopname='" + ShopName + "'");

			if (rs.absolute(1)) {
				lastAddress = rs.getString("address");
				updateFlag = 1;
				
			} 
			stmt.close();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// connection.close();
		}
		String[] res = { "" + updateFlag, ShopName};
		return res;
	}
	
	
	//Inser Shop To db
	public static String[] insertShopToDB(String ShopName, String address) throws SQLException {
		Connection connection = getDBConnection();
		Statement stmt = null;
		int updateFlag = 0;
		String lastAddress = null;
		try {
			connection.setAutoCommit(true);
			stmt = connection.createStatement();

			String[] loglat = GeocodeImplementation.getLatLongPositions(address);

			ResultSet rs = stmt.executeQuery("select * from Shops where shopname='" + ShopName + "'");

			if (rs.absolute(1)) {
				lastAddress = rs.getString("address");
				updateFlag = 1;
				updateShopToDB(ShopName, address, loglat);
			} else {
				stmt.execute("INSERT INTO Shops(shopName, address,lat,lon) VALUES('" + ShopName + "', '" + address
						+ "','" + Double.parseDouble(loglat[0]) + "','" + Double.parseDouble(loglat[1]) + "')");
				System.out.println("Shop Inserted.............");
			}
			ResultSet rs1 = stmt.executeQuery("select * from Shops");

			while (rs1.next()) {
				System.out.println("Id " + rs.getString("shopName") + " Name " + rs.getString("address"));
			}

			stmt.close();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// connection.close();
		}
		String[] res = { "" + updateFlag, lastAddress, address };
		return res;
	}

	//If Shop Is Present then update Address in db
	public static void updateShopToDB(String ShopName, String address, String[] loglat) throws SQLException {
		Connection connection = getDBConnection();
		Statement stmt = null;
		try {
			connection.setAutoCommit(true);
			stmt = connection.createStatement();
			stmt.execute("UPDATE Shops SET address = '" + address + "' , lat = '" + Double.parseDouble(loglat[0]) + "', lon = '" + Double.parseDouble(loglat[1])
					+ "'  WHERE shopName = '" + ShopName + "'");
			
			ResultSet rs = stmt.executeQuery("select * from Shops");
			System.out.println("Shop Updated............");
			while (rs.next()) {
				System.out.println("Id " + rs.getString("shopName") + " Name " + rs.getString("address") + " log "
						+ rs.getString("lon") + " lat " + rs.getString("lat"));
			}

			stmt.close();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// connection.close();
		}
	}

	//Getting LAT and LONG
	public static Double[][] getAllLatLong() {
		// TODO Auto-generated method stub
		lat = new ArrayList();
		lon = new ArrayList();
		Connection connection = getDBConnection();
		Statement stmt = null;
		try {
			connection.setAutoCommit(true);
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery("Select lat,lon from Shops");
			System.out.println("Shop Getting............");
			int i = 0, j = 0;
			while (rs.next()) {

				System.out.println(" log " + rs.getString("lon") + " lat " + rs.getString("lat"));
				lat.add(Double.parseDouble(rs.getString("lat")));
				lon.add(Double.parseDouble(rs.getString("lon")));

			}
			// System.out.println("arrrrrrrrrrrrrrrrrr"+ lat.get(0));

			stmt.close();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return null;
	}

	//Getting Shop name from Geolocation
	public static String[] getShopName(Double lon, Double lat) {
		// TODO Auto-generated method stub

		Connection connection = getDBConnection();
		Statement stmt = null;

		try {
			connection.setAutoCommit(true);
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery("Select * from Shops where lon='" + lon + "' and lat='" + lat + "'");
			System.out.println("Shop Getting............");

			while (rs.next()) {

				System.out.println(" log " + rs.getString("shopName") + " lat " + rs.getString("address"));

				String res[] = { rs.getString("shopName"), rs.getString("address") };
				return res;
			}
			// System.out.println("arrrrrrrrrrrrrrrrrr"+ lat.get(0));

			stmt.close();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return null;
	}

}
