package requestresponse;

import com.google.gson.annotations.SerializedName;

/**
 * Created by danu on 6/1/17.
 */

public class SubMessageResponseBean {
    @SerializedName("id")
    private String id;
    @SerializedName("node")
    private String node;
    @SerializedName("url")
    private String url;
    @SerializedName("nodeurl")
    private String nodeurl;
    @SerializedName("sensorurl")
    private String sensorurl;
    @SerializedName("sensorlabel")
    private String sensorlabel;
    @SerializedName("sensor")
    private String sensor;
    @SerializedName("data")
    private String data;
    @SerializedName("timestamp")
    private String timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNodeurl() {
        return nodeurl;
    }

    public void setNodeurl(String nodeurl) {
        this.nodeurl = nodeurl;
    }

    public String getSensorurl() {
        return sensorurl;
    }

    public void setSensorurl(String sensorurl) {
        this.sensorurl = sensorurl;
    }

    public String getSensorlabel() {
        return sensorlabel;
    }

    public void setSensorlabel(String sensorlabel) {
        this.sensorlabel = sensorlabel;
    }

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
