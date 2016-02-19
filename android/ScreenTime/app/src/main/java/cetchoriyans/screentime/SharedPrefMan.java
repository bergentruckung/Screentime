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
            session.put("type","screen");
            session.put("start",start+"");
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
            session.put("stop",end+"");
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
    public  String getSessions()
    {
      return sharedPreferences.getString("sessions", "[]");
    }
    public  String getProcesses()
    {
        return sharedPreferences.getString("processes", "[]");
    }
    public void addHash(String hash)
    {
        editor.putString("auth",hash);
        editor.commit();
    }
    public String checkHash()
    {
        String hash=sharedPreferences.getString("auth","");
        return hash;

    }
    public void addProcess(String process,long time)
    {
        if(process==null)
            return;
        JSONArray jsonArray=null;
        try {
             jsonArray=new JSONArray(sharedPreferences.getString("processes","[]"));
            JSONObject session=jsonArray.getJSONObject(jsonArray.length()-1);
            if(session.getString("type").equals(process)) {
                session.put("stop",time+"");
                jsonArray.put(jsonArray.length() - 1, session);
                editor.putString("processes", jsonArray.toString());
                editor.commit();
                return;
            }
        }
        catch (Exception e)
        {

        }

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("type", process);
            jsonObject.put("start", time+"");
            jsonObject.put("stop",time+30+"");
            jsonArray.put(jsonObject);
        }catch(Exception e)
        {

        }
        editor.putString("processes",jsonArray.toString());
        editor.commit();
        return;

    }
    public void clearSessions()
    {
        editor.putString("sessions","[]");
        editor.putString("processes","[]");
        editor.commit();
    }
}
