package requestresponse;

import com.google.gson.annotations.SerializedName;

/**
 * Created by danu on 6/1/17.
 */

public class TokenResponse {
    @SerializedName("node")
    private Node node;
    @SerializedName("token")
    private String token;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
