package constant;

import java.util.List;

/**
 * Created by danu on 5/30/17.
 */

public class GlobalData {
    public static String db = "db";

    public static String url = "http://agrihub.tujuhlangit.id:8080/";
    public static String subs = "subscriptions/";
    public static String token = "node-auth/";
    public static String defaultLoginLabel = "FILKOM_1";
    public static String[] sensorList = {"TEMP","RADIANCE","HUMIDITY"};
//    public static String buddyEndpoint = "dtn://relay2.dtn/chat";

    public static String mqttBroker = "tcp://192.168.1.134:1883";
    public static String mqttClientId = "ExampleAndroidClient";
    public static String mqttSubscriptionTopic = "/test";
    public static String mqttPublishTopic = "/test";

    public static String mqttUser = "mqtt";

    public static String wifiSSID = "MDC-Backup1";
    public static String wifiPass = "AdInsMDC";
}
