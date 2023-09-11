package no.alek.datamodels;

public class WeatherObservation {
	 private String credit;
	 private String credit_URL;
	 private WeatherImage image;
	 private String suggested_pickup;
	 private String suggested_pickup_period;
	 private String location;
	 private String station_id;
	 private String observation_time;
	 private String observation_time_rfc822;
	 private String temperature_string;
	 private String temp_f;
	 private String temp_c;
	 private String wind_string;
	 private String wind_dir;
	 private String wind_degrees;
	 private String wind_mph;
	 private String wind_gust_mph;
	 private String wind_kt;
	 private String pressure_string;
	 private String pressure_mb;
	 private String mean_wave_dir;
	 private String mean_wave_degrees;
	 private String disclaimer_url;
	 private String copyright_url;
	 private String privacy_policy_url;
	 
	public String getCredit() {
		return credit;
	}
	public String getCredit_URL() {
		return credit_URL;
	}
	public WeatherImage getImageObject() {
		return image;
	}
	public String getSuggested_pickup() {
		return suggested_pickup;
	}
	public String getSuggested_pickup_period() {
		return suggested_pickup_period;
	}
	public String getLocation() {
		return location;
	}
	public String getStation_id() {
		return station_id;
	}
	public String getObservation_time() {
		return observation_time;
	}
	public String getObservation_time_rfc822() {
		return observation_time_rfc822;
	}
	public String getTemperature_string() {
		return temperature_string;
	}
	public String getTemp_f() {
		return temp_f;
	}
	public String getTemp_c() {
		return temp_c;
	}
	public String getWind_string() {
		return wind_string;
	}
	public String getWind_dir() {
		return wind_dir;
	}
	public String getWind_degrees() {
		return wind_degrees;
	}
	public String getWind_mph() {
		return wind_mph;
	}
	public String getWind_gust_mph() {
		return wind_gust_mph;
	}
	public String getWind_kt() {
		return wind_kt;
	}
	public String getPressure_string() {
		return pressure_string;
	}
	public String getPressure_mb() {
		return pressure_mb;
	}
	public String getMean_wave_dir() {
		return mean_wave_dir;
	}
	public String getMean_wave_degrees() {
		return mean_wave_degrees;
	}
	public String getDisclaimer_url() {
		return disclaimer_url;
	}
	public String getCopyright_url() {
		return copyright_url;
	}
	public String getPrivacy_policy_url() {
		return privacy_policy_url;
	}
}
