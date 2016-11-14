package fadhel.iac.tn.eventtracker.receivers;




import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import fadhel.iac.tn.eventtracker.data.DatabaseHandler;

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        String type = intent.getStringExtra("notification_type");
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        Log.d("Notification id", id + "");
        notificationManager.notify(id, notification);
        DatabaseHandler dbh = new DatabaseHandler(context);
        dbh.setNotified(type,id+"");
    }
}
