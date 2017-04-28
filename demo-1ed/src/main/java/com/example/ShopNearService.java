package com.example;

import java.awt.PageAttributes.MediaType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class ShopNearService {

	@RequestMapping(method = RequestMethod.POST, value = "/ShopNear", produces = "application/json")
	@ResponseBody
	Map<String, ArrayList<String>> home(@RequestParam(value = "longitude", defaultValue = "0") Double longitude,
			@RequestParam(value = "latitude", defaultValue = "0") Double latitude) throws JSONException {

		Map<String, ArrayList<String>> resaltMap = new HashMap<String, ArrayList<String>>();
		
		
			resaltMap = findNearestShops(longitude, latitude);
			return resaltMap;
		
	       }

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ShopInsertService.class, args);
	}

	
	Map<String, ArrayList<String>> findNearestShops(double longitude,double latitude)
	{
		Map<String, ArrayList<String>> resultMap = new HashMap<String, ArrayList<String>>();
		try{
		DBHelper.getAllLatLong();
		Double finallat = null;
		Double finallon = null;
		Double secondlargestlat = null, secondlargestlon = null;
		Float minDist = 0f, secondminDist = 0f;
		for (int i = 0; i < DBHelper.lat.size(); i++) {
			float dist = 0;
			dist = distFrom(longitude, latitude, (Double) DBHelper.lat.get(i), (Double) DBHelper.lon.get(i));
			System.out.println("Mindist" + minDist + "      dist" + dist);
			if (i == 0) {
				minDist = dist;
				finallat = DBHelper.lat.get(i);
				finallon = DBHelper.lon.get(i);
			}
			if (minDist > dist) {

				secondlargestlat = finallat;
				secondlargestlon = finallon;
				finallat = DBHelper.lat.get(i);
				finallon = DBHelper.lon.get(i);
				System.out.println("in................" + finallat + "" + finallon);
				secondminDist = minDist;
				minDist = dist;

			}
		}

		System.out.println("output................" + finallat + "" + finallon);
		String[] res = DBHelper.getShopName(finallon, finallat);

	
		ArrayList<String> addresswithLatLong = new ArrayList<String>();
		addresswithLatLong.add("Shop Adress :- " + res[1]);
		addresswithLatLong.add("Shop Away from you :-" + minDist / 1000 + " Km");
		addresswithLatLong.add(" Logitude :- " + finallon);
		addresswithLatLong.add(" Latitude :- " + finallat);

		String[] ressecond = DBHelper.getShopName(secondlargestlon, secondlargestlat);

		ArrayList<String> addresswithLatLongSecond = new ArrayList<String>();
		addresswithLatLongSecond.add("Shop Adress :- " + ressecond[1]);
		addresswithLatLongSecond.add("Shop Away from you :-" + secondminDist / 1000 + " Km");
		addresswithLatLongSecond.add(" Logitude :- " + secondlargestlon);
		addresswithLatLongSecond.add(" Latitude :- " + secondlargestlat);

		resultMap.put("Second Nearest shopName :- " + ressecond[0], addresswithLatLongSecond);
		resultMap.put("Nearest shopName :- " + res[0], addresswithLatLong);
		
		}catch(Exception e){
			
			ArrayList<String> addresswithLatLongSecond = new ArrayList<String>();
			addresswithLatLongSecond.add("Description :- Not Enough Entrys " );
			resultMap.put("Error :- ", addresswithLatLongSecond);
	
		}

		return resultMap;

	}

	public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float dist = (float) (earthRadius * c);

		return dist;
	}

}
