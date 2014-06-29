package edu.vt.alerts.android.library.util.marshal;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.vt.alerts.android.library.domain.cap.CapAlert;

/**
 * An unmarshaller that converts incoming JSON data into an Alert message.
 * 
 * @author Michael Irwin
 */
public class JsonAlertUnmarshaller implements Unmarshaller<CapAlert> {

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
  public CapAlert convertData(String data) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      CapAlert alert = mapper.readValue(data, CapAlert.class);
      return alert;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
	}
	
}
