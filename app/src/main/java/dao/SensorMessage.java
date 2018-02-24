package dao;

import com.google.gson.annotations.SerializedName;
import requestresponse.ExcludeFromGson;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "SENSOR_MESSAGE".
 */
public class SensorMessage {

    /** Not-null value. */
     @SerializedName("uuid_sensor_message")
    private String uuid_sensor_message;
     @SerializedName("message")
    private String message;
     @SerializedName("sensor_type")
    private String sensor_type;
     @SerializedName("source")
    private String source;
     @SerializedName("is_sent")
    private String is_sent;

    public SensorMessage() {
    }

    public SensorMessage(String uuid_sensor_message) {
        this.uuid_sensor_message = uuid_sensor_message;
    }

    public SensorMessage(String uuid_sensor_message, String message, String sensor_type, String source, String is_sent) {
        this.uuid_sensor_message = uuid_sensor_message;
        this.message = message;
        this.sensor_type = sensor_type;
        this.source = source;
        this.is_sent = is_sent;
    }

    /** Not-null value. */
    public String getUuid_sensor_message() {
        return uuid_sensor_message;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUuid_sensor_message(String uuid_sensor_message) {
        this.uuid_sensor_message = uuid_sensor_message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSensor_type() {
        return sensor_type;
    }

    public void setSensor_type(String sensor_type) {
        this.sensor_type = sensor_type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIs_sent() {
        return is_sent;
    }

    public void setIs_sent(String is_sent) {
        this.is_sent = is_sent;
    }

}
