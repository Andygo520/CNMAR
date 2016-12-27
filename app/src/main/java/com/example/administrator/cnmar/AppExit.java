package com.example.administrator.cnmar;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import java.util.LinkedList;
import java.util.List;
/**
 * 完全退出应用程序类（利用单例模式管理Activity,在每个activity的onCreate方法中调用addActivity方法，在应用程序退出时调用exit方法，就可以完全退出）
 * Created by Administrator on 2016/11/15.
 */
public class AppExit extends Application {
    private static AppExit instance = null;
    private List<Activity> activityList = new LinkedList<Activity>();

    private AppExit() {
    }

    public synchronized static AppExit getInstance() {
        if (instance == null) {
            instance = new AppExit();
        }

        return instance;
    }

    public void addActivity(Activity pActivity) {
        if (!activityList.contains(pActivity))
            activityList.add(pActivity);
    }

    public void exit() {
        try {
            for (Activity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            Log.d("TAG", String.valueOf(e));
        } finally {
            System.exit(0);
        }
    }
}

