package cetchoriyans.screentime;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;

/**
 * Created by joseph on 19/2/16.
 */
public class PackageInfoService extends Service {
    int mStartmode;
    String lastPackageName;
    int lastEntryId;
    SharedPrefMan sharedPrefMan;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
sharedPrefMan=new SharedPrefMan(getBaseContext());
            Log.i("location", "started the poller");

                try {
                    ActivityManager mActivityManager=(ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
                    String packageName;
                    if(Build.VERSION.SDK_INT > 20){
                        packageName = mActivityManager.getRunningAppProcesses().get(0).processName;
                    }
                    else{
                        packageName = mActivityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
                    }

                        Log.i("package",packageName);
                        sharedPrefMan.addProcess(packageName,System.currentTimeMillis()/1000);
                        Log.i("location","poller stopped");
                this.stopSelf();


                }catch (Exception e)
                {
                    Log.e("Error", String.format(e.getMessage(), new Object[0]));
                }


        } catch (Exception e) {

        }
        return mStartmode;
    }

    private ActivityManager.RunningTaskInfo getForegroundTask(Context context) {
        ActivityManager.RunningTaskInfo foregroundTaskInfo = null;
        try {
            return (ActivityManager.RunningTaskInfo) ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0);
        } catch (Exception ex) {
            Log.i("Aptrax", String.format("Unable to get current foreground task. Error message: %s", new Object[]{ex.getMessage()}));
            return foregroundTaskInfo;
        } catch (Throwable th) {
            return foregroundTaskInfo;
        }
    }
    private String getLauncherPackageName() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        return getPackageManager().resolveActivity(intent, AccessibilityNodeInfoCompat.ACTION_CUT).activityInfo.packageName;
    }
}