package com.mobile.agri10x.Connection;

import android.content.Context;
import android.content.Intent;

import com.mobile.agri10x.LoginActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

public class GETRequest {
    static Context context;
    public static String fetchUserData(String requestUrl,Context context) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        GETRequest.context = context;
        URL url = new URL(requestUrl);
        String jsonResponse;
        if(url!=null) {
            jsonResponse = makeHttpRequest(url);
        }
        else
        {
            throw new MalformedURLException("Wrong URL :/ ");
        }
        return jsonResponse;
    }


    private static String makeHttpRequest(URL url) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        String jsonResponse = "";
        HttpsURLConnection urlConnection = SSLCertificateManagment.getConnection(url);
        InputStream inputStream;
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        urlConnection.connect();
        if (urlConnection.getResponseCode() == 200) {
            inputStream = urlConnection.getInputStream();
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
        if(jsonResponse.equals("Session Expired. Login Again") || jsonResponse.equals("null")){
//            Toast.makeText(context,"Session Expired. Login Again",Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra("session expired","true");
            context.startActivity(i);

            return null;
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
