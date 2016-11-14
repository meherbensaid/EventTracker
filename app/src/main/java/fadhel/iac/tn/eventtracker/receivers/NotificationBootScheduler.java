package fadhel.iac.tn.eventtracker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fadhel.iac.tn.eventtracker.R;
import fadhel.iac.tn.eventtracker.data.DatabaseHandler;
import fadhel.iac.tn.eventtracker.model.Event;
import fadhel.iac.tn.eventtracker.model.FavouriteEvent;
import fadhel.iac.tn.eventtracker.utils.Utils;

/**
 * Created by Fadhel on 23/04/2016.
 */
public class NotificationBootScheduler extends BroadcastReceiver {

    DatabaseHandler dbHandler;
    @Override
    public void onReceive(Context context, Intent intent) {

        dbHandler = new DatabaseHandler(context);
        List<Event> favourites = dbHandler.getAllFavouriteEvents();
        Calendar cal = Calendar.getInstance();
        Log.d("D","Inside Notif");
        for(Event ev : favourites) {
            FavouriteEvent fev = (FavouriteEvent) ev;
            long diff;
            Date datej = Utils.getDate(fev.getDateStart(), "yyyy-MM-dd");
            Date datej1 = null;
             Log.d("D",ev.toString());
            // La notif du jour j n'a pas encore été déclenchée
            if(fev.getNotif_1().equals("NO")) {
                Log.d("MAJ","Mise à jour notif jour j de "+fev.getName());
                cal.setTime(datej);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                datej = cal.getTime();
                int id1 = 0;
                 diff = datej.getTime() - System.currentTimeMillis();
                if(diff>-86400000)
                id1 = Utils.scheduleNotification(Utils.getNotification(fev.getName(), context.getApplicationContext().getString(R.string.today_event) + fev.getName() + " "+ context.getApplicationContext().getString(R.string.a)+" " + fev.getTimeStart(), context), diff+1000, context, "1");
                Log.d("MAJ","nouveau id notif "+id1);
                dbHandler.updateNotifId("1",fev.getId(),id1+"");
            }
            // La notif de la veille n'a pas été déclenchée
            if(fev.getNotif_2().equals("NO")) {
                Log.d("MAJ","Mise à jour notif jour j-1 de "+fev.getName());
                cal.add(Calendar.DAY_OF_MONTH,-1);
                 datej1 = cal.getTime();
                diff = datej1.getTime() - System.currentTimeMillis();
                int id2 = 0;
                if(diff>-86400000)
                id2 = Utils.scheduleNotification(Utils.getNotification(fev.getName(), context.getApplicationContext().getString(R.string.tomorrow_event)+" " + fev.getName() + " "+ context.getApplicationContext().getString(R.string.a)+" " + fev.getTimeStart(), context), diff+1000 , context, "2");
                Log.d("MAJ","nouveau id notif "+id2);
                dbHandler.updateNotifId("2",fev.getId(),id2+"");
            }
            }

        }

}
