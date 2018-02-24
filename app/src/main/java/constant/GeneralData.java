package constant;

import java.util.HashMap;
import java.util.List;

/**
 * Created by danu on 6/1/17.
 */

public class GeneralData {
    private static String token;
    private static String uuidUser;
    private static String userId;
    private static String password;
    private static HashMap<String, Long> buddyEndpointMap;
    private static Boolean presenceHackFlag;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        GeneralData.token = token;
    }

    public static String getUuidUser() {
        return uuidUser;
    }

    public static void setUuidUser(String uuidUser) {
        GeneralData.uuidUser = uuidUser;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        GeneralData.userId = userId;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        GeneralData.password = password;
    }

    public static HashMap<String, Long> getBuddyEndpointMap() {
        return buddyEndpointMap;
    }

    public static void setBuddyEndpointMap(HashMap<String, Long> buddyEndpointMap) {
        GeneralData.buddyEndpointMap = buddyEndpointMap;
    }

    public static Boolean getPresenceHackFlag() {
        return presenceHackFlag;
    }

    public static void setPresenceHackFlag(Boolean presenceHackFlag) {
        GeneralData.presenceHackFlag = presenceHackFlag;
    }
}
