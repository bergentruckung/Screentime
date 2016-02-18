package cetchoriyans.screentime;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by joseph on 18/2/16.
 */
public class SharedPrefMan {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public SharedPrefMan(Context context)
    {
        sharedPreferences=context.getSharedPreferences("screentime",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
    public void addNewSessionBegin(long start)
    {
        try {
            JSONArray jsonArray=new JSONArray(sharedPreferences.getString("sessions","[]"));
            JSONObject session;
            try {
                session = jsonArray.getJSONObject(jsonArray.length() - 1);
                if(session.has("start") && !session.has("stop")) {
                    Log.i("Session","Open object found");
                    return;
                }
            }catch (JSONException e)
            {
                Log.i("session","Start Exception");
            }

            session=new JSONObject();
            session.put("screen",0);
            session.put("start",start);
            jsonArray.put(session);
            editor.putString("sessions",jsonArray.toString());
            editor.commit();
        } catch (JSONException e) {
        Log.i("session","Start Exception");
        }
    }
    public void addNewSessionEnd(long end)
    {
        try {
            JSONArray jsonArray=new JSONArray(sharedPreferences.getString("sessions","[]"));
            JSONObject session=jsonArray.getJSONObject(jsonArray.length()-1);
            if(session.has("stop")) {
                Log.i("Session","Closed object found");
                return;
            }
            session.put("stop",end);
            jsonArray.put(jsonArray.length() - 1, session);
            editor.putString("sessions", jsonArray.toString());
            editor.commit();
        } catch (JSONException e) {
            Log.i("Session","session end exception");

        }
    }
    public  void printSessions()
    {
        Log.i("sessions",sharedPreferences.getString("sessions","nil"));
    }
}
