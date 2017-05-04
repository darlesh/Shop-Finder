package com.example;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;


	public class GeocodeImplementation {

	/*Geocode request URL. Here see we are passing "json" it means we will get the output in JSON format.
	* You can also pass "xml" instead of "json" for XML output.
	* For XML output URL will be "http://maps.googleapis.com/maps/api/geocode/xml"; 
	*/

	 public static String[] getLatLongPositions(String address) throws Exception
         {
                   int responseCode = 0;
                   String api = "http://maps.googleapis.com/maps/api/geocode/xml?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=true";
                   URL url = new URL(api);
                   //System.setProperty("http.proxyHost", "10.2.11.31");     //Update Proxy Here
                   //System.setProperty("http.proxyPort", "8585");           //Also Update Port Here

                   HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();

                   httpConnection.connect();
                   responseCode = httpConnection.getResponseCode();
                   if(responseCode == 200)
                   {
                     DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();;
                     Document document = builder.parse(httpConnection.getInputStream());
                     XPathFactory xPathfactory = XPathFactory.newInstance();
                     XPath xpath = xPathfactory.newXPath();
                     XPathExpression expr = xpath.compile("/GeocodeResponse/status");
                     String status = (String)expr.evaluate(document, XPathConstants.STRING);
                     if(status.equals("OK"))
                     {
                        expr = xpath.compile("//geometry/location/lat");
                        String latitude = (String)expr.evaluate(document, XPathConstants.STRING);
                        expr = xpath.compile("//geometry/location/lng");
                        String longitude = (String)expr.evaluate(document, XPathConstants.STRING);
                        return new String[] {latitude, longitude};
                     }
                     else
                     {
                        throw new Exception("Error from the API - response status: "+status);
                     }
                   }
                   return null;

         }
	}
	
	

