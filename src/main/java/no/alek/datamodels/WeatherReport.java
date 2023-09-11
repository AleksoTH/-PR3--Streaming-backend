package no.alek.datamodels;

public class WeatherReport {
	 WeatherObservation Current_observationObject;


	 // Getter Methods 

	 public WeatherObservation getCurrent_observation() {
	  return Current_observationObject;
	 }

	 // Setter Methods 

	 public void setCurrent_observation(WeatherObservation current_observationObject) {
	  this.Current_observationObject = current_observationObject;
	 }
	 
}
