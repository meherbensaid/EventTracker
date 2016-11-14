package fadhel.iac.tn.eventtracker.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fadhel.iac.tn.eventtracker.receivers.NotificationPublisher;
import fadhel.iac.tn.eventtracker.R;
import fadhel.iac.tn.eventtracker.activities.FavoritesActivity;

/**
 * Created by Fadhel on 15/04/2016.
 */
public class Utils {


    public static Date getDate(String date , String format1) {
        SimpleDateFormat df = new SimpleDateFormat(format1);
        Date d = null;
        try {
            d = df.parse(date);
        }catch(ParseException e) {e.printStackTrace();}
        return d;
    }

    public static String transformDate(String date , String format1 , String format2) {
        SimpleDateFormat df = new SimpleDateFormat(format1);
        Date d = null;
        try {
            d = df.parse(date);
        }catch(ParseException e) {e.printStackTrace();}
        df = new SimpleDateFormat(format2);
        return df.format(d);
    }
    public static String updateDate() {
        Date  d1 = null;
        Date  d2 = null;
        Calendar cal = Calendar.getInstance();
        d1 = cal.getTime();
        cal.add(Calendar.WEEK_OF_MONTH,2);
        d2 = cal.getTime();
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd00");
        return sd.format(d1) + "-" + sd.format(d2);
    }

    public static int scheduleNotification(Notification notification, long delay,Context context,String notif_type) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        SharedPreferences sp = context.getSharedPreferences("myPrefs", context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        int notification_id = sp.getInt("notification_id",1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notification_id);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        notificationIntent.putExtra("notification_type",notif_type);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notification_id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, futureInMillis, pendingIntent);
        }
        else alarmManager.set(AlarmManager.ELAPSED_REALTIME, futureInMillis, pendingIntent);
        notification_id = (notification_id + 1) % 2147483647;
        if (notification_id==0) notification_id++;
        e.putInt("notification_id",notification_id);
        e.commit();
        return  (notification_id-1);
    }

    public static Notification getNotification(String EventTitle,String content,Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        Intent resultIntent = new Intent(context,FavoritesActivity.class);
        builder.setContentTitle(EventTitle);
        Log.d("EVENT TITLE", EventTitle);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.logo);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_ONE_SHOT //can only be used once
                );
        // start the activity when the user clicks the notification text
        builder.setContentIntent(resultPendingIntent);
        return builder.build();
    }
    // -1 : Non
    // 0 : date event = date aujourdhui
    // 1 : date event = date aujourdhui - 1
    public static int launchNotification(Date dueDate ){
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
        String dueDateString = sf.format(dueDate);
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        String todayDate = sf.format(today);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date  tomorrow = cal.getTime();
        String tomorrowDate = sf.format(tomorrow);
        if(todayDate.equals(dueDateString)) return 0;
        else if(tomorrowDate.equals(dueDateString)) return 1;
        else return -1;
    }

}
