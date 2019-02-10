package com.example.weighsafe;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class YourService extends Service {

    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";
    AlarmManager manager;
    String str1,str2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        str1 = intent.getStringExtra("value1");
        //str2 = intent.getStringExtra("value");
        try {
           new FetchThingspeakTask().execute();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
        // do your jobs here

        startForeground();

        return super.onStartCommand(intent, flags, startId);
    }

    private void startForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build());
    }

    class FetchThingspeakTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            //t2.setText("Fetching Data from Server.Please Wait...");
        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL("https://api.thingspeak.com/channels/698454/feeds.json?api_key=Y0XD4J2RIG0QPI47&results=2");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);


                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                Toast.makeText(YourService.this, "There was an error", Toast.LENGTH_SHORT).show();
                return;
            }
            try {

                 // str1.setText(response);
                JSONObject channel = (JSONObject) new JSONObject(response);
                // t1.setText(""+channel);
                JSONArray feed = channel.getJSONArray("feeds");
                JSONObject first = feed.getJSONObject(0);
                // t1.setText(""+first);
                String v1 = first.getString("field1");
                Log.v("Message",v1);
                //Log.v("Value",str2);
                Float v= Float.parseFloat(v1);
                if(str1=="0"){
                    str2="0";
                }
                else {
                    if (v<800 || v>830) {
                        manager = (AlarmManager) getSystemService(ALARM_SERVICE);


                        Intent intent = new Intent(YourService.this, BroadCast.class);
                        PendingIntent broadcast = PendingIntent.getBroadcast(YourService.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        long time = System.currentTimeMillis();
                        //manager.set(AlarmManager.RTC_WAKEUP,cc.getTimeInMillis(),broadcast);
                        notificationCall(time + 10 * 1000, broadcast);


                        //setAlarm();


                    }
                }
                //    if(v1>=90)
                //  t1.setText("" + v1);

                //  else
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressLint("NewApi")
    private void notificationCall(long time, PendingIntent pIntent) {
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            manager.setExact(AlarmManager.RTC_WAKEUP, time, pIntent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, time, pIntent);
        }
    }
}
