package requestresponse;

import com.google.gson.annotations.SerializedName;

/**
 * Created by danu on 6/1/17.
 */

public class SubMessageRequestBean {
    @SerializedName("sensor")
    private String sensor;
    @SerializedName("data")
    private String data;

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
