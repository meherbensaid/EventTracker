package fadhel.iac.tn.eventtracker.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import fadhel.iac.tn.eventtracker.R;
import fadhel.iac.tn.eventtracker.activities.FavoritesActivity;
import fadhel.iac.tn.eventtracker.activities.ResultActivity;
import fadhel.iac.tn.eventtracker.adapters.CustomEventAdapter;
import fadhel.iac.tn.eventtracker.data.DatabaseHandler;
import fadhel.iac.tn.eventtracker.model.Event;

/**
 * Created by Fadhel on 08/06/2016.
 */
public class EventsDBLoader extends AsyncTask<String,Void,Void> {
    ProgressDialog progress;
    Context context;

    public EventsDBLoader(Context context) { this.context = context;}

    @Override
    protected void onPreExecute() {
        Log.d("display", "progress");
        progress = ProgressDialog.show(
                context, null, context.getString(R.string.loading));
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(String... params) {
           ResultActivity activity = (ResultActivity) context;
            DatabaseHandler db = new DatabaseHandler(activity);
            List<Event> all = activity.getAllitems();
            List<Event> sport = activity.getSportItems();
            List<Event> cultural = activity.getCulturalItems();
            CustomEventAdapter adapter = activity.getCustomAdapter();
            all.addAll(db.getAllEvents());
            for(Event e :all)
                if(e.getCatégorie().equals("sport")) sport.add(e);
                else cultural.add(e);

            activity.getCustomAdapter().setItems(activity.getAllitems());
            adapter.notifyDataSetChanged();

            ((ResultActivity) context).runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   Toast t = Toast.makeText(context, context.getString(R.string.last_db_loaded), Toast.LENGTH_LONG);
                   t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                   t.show();
               }
           });

            Log.d("Getting", "Last db results");
        return null;
    }
    @Override
    protected void onPostExecute(Void a) {

        progress.dismiss();
    }
}
