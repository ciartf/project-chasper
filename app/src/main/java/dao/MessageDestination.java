package dao;

import   requestresponse.ExcludeFromGson;
import com.google.gson.annotations.SerializedName;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "MESSAGE_DESTINATION".
 */
public class MessageDestination {

    /** Not-null value. */
     @SerializedName("uuid_destination")
    private String uuid_destination;
     @SerializedName("destonation")
    private String destination;
     @SerializedName("buddy_id")
    private String buddy_id;

    public MessageDestination() {
    }

    public MessageDestination(String uuid_destination) {
        this.uuid_destination = uuid_destination;
    }

    public MessageDestination(String uuid_destination, String destination, String buddy_id) {
        this.uuid_destination = uuid_destination;
        this.destination = destination;
        this.buddy_id = buddy_id;
    }

    /** Not-null value. */
    public String getUuid_destination() {
        return uuid_destination;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUuid_destination(String uuid_destination) {
        this.uuid_destination = uuid_destination;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getBuddy_id() {
        return buddy_id;
    }

    public void setBuddy_id(String buddy_id) {
        this.buddy_id = buddy_id;
    }

}