package fadhel.iac.tn.eventtracker.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import fadhel.iac.tn.eventtracker.R;
import fadhel.iac.tn.eventtracker.data.DatabaseHandler;
import fadhel.iac.tn.eventtracker.utils.ConnectionController;
import fadhel.iac.tn.eventtracker.utils.Locator;
import fadhel.iac.tn.eventtracker.utils.Utils;

/**
 * Created by Fadhel on 28/03/2016.
 */
public class SplashActivity extends AppCompatActivity implements LocationListener {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        boolean connected=true;

        try{
            connected = new ConnectionController(SplashActivity.this).execute().get();
        }catch(InterruptedException | ExecutionException e) {e.printStackTrace();}

        String location = "";
        if(connected)
            try {
                location = new Locator(SplashActivity.this).execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        // Supprimer les anciens événements favoris
        DatabaseHandler db = new DatabaseHandler(SplashActivity.this);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH,-1);
        db.updateTables(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));

        SharedPreferences sp = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        String culture = sp.getString("urlculture", null);
        String sport = sp.getString("urlsport", null);
        Intent i;

        if (sport == null && culture==null)
            i = new Intent(SplashActivity.this, PreferencesActivity.class);

        else {
            String date = Utils.updateDate();
            if(!sport.isEmpty())
                sport = sport.replace(sport.substring(sport.indexOf(";date=")+6,sport.indexOf(";location=")),date);
            if (!culture.isEmpty())
                culture = culture.replace(culture.substring(culture.indexOf(";date=")+6,culture.indexOf(";location=")),date);
            e.putString("urlculture", culture);
            e.putString("urlsport", sport);
            if(location.isEmpty())
                location = sp.getString("prefLocation", getString(R.string.undefined));
            e.putString("location", location);
            e.commit();
            i = new Intent(SplashActivity.this, ResultActivity.class);
            String [] l = location.split(",");
            if(l.length==3) {
                i.putExtra("country", l[2]);
                i.putExtra("city", l[1]);
            }
            else if(l.length==2) {
                i.putExtra("country", l[1]);
                i.putExtra("city", l[0]);
            }

        }
        Log.d("location", sp.getString("location", "none"));
        i.putExtra("activity","splash");
        startActivity(i);
        finish();

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
