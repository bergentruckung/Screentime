package cetchoriyans.screentime;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by joseph on 18/2/16.
 */
public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // do whatever you need to do here
            Log.i("statechange", "Screen State Off");
            SharedPrefMan sharedPrefMan=new SharedPrefMan(context);
            sharedPrefMan.addNewSessionEnd(System.currentTimeMillis());
            sharedPrefMan.printSessions();
          if(!isMyServiceRunning(SendData.class,context)) {
              context.startService(new Intent(context, SendData.class));
          }
            else
              Log.i("location","service already running");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // and do whatever you need to do here
            Log.i("statechange","Screen State ON");
            SharedPrefMan sharedPrefMan=new SharedPrefMan(context);
            sharedPrefMan.addNewSessionBegin(System.currentTimeMillis());

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