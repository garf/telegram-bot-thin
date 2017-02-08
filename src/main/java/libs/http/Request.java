package libs.http;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Request {
    public String getContent(String endpoint) throws IOException {
        URL url = new URL(endpoint);
        URLConnection urlConnection = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        return this.getBufferContent(reader);
    }

    public String postContent(String endpoint, String data) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead

        HttpPost request = new HttpPost(endpoint);
        StringEntity params = new StringEntity(data);
        request.addHeader("content-type", "application/json");
        request.setEntity(params);
        HttpResponse response = httpClient.execute(request);

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        return this.getBufferContent(reader);
    }

    public String getBufferContent(BufferedReader reader) throws IOException {
        StringBuilder everything = new StringBuilder();
        String line;

        while( (line = reader.readLine()) != null) {
            everything.append(line);
        }

        reader.close();

        return everything.toString();
    }
}
