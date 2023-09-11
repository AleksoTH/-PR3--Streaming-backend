package no.alek;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.google.gson.Gson;

import no.alek.datamodels.WeatherReport;

public class Converter {
	
	public static Gson gson = new Gson();
	
	public static JSONObject getJson(String xml) {
		try {
		    return XML.toJSONObject(xml);
		}catch(JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static String getJsonString(String xml) {
		JSONObject result = getJson(xml);
		if(result != null) {
			return result.toString();
		}
		return null;
	}
	
	public static WeatherReport getReportFromXML(String xml) {
		String js = getJsonString(xml);
		if(js != null) {
			return gson.fromJson(js, WeatherReport.class);
		}
		return null;
	}
	
	
	public static WeatherReport getReportFromJson(String json) {
		if(json != null) {
			return gson.fromJson(json, WeatherReport.class);
		}
		return null;
	}


	public static String getJsonString(WeatherReport report) {
	     return gson.toJson(report);
	}

}
