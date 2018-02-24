package requestresponse;

import com.google.gson.annotations.SerializedName;

/**
 * Created by danu on 6/1/17.
 */

public class TokenRequest {
    @SerializedName("user")
    private String user;
    @SerializedName("label")
    private String label;
    @SerializedName("secretkey")
    private String secretkey;

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

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }
}
