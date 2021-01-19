package com.mobile.agri10x.Connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static android.util.Log.e;

public class HttpPOST {
    static  String jsonResponse ;
    public static String fetchUserData(String requestUrl,String jsonData) {
        URL url = createUrl(requestUrl);


        try {
            jsonResponse = makeHttpRequest(createUrl(requestUrl),jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(jsonResponse ==null){
            return null;
        }
        else
            e("json",jsonResponse);
        return jsonResponse;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url,String jsonData) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection;
        InputStream inputStream;
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        OutputStream os = urlConnection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        writer.write(jsonData);
        writer.flush();
        writer.close();
        os.close();
        urlConnection.connect();
        if (urlConnection.getResponseCode() == 200) {
            inputStream = urlConnection.getInputStream();//output stream for the server's socket
            jsonResponse = readFromStream(inputStream);
        } else {
            return null;
        }
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


}
