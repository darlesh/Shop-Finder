package com.example;

import java.awt.PageAttributes.MediaType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class ShopInsertService {
	 

	 
	@RequestMapping(method = RequestMethod.POST, value = "/ShopInsert", produces = "application/json")
	@ResponseBody
	ArrayList<String> home(@RequestParam(value = "shopName", defaultValue = "null") String shopName,
			@RequestParam(value = "address", defaultValue = "null") String address) throws JSONException {
		
		ArrayList<String> resultArr = new ArrayList<String>();
		String[] dbResult = null,checkRes=null;
		
		if (!shopName.equals("null") && !address.equals("null")) {
			if(!Application.controlList.contains(shopName)){
			try {
				
				checkRes=DBHelper.CheckShopPresent(shopName);
				if(checkRes[0].contains("1")){
					Application.controlList.add(checkRes[1]);
				}
				
				
				dbResult = DBHelper.insertShopToDB(shopName, address);
				
				System.out.println("resssssss"+checkRes[1]+Application.controlList.contains(checkRes[1]));
				
				Application.controlList.remove(checkRes[1]);
				if (dbResult[0].equals("0")) {
					resultArr.add("Status : Success" );
					resultArr.add("Description : Added Shop '" + shopName + "' and address '" + address + "'");
					return resultArr;
				} else if (dbResult[0].equals("1")) {
					resultArr.add("Status : Updated" );
					resultArr.add("Description :  Updated Shop '" + shopName + "'.Updated address as '" + address
							+ "'  .Repleced Address is '" + dbResult[1] + "'");
					return resultArr;
				}
			} catch (SQLException e) {

			}
			
			}else{
				resultArr.add("Status : Concurrency Control" );
				resultArr.add("Description :  Opps...Somone is also Accessing same data");
				return resultArr;
			}
			
		} else {
			
			resultArr.add("Status :  NotParameters" );
			resultArr.add("Description :  Please Pass Parameters like Shop name and address");
			return resultArr;
		}

		

		resultArr.add("Status :  Error" );

		resultArr.add("Description :  Something Went Wrong");

		
		return resultArr;
	}

}