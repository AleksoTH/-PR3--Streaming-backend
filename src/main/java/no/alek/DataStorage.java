package no.alek;

import org.apache.spark.sql.SparkSession;

import com.google.gson.Gson;

import io.delta.tables.DeltaTable;
import no.alek.datamodels.WeatherReport;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;

//delta lake
public class DataStorage {

	SparkSession session = null;

	DataStorage() {
		session = SparkSession.builder().master("local[1]").appName("DataConverter").getOrCreate();

		Dataset<Row> df = session.readStream().format("kafka")
				.option("kafka.bootstrap.servers", KafkaWeatherFeed.broker).option("subscribe", KafkaWeatherFeed.TOPIC)
				.option("startingOffsets", "earliest") // From starting
				.load();
		df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)");
		try {
			df.writeStream().foreachBatch(new VoidFunction2<Dataset<Row>, Long>() {
				public void call(Dataset<Row> dataset, Long batchId) {
					List<Row> all = dataset.collectAsList();
					for (Row ex : all) {
						String value = ex.getString(0);
						WeatherReport report = Converter.getReportFromJson(value);
						ingest(report);
					}
				}
			}).start();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void ingest(WeatherReport report) {
		List<wrapper> wrapList = new ArrayList<wrapper>();
		wrapper wrapper = new wrapper();
		wrapper.eventDate = new java.sql.Date(System.currentTimeMillis());
		wrapper.eventOrigin = report.getCurrent_observation().getStation_id();
		wrapper.eventTime = new java.sql.Timestamp(System.currentTimeMillis());
		wrapper.eventType = "weather";
		wrapper.data = Converter.getJsonString(report);
		wrapList.add(wrapper);
		Encoder<wrapper> wEncoder = Encoders.bean(wrapper.class);
		Dataset<Row> wrapDS = session.createDataset(wrapList, wEncoder).toDF();
		wrapDS.write().format("delta").insertInto("default.events");
	}

	List<WeatherReport> getReportForDayByStation(String station, Timestamp start, Timestamp end) {
		// We will never accept user input.
		Dataset<wrapper> results = session.sql("SELECT * FROM default.events WHERE eventTime >= \"" + start + "\" <= \""
				+ end + "\" AND eventOrigin = '" + station + "'  LIMIT 100").as(Encoders.bean(wrapper.class));

		List<WeatherReport> report = new ArrayList<WeatherReport>();
		for (wrapper wrap : results.collectAsList()) {
			WeatherReport unwrapped = Converter.getReportFromJson(wrap.data);
			report.add(unwrapped);
		}
		return report;
	}

}

class wrapper {
	long eventId;
	String data;
	String eventOrigin;
	String eventType;
	Timestamp eventTime;
	Date eventDate;
}