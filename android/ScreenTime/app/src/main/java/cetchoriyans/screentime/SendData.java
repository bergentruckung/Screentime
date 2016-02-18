package cetchoriyans.screentime;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Created by joseph on 18/2/16.
 */
public class SendData extends Service {
    int mStartMode;
    String hash;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("location","sendata start");
        new SendTask().execute();

        return mStartMode;
    }
    public class SendTask extends AsyncTask<Void, Void, Boolean> {


        private JSONObject jsonObject;
        private SharedPrefMan sharedPrefMan;
        SendTask() {
            sharedPrefMan=new SharedPrefMan(getBaseContext());
          jsonObject=new JSONObject();
            try {
                jsonObject.put("hash",sharedPrefMan.checkHash());
                jsonObject.put("session_data",new JSONArray(sharedPrefMan.getSessions()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String response;
            try {

                URL url = new URL("http://192.168.1.57:8080/api/device/session");
                URLConnection urlConn = url.openConnection();

                if (!(urlConn instanceof HttpURLConnection)) {
                    throw new IOException("URL is not an Http URL");
                }
                HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                httpConn.setReadTimeout(15000);
                httpConn.setConnectTimeout(15000);
                httpConn.setRequestMethod("POST");
                httpConn.setRequestProperty("Content-Type", "application/json");
                httpConn.setDoInput(true);
                httpConn.setDoOutput(true);
                httpConn.setRequestMethod("POST");

                OutputStream os = httpConn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonObject.toString());
                Log.i("webrequest",jsonObject.toString());
                writer.flush();
                writer.close();
                os.close();

                int resCode = httpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = httpConn.getInputStream();
                    response=LoginActivity.readIt(in,500);
                    Log.i("webresponse", response);
                    if (response.trim().equals("updated"))
                        return true;
                }

            }

            catch (MalformedURLException e) {
                e.printStackTrace();
            }

            catch (IOException e) {
                e.printStackTrace();
            }


            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                sharedPrefMan.clearSessions();
            }
            SendData.this.stopSelf();
        }

        @Override
        protected void onCancelled() {

        }
    }
}
