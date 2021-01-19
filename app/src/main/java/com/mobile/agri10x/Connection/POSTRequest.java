package com.mobile.agri10x.Connection;

import android.content.Context;
import android.content.Intent;

import com.mobile.agri10x.LoginActivity;

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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

import static com.mobile.agri10x.Connection.SSLCertificateManagment.DO_NOT_VERIFY;


public class POSTRequest {
    static Context context;
    public static String fetchUserData(String requestUrl, String jsonData,Context context) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        POSTRequest.context = context;
        URL url = new URL(requestUrl);
        String jsonResponse;
        if(url!=null) {
            jsonResponse = makeHttpRequest(url,jsonData);
        }
        else
        {
            throw new MalformedURLException("Wrong URL :/ ");
        }
        return jsonResponse;
    }


    private static String makeHttpRequest(URL url, String jsonData) throws IOException, NoSuchAlgorithmException, KeyManagementException {

        if (url.getProtocol().toLowerCase().equals("https")) {
            HttpsURLConnection urlConnection ;
            SSLCertificateManagment.trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            urlConnection = https;
            String jsonResponse = "";
            InputStream inputStream;
            urlConnection.setRequestMethod("POST");

            urlConnection.setRequestProperty("x-auth-token","123456");

            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setDoOutput(true);
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
            if (jsonResponse.equals("Session Expired. Login Again") || jsonResponse.equals("null")) {
//            Toast.makeText(context,"Session Expired. Login Again",Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("session expired","true");
                context.startActivity(i);

                return null;
            }
            return jsonResponse;
        }
        else{
            String jsonResponse = "";

            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection;
            InputStream inputStream;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");

            urlConnection.setRequestProperty("x-auth-token","123456");

            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
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



    private static String makeMultiHttpRequest(URL url, String jsonData) throws IOException, NoSuchAlgorithmException, KeyManagementException {

        if (url.getProtocol().toLowerCase().equals("https")) {
            HttpsURLConnection urlConnection;
            SSLCertificateManagment.trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            urlConnection = https;
            String jsonResponse = "";
            InputStream inputStream;
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setDoOutput(true);
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
            if (jsonResponse.equals("Session Expired. Login Again") || jsonResponse.equals("null")) {
//            Toast.makeText(context,"Session Expired. Login Again",Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("session expired", "true");
                context.startActivity(i);

                return null;
            }
            return jsonResponse;
        } else {
            String jsonResponse = "";

            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection;
            InputStream inputStream;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
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
    }


    /*public static JSONObject postFile(String url,String filePath,int id){
        String result="";
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        File file = new File(filePath);
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(file, "image/jpeg");
        StringBody stringBody= null;
        JSONObject responseObject=null;
        try {
            stringBody = new StringBody(id+"");
            mpEntity.addPart("file", cbFile);
            mpEntity.addPart("id",stringBody);
            httpPost.setEntity(mpEntity);
            System.out.println("executing request " + httpPost.getRequestLine());
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            result=resEntity.toString();
            responseObject=new JSONObject(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseObject;
    }*/

}


/*
            testing d;
			FileInputStream f = new FileInputStream("file.txt");
			ObjectInputStream oos = new ObjectInputStream(f);
			try{
            			while((d = (testing)oos.readObject())!=null)
				System.out.println("name = "+ d.name);
			}catch(Exception e){
				System.out.println("EOF");
			}
*/