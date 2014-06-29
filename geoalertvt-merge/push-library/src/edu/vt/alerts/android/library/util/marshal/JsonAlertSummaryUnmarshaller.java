package edu.vt.alerts.android.library.util.marshal;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.vt.alerts.android.library.api.domain.summary.AlertSummary;

/**
 * A marshaller that converts incoming JSON data into AlertSummary objects.
 * 
 * Decided to do this, rather than using an ObjectMapper, to prevent yet one
 * more dependency.
 * 
 * @author Michael Irwin
 */
public class JsonAlertSummaryUnmarshaller 
    implements Unmarshaller<List<AlertSummary>> {

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMimeType() {
    return "application/json";
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public List<AlertSummary> convertData(String data) {
    try {
      ObjectMapper mapper = new ObjectMapper();
  	  List<AlertSummary> summaries = new ArrayList<AlertSummary>();
      JSONObject jsonObject = new JSONObject(data);
      JSONArray alerts = jsonObject.getJSONArray("alerts");
      for (int i = 0; i < alerts.length(); i++) {
        summaries.add(mapper.readValue(alerts.get(i).toString(), 
            AlertSummary.class));
      }
  	  return summaries;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
	}
	
}
