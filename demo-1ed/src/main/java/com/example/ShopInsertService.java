package com.example;

import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class ShopInsertService {
	 

	 
	@RequestMapping(method = RequestMethod.POST, value = "/ShopInsert", produces = "application/json")
	@ResponseBody
	ArrayList<String> home(@RequestParam(value = "shopName", defaultValue = "null") String shopName,
			@RequestParam(value = "address", defaultValue = "null") String address) {
		
		ArrayList<String> resultArr = new ArrayList<String>();
		String[] dbResult = null,checkRes=null;
	
		//Checking for Parameters Pass	or not
	
		if (!shopName.equals("null") && !address.equals("null")) {
			
			//Checking for other User Accessing Same Shop
			if(!Application.controlList.contains(shopName)){
			try {
				//Calling DB for chacking shop present or not
				checkRes=DBHelper.CheckShopPresent(shopName);
				if(checkRes[0].contains("1")){
					//Concurrency Parameter updation  
					Application.controlList.add(checkRes[1]);
				}
				
				//Colling DB for Insert Shop
				dbResult = DBHelper.insertShopToDB(shopName, address);
				
				//Remove shop From Sncronize list to allow other user update shop 
				Application.controlList.remove(checkRes[1]);
				
				//Generating Result
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
				//If Someone is Accesing the Shop Requested by User
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
