package fadhel.iac.tn.eventtracker.utils;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.CamcorderProfile;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fadhel.iac.tn.eventtracker.R;
import fadhel.iac.tn.eventtracker.receivers.NotificationPublisher;
import fadhel.iac.tn.eventtracker.data.DatabaseHandler;
import fadhel.iac.tn.eventtracker.model.Event;
import fadhel.iac.tn.eventtracker.model.FavouriteEvent;

/**
 * Created by Fadhel on 26/04/2016.
 */
public class EventUtils {

public static boolean addToFavourite(final Context context, final Event event) {


    Calendar cal = Calendar.getInstance();
    final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    final SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    // On teste si l'événement n'est pas déja passée
    if ((event.getDateStop().isEmpty() && event.getDateStart().compareTo(df1.format(cal.getTime())) < 0) ||
            (!event.getDateStop().isEmpty() && event.getDateStop().compareTo(df1.format(cal.getTime())) < 0)) {
        final AlertDialog.Builder alertDlialogTest = new AlertDialog.Builder(context);
        alertDlialogTest.setCancelable(true);
        alertDlialogTest.setTitle(context.getString(R.string.dialogcannottitle));
        alertDlialogTest.setMessage(context.getString(R.string.cannnotaddtofav));
        alertDlialogTest.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        alertDlialogTest.show();
        return false;
    }
    else {
// Si l'événement se déroule sur plusieurs jours, l'utilisateur doit choisir la date de la notification
        if (!event.getDateStop().isEmpty() && !event.getDateStop().equals(event.getDateStart())) {

            Log.d("ev", "many days");
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.prompt, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);
            alertDialogBuilder.setView(promptsView);
            alertDialogBuilder.setTitle(context.getString(R.string.set_notification));
            TextView tv = (TextView) promptsView.findViewById(R.id.prompt_message);
            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);
            userInput.setInputType(InputType.TYPE_NULL);
            String intervalStartDate = null;

            if (event.getDateStart().compareTo(df1.format(cal.getTime())) > -1)
                intervalStartDate = event.getDateStart();
            else intervalStartDate = df1.format(cal.getTime());

            tv.setText(context.getString(R.string.choose_date, Utils.transformDate(intervalStartDate, "yyyy-MM-dd", "dd/MM/yyyy"), Utils.transformDate(event.getDateStop(), "yyyy-MM-dd", "dd/MM/yyyy")));
            userInput.setText(Utils.transformDate(intervalStartDate, "yyyy-MM-dd", "dd/MM/yyyy"));


            final DatePickerDialog dp = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    userInput.setText(df.format(newDate.getTime()));
                }

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

            userInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userInput.setError(null);
                    dp.show();
                }
            });
            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });

            // create alert dialog
            final AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            final String finalIntervalStartDate = intervalStartDate;
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date pickerDate = Utils.getDate(userInput.getText().toString(), "dd/MM/yyyy");
                    Calendar cal = Calendar.getInstance();


                    if (df1.format(pickerDate).compareTo(finalIntervalStartDate) < 0 || df1.format(pickerDate).compareTo(event.getDateStop()) > 0) {
                        userInput.setError(context.getString(R.string.appropriate_date));
                    } else {
                        event.setDateReal(Utils.transformDate(userInput.getText().toString(), "dd/MM/yyyy", "yyyy-MM-dd"));
                        alertDialog.dismiss();
                        actionAdd(context, event);
                    }
                    Log.d("date_real", "=" + event.getDateReal());
                }
            });

        }
        // Si l'événement est sur un jour
        else actionAdd(context, event);
        return true;
    }
}

    private static void actionAdd(Context context, Event event) {
        DatabaseHandler db = new DatabaseHandler(context);
        Calendar cal = Calendar.getInstance();
        Event e = db.getFavouriteEvent(event.getId());
        Toast t = null;
        if (e == null) {
            Date datej = Utils.getDate(event.getDateReal(), "yyyy-MM-dd");
            Log.d("DateReal",":::"+datej);
            cal.setTime(datej);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.HOUR_OF_DAY,0);
            datej = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH,-1);
            Date datej1 = cal.getTime();
            long diff = datej.getTime() - System.currentTimeMillis();
            Log.d("diff1-jour j", diff + "");
            int id1=0;
            if(diff>-86400000)
            id1 = Utils.scheduleNotification(Utils.getNotification(event.getName(), context.getString(R.string.today_event,event.getName(),event.getTimeStart()), context),1000+diff, context, "1");

            diff = datej1.getTime() - System.currentTimeMillis();
            Log.d("diff2-jour j-1",diff+"");
            int id2=0;
            if(diff>-86400000)
            id2 = Utils.scheduleNotification(Utils.getNotification(event.getName(), context.getString(R.string.tomorrow_event,event.getName(),event.getTimeStart()), context), diff +1000, context, "2");

            Log.d("id2=",id2+"");
            FavouriteEvent ev =null;
            Log.d("id1",id1+"");
            Log.d("id2",id2+"");

            // S'il y aura une alarme pour le jour même et la veille
            if(id2==0 && id1==0)
                ev =   new FavouriteEvent(event,id1+"",id2+"","NO","NO");
                // Sinon s'il y aura une alarme uniquement pour le jour même
            else  ev =  new FavouriteEvent(event,id1+"",id2+"","NO","YES");

            Log.d("Favourite Added",ev.toString());
            db.addEvent(ev);
            t = Toast.makeText(context, context.getString(R.string.added_to_favourite), Toast.LENGTH_LONG);
        } else
            t = Toast.makeText(context, context.getString(R.string.already_favourite), Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        t.show();
    }

    public static void removeFromFavourite(Context context,Event ev) {
        DatabaseHandler db = new DatabaseHandler(context);
        FavouriteEvent fe = db.getFavouriteEvent(ev.getId());
        Log.d("fe",fe.toString());
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        PendingIntent pendingIntent1,pendingIntent2;
        pendingIntent1 = pendingIntent2 = null;
        if(fe.getNotif_1().equals("NO")) {
            pendingIntent1 = PendingIntent.getBroadcast(context, Integer.parseInt(fe.getId_notif_1()), notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            Log.d("Removed",fe.getId_notif_1());
        }
        if(fe.getNotif_2().equals("NO")) {
            pendingIntent2 = PendingIntent.getBroadcast(context, Integer.parseInt(fe.getId_notif_2()), notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            Log.d("Removed",fe.getId_notif_2());
        }
        if(pendingIntent1!=null)
            am.cancel(pendingIntent1);
        if(pendingIntent2!=null)
            am.cancel(pendingIntent2);
        db.deleteEvent("favourite",ev.getId());

    }
}
