package cetchoriyans.screentime;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by joseph on 18/2/16.
 */
public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;
    private Intent packageInfo;
    PendingIntent pintent;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // do whatever you need to do here
            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if(pintent!=null)
            {
                alarm.cancel(pintent);
                Log.i("location", "pintent cancelled");
            }
            else
                Log.i("location","pintent null");
            Log.i("statechange", "Screen State Off");
            SharedPrefMan sharedPrefMan=new SharedPrefMan(context);
            sharedPrefMan.addNewSessionEnd(System.currentTimeMillis()/1000);
            sharedPrefMan.printSessions();
          if(!isMyServiceRunning(SendData.class,context)) {
              context.startService(new Intent(context, SendData.class));
          }
            else
              Log.i("location","service already running");

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // and do whatever you need to do here
            Log.i("statechange", "Screen State ON");
            SharedPrefMan sharedPrefMan=new SharedPrefMan(context);
            sharedPrefMan.addNewSessionBegin(System.currentTimeMillis()/1000);
            Calendar cal = Calendar.getInstance();
            Intent intent2 = new Intent(context, PackageInfoService.class);
            context.startService(intent2);
            pintent = PendingIntent.getService(context, 0, intent2, 0);
            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarm.setRepeating(AlarmManager.RTC, cal.getTimeInMillis(), 30 * 1000, pintent);

        }
    } private boolean isMyServiceRunning(Class<?> serviceClass,Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}