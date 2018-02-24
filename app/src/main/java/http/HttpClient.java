package http;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by danu on 5/30/17.
 */

public class HttpClient {

    private OkHttpClient client;
    private static MediaType jsonType;

    public HttpClient(){
        client = new OkHttpClient();
        jsonType = MediaType.parse("application/json; charset=utf-8");

    }

    public String postMessage(String jsonBody, String url)throws IOException {
        RequestBody body = RequestBody.create(jsonType, jsonBody);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if(response.code() == 200){
                return response.body().string();
            }else{
                return null;
            }
        }
    }

    public String postMessage(String jsonBody, String url, String authHeader)throws IOException {
        RequestBody body = RequestBody.create(jsonType, jsonBody);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", authHeader)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if(response.code() == 200){
                return response.body().string();
            }else if(response.code() == 201){
                return response.body().string();
            }else if(response.code() == 401) {
                return "retryToken";
            }else if(response.code() == 400){
                return "reviewMessage";
            }else{
                return null;
            }
        }
    }
}
