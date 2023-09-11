package no.alek;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import no.alek.datamodels.WeatherReport;

public class KafkaWeatherFeed {
	public static String TOPIC = "weather-events";
	public static String broker = "localhost:6667";
	 
	static class Task extends TimerTask {
		Task() throws MalformedURLException {
			weatherURL = new URL("http://w1.weather.gov/xml/current_obs/all_xml.zip");
		}
		@Override
		public void run() {
			//every hour, send a complete dataset to a topic on kafka.
			try {
		        Properties props = new Properties();
		        props.put("metadata.broker.list", broker);
		        props.put("zk.connect", "localhost:2181");
		        props.put("serializer.class", "kafka.serializer.StringEncoder");
		        props.put("request.required.acks", "1");

		        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
		        
				URLConnection conn = weatherURL.openConnection();
				ZipInputStream zis = new ZipInputStream(conn.getInputStream());
				ZipEntry ze = null;
				while((ze = zis.getNextEntry()) != null) {
					if(ze.getName().indexOf(".") != 4)
						continue;
					StringWriter writer = new StringWriter();
					IOUtils.copy(zis, writer, StandardCharsets.UTF_8);
					String weatherReport = writer.toString();		
					WeatherReport report = Converter.getReportFromXML(weatherReport);
					ProducerRecord<String, String> data = new ProducerRecord<String, String>(TOPIC, report.toString());
	                producer.send(data);
				}
				producer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		URL weatherURL;
	}
	public static void main(String[] args) {
		try {
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new Task(), 0, 60*60*1000);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
