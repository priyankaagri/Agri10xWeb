package com.mobile.agri10x.Model;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mobile.agri10x.Connection.CheckSessionGETAPI;
import com.mobile.agri10x.LoginActivity;
import com.mobile.agri10x.Notification;
import com.mobile.agri10x.R;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;



public class MyFirebaseMessagingService extends FirebaseMessagingService {

    RemoteMessage message ;
    PendingIntent pendingIntent;
    private static final String TAG = "MyFirebaseMsgService";
    public static String newToken;
    static String jsonNotifiData;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData() != null) {
            message = remoteMessage;
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(R.string.default_notification_channel_id);
            Map<String ,String > jsondata = remoteMessage.getData();
            jsonNotifiData = jsondata.get("datab");
//            sendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
            if(true) {
                boolean foreground;
                try {
                    foreground = new ForegroundCheckTask().execute(this).get();
                    if(foreground){
                        if(new isSessionAlive().execute(Main.getIp()+"/UserInfo").get()){
                            Intent intent = new Intent(this, Notification.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("notification data",jsonNotifiData);
                            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                                    PendingIntent.FLAG_ONE_SHOT);
                        }
                        else{
                            Intent intent = new Intent(this, Notification.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("notification data",jsonNotifiData);
                                pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                                    PendingIntent.FLAG_ONE_SHOT);
                        }
                        sendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());

                    }else { //kill
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("notification data",jsonNotifiData);
                        pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                                PendingIntent.FLAG_ONE_SHOT);
                        sendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message NotificationActivity Body: " + remoteMessage.getNotification().getBody());
        }
    }

    class isSessionAlive extends AsyncTask<String ,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            String str;
            try {
                str =  CheckSessionGETAPI.fetchUserData(strings[0]);
            } catch (Exception e){
                return false;
            }
            return (str == null || !str.equals("Session Expired. Login Again")) && !str.equals("null");
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        newToken = token;
    }



    class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

    // Use like this:


    private void sendNotification(String messageBody,String title) {

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_agri10x_leaf)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "NotificationActivity Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}