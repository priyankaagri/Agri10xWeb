package com.mobile.agri10x.retrofit;

import com.mobile.agri10x.Connection.SSLCertificateManagment;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHandler {

    private static final String DEV_BASE_URL = "https://data.agri10x.com/m/";


    private static final long HTTP_TIMEOUT = TimeUnit.SECONDS.toMillis(60);
    private static AgriInvestor apiService;
static OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(DEV_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static AgriInvestor getApiService() {
        if (apiService == null) {
            httpClient.connectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
            httpClient.writeTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
            httpClient.readTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
            httpClient.retryOnConnectionFailure(true);

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
           Retrofit retrofit = builder.client(okHttpClient).build();//httpClient.build()
            apiService = retrofit.create(AgriInvestor.class);
            return apiService;
        }
        else {

            return apiService;
        }
    }
}
