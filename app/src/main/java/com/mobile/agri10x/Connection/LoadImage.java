package com.mobile.agri10x.Connection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

public class LoadImage {
    static Context context;
    public static Bitmap fetchUserData(String requestUrl, Context context) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        GETRequest.context = context;
        URL url = new URL(requestUrl);
        Bitmap BitmapResponse;
        if(url!=null) {
            BitmapResponse = makeHttpRequest(url);
        }
        else
        {
            throw new MalformedURLException("Wrong URL please check again :/ ");
        }
        return BitmapResponse;
    }


    private static Bitmap makeHttpRequest(URL url) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        Bitmap bitmap;
        HttpsURLConnection urlConnection = SSLCertificateManagment.getConnection(url);
        InputStream inputStream;
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        urlConnection.connect();
        if (urlConnection.getResponseCode() == 200) {
            inputStream = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } else {
            return null;
        }
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        return bitmap;
    }

}
