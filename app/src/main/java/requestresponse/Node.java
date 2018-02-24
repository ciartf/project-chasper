package requestresponse;

import com.google.gson.annotations.SerializedName;

/**
 * Created by danu on 6/1/17.
 */

public class Node {
    @SerializedName("id")
    private String id;
    @SerializedName("user")
    private String user;
    @SerializedName("label")
    private String label;
    @SerializedName("url")
    private String url;
    @SerializedName("sensor_count")
    private String sensor_count;
    @SerializedName("sensors_list")
    private String sensors_list;
    @SerializedName("subscriptions_list")
    private String subscriptions_list;
    @SerializedName("secretkey")
    private String secretkey;
    @SerializedName("is_public")
    private String is_public;
    @SerializedName("subsperday")
    private String subsperday;
    @SerializedName("subsperdayremain")
    private String subsperdayremain;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSensor_count() {
        return sensor_count;
    }

    public void setSensor_count(String sensor_count) {
        this.sensor_count = sensor_count;
    }

    public String getSensors_list() {
        return sensors_list;
    }

    public void setSensors_list(String sensors_list) {
        this.sensors_list = sensors_list;
    }

    public String getSubscriptions_list() {
        return subscriptions_list;
    }

    public void setSubscriptions_list(String subscriptions_list) {
        this.subscriptions_list = subscriptions_list;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public String getIs_public() {
        return is_public;
    }

    public void setIs_public(String is_public) {
        this.is_public = is_public;
    }

    public String getSubsperday() {
        return subsperday;
    }

    public void setSubsperday(String subsperday) {
        this.subsperday = subsperday;
    }

    public String getSubsperdayremain() {
        return subsperdayremain;
    }

    public void setSubsperdayremain(String subsperdayremain) {
        this.subsperdayremain = subsperdayremain;
    }
}
