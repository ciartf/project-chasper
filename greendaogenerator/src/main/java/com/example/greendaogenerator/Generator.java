package com.example.greendaogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class Generator{
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(3, "dao");
        general(schema);


        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

    private static void general(Schema schema) {
        //SENSOR_MESSAGE
        Entity sensorMessage = schema.addEntity("SensorMessage").addImport("requestresponse.ExcludeFromGson").addImport("com.google.gson.annotations.SerializedName");
        sensorMessage.setTableName("SENSOR_MESSAGE");
        sensorMessage.addStringProperty("uuid_sensor_message").notNull().primaryKey().codeBeforeField("@SerializedName(\"uuid_sensor_message\")");
        sensorMessage.addStringProperty("message").codeBeforeField("@SerializedName(\"message\")");
        sensorMessage.addStringProperty("sensor_type").codeBeforeField("@SerializedName(\"sensor_type\")");
        sensorMessage.addStringProperty("source").codeBeforeField("@SerializedName(\"source\")");
        sensorMessage.addStringProperty("is_sent").codeBeforeField("@SerializedName(\"is_sent\")");

        //USER
        Entity user = schema.addEntity("User").addImport("  requestresponse.ExcludeFromGson").addImport("com.google.gson.annotations.SerializedName");
            user.setTableName("USER");
        user.addStringProperty("uuid_user").notNull().primaryKey().codeBeforeField("@SerializedName(\"uuid_user\")");
        user.addStringProperty("user_id").codeBeforeField("@SerializedName(\"user_id\")");
        user.addStringProperty("password").codeBeforeField("@SerializedName(\"password\")");
        user.addStringProperty("token").codeBeforeField("@SerializedName(\"token\")");

        //MESSAGE_DESTINATION
        Entity messageDestination = schema.addEntity("MessageDestination").addImport("  requestresponse.ExcludeFromGson").addImport("com.google.gson.annotations.SerializedName");
        messageDestination.setTableName("MESSAGE_DESTINATION");
        messageDestination.addStringProperty("uuid_destination").notNull().primaryKey().codeBeforeField("@SerializedName(\"uuid_destination\")");
        messageDestination.addStringProperty("destination").codeBeforeField("@SerializedName(\"destonation\")");
        messageDestination.addStringProperty("buddy_id").codeBeforeField("@SerializedName(\"buddy_id\")");


    }
}
