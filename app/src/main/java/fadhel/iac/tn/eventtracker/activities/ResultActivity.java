package fadhel.iac.tn.eventtracker.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fadhel.iac.tn.eventtracker.utils.ConnectionController;
import fadhel.iac.tn.eventtracker.adapters.CustomEventAdapter;
import fadhel.iac.tn.eventtracker.data.DatabaseHandler;
import fadhel.iac.tn.eventtracker.utils.DateComparator;
import fadhel.iac.tn.eventtracker.parsers.HandleRest;
import fadhel.iac.tn.eventtracker.R;
import fadhel.iac.tn.eventtracker.model.Event;
import fadhel.iac.tn.eventtracker.utils.EventsDBLoader;
import fadhel.iac.tn.eventtracker.utils.WSEventsHandler;

public class ResultActivity extends BaseActivity {
    FloatingActionButton cultureFab;
    FloatingActionButton sportFab;
    FloatingActionButton allFab;
    FloatingActionButton asc;
    FloatingActionButton desc;
    private RecyclerView list = null;
    private HandleRest handleRest = null;
    private List<Event> Allitems = new ArrayList<>();
    private List<Event> culturalItems = new ArrayList<>();
    private List<Event> sportItems = new ArrayList<>();
    private CustomEventAdapter customAdapter = new CustomEventAdapter(culturalItems,ResultActivity.this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle(getString(R.string.results));

        cultureFab = (FloatingActionButton) findViewById(R.id.cultureButton);
        sportFab = (FloatingActionButton) findViewById(R.id.sportButton);
        allFab = (FloatingActionButton) findViewById(R.id.allButton);
        desc = (FloatingActionButton) findViewById(R.id.descButton);
        asc = (FloatingActionButton) findViewById(R.id.ascButton);

        allFab.setBackgroundTintList(ContextCompat.getColorStateList(ResultActivity.this, R.color.fabpressed));

        sportFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sportFab.setBackgroundTintList(ContextCompat.getColorStateList(ResultActivity.this, R.color.fabpressed));
                cultureFab.setBackgroundTintList(ContextCompat.getColorStateList(ResultActivity.this, R.color.fabnotpressed));
                allFab.setBackgroundTintList(ContextCompat.getColorStateList(ResultActivity.this, R.color.fabnotpressed));


                Toast t;
                if (!sportItems.isEmpty()) {
                    t = Toast.makeText(ResultActivity.this, getApplicationContext().getString(R.string.events_sport), Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    t.show();
                    customAdapter.setItems(sportItems);
                    customAdapter.notifyDataSetChanged();
                } else {
                    t = Toast.makeText(ResultActivity.this,getApplicationContext().getString(R.string.no_sportive), Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    t.show();
                }
            }
        });
        cultureFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cultureFab.setBackgroundTintList(ContextCompat.getColorStateList(ResultActivity.this, R.color.fabpressed));
                sportFab.setBackgroundTintList(ContextCompat.getColorStateList(ResultActivity.this, R.color.fabnotpressed));
                allFab.setBackgroundTintList(ContextCompat.getColorStateList(ResultActivity.this, R.color.fabnotpressed));

                Toast t;
                if (!culturalItems.isEmpty()) {
                    t = Toast.makeText(ResultActivity.this, getApplicationContext().getString(R.string.events_culturel), Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    t.show();
                    customAdapter.setItems(culturalItems);
                    customAdapter.notifyDataSetChanged();

                } else {
                    t = Toast.makeText(ResultActivity.this, getApplicationContext().getString(R.string.no_cultural), Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    t.show();

                }
            }
        });
        allFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allFab.setBackgroundTintList(ContextCompat.getColorStateList(ResultActivity.this, R.color.fabpressed));
                sportFab.setBackgroundTintList(ContextCompat.getColorStateList(ResultActivity.this, R.color.fabnotpressed));
                cultureFab.setBackgroundTintList(ContextCompat.getColorStateList(ResultActivity.this, R.color.fabnotpressed));

                Toast t = Toast.makeText(ResultActivity.this, getApplicationContext().getString(R.string.all_events), Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                t.show();
                customAdapter.setItems(Allitems);
                customAdapter.notifyDataSetChanged();


            }
        });

        asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(customAdapter.getItems(), new DateComparator());
                customAdapter.notifyDataSetChanged();
                Toast t = Toast.makeText(ResultActivity.this, getApplicationContext().getString(R.string.tri_date_croi), Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                t.show();
            }
        });

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(customAdapter.getItems(), Collections.reverseOrder(new DateComparator()));
                customAdapter.notifyDataSetChanged();
                Toast t = Toast.makeText(ResultActivity.this, getApplicationContext().getString(R.string.tri_date_decroi), Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                t.show();

            }
        });
        list = (RecyclerView) findViewById(R.id.list);


        LinearLayoutManager llm = new LinearLayoutManager(ResultActivity.this);
        list.setLayoutManager(llm);
        list.setAdapter(customAdapter);
        Intent i = getIntent();
        String urlSport = i.getStringExtra("urlsport");
        String urlCulture = i.getStringExtra("urlculture");
        String urlws = i.getStringExtra("wsurl");
        String country = i.getStringExtra("country");
        String city = i.getStringExtra("city");
           if(country!=null && city!=null && !country.isEmpty() && !city.isEmpty()) {
               Log.d("country", country);
               Log.d("city", city);
           }

        // if we were redirected from preferences activity
        if (urlSport == null && urlCulture == null) {

            SharedPreferences sp = getSharedPreferences("myPrefs", MODE_PRIVATE);
            String culture = sp.getString("urlculture", null);
            String sport = sp.getString("urlsport",null);
            String d1=null,d2=null;
            if(sport!=null && !sport.isEmpty())
            d1 = sport.substring(sport.indexOf(";date="),sport.indexOf(";location="));
            if(culture!=null && !culture.isEmpty())
            d2 = culture.substring(culture.indexOf("date="),culture.indexOf(";location="));
            if(d1!=null && !d1.isEmpty())
            Log.d("d1",d1);
            if(d2!=null && !d2.isEmpty())
                Log.d("d2",d2);
            urlCulture = sp.getString("urlculture",null);
            urlSport = sp.getString("urlsport",null);

        }
        // Verification de la connexion
        boolean connected=false;
        try {
            Log.d("results","checking connection");
            connected = new ConnectionController(ResultActivity.this).execute().get();
        }
        catch(InterruptedException | ExecutionException e) {e.printStackTrace();}
        Log.d("Searching", "Seaching");
        // Si la connexion internet est disponible on effectue la recherche
       if(connected)
        new RetrieveEvents().execute(urlSport,urlCulture,urlws);
        //Sinon on charge le résultat de la dernière recherche
        else new EventsDBLoader(ResultActivity.this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_search:
              boolean  connected=false;
                try{
                    connected = new ConnectionController(ResultActivity.this).execute().get();
                }catch(InterruptedException | ExecutionException e) {e.printStackTrace();}
                if(connected) {
                    Intent i = getIntent();
                    if (i.getStringExtra("activity").equals("splash")) {
                        i = new Intent(ResultActivity.this, ChoiceActivity.class);
                        startActivity(i);
                    } else finish();
                }  else {
                    Toast t = Toast.makeText(ResultActivity.this, getApplicationContext().getString(R.string.connectez_internet), Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    t.show();
                }
                break;
            case R.id.favorites_main:
                    Intent i = new Intent(ResultActivity.this, FavoritesActivity.class);
                    startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private class RetrieveEvents extends AsyncTask<String, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected void onPreExecute() {
            Log.d("display","progress");
            progress = ProgressDialog.show(
                    ResultActivity.this, null, getString(R.string.loading));

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            List<Event> listres = new ArrayList<>();

            if(!params[0].isEmpty()) {
                    int i=1;
                    handleRest = new HandleRest(params[0], "sport");
                    Log.d("urlSPORT",params[0]);
                    int total= handleRest.ParseAndStore(listres,i);
                    int pagenumbers = (total%100==0)?total/100:total/100+1;
                    Log.d("pages",pagenumbers+"");
                    for( i=2;i<=pagenumbers;i++)
                    handleRest.ParseAndStore(listres,i);
                    Allitems.addAll(listres);
                    sportItems.addAll(listres);
                    listres.clear();
            }
            if (!params[1].isEmpty()) {
                 int i=1;
                 handleRest = new HandleRest(params[1], "culture");
                 Log.d("urlCULTURE",params[1]);
                 int total = handleRest.ParseAndStore(listres,i);
                 int pagenumbers = (total%100==0)?total/100:total/100+1;
                 Log.d("Total",total+"");
                 Log.d("pages", pagenumbers + "");
                for( i=2;i<=pagenumbers;i++)
                    handleRest.ParseAndStore(listres,i);
                    Allitems.addAll(listres);
                    culturalItems.addAll(listres);
                    listres.clear();
                }
            if(params[2]!=null)
              {
                    WSEventsHandler wshandler = new WSEventsHandler(ResultActivity.this,params[2]);
                    listres = wshandler.loadEvents();
                    Allitems.addAll(listres);
                    for(Event ev : listres)
                        if(ev.getCatégorie().equalsIgnoreCase("football") || ev.getCatégorie().equalsIgnoreCase("basketball")|| ev.getCatégorie().equalsIgnoreCase("handball") || ev.getCatégorie().equalsIgnoreCase("tennis"))
                        sportItems.add(ev);
                  else culturalItems.add(ev);
              }
            customAdapter.setItems(Allitems);
            DatabaseHandler db = new DatabaseHandler(ResultActivity.this);
            if(db.getEventsCount("events")!=0)
                db.deleteAll("events");
            for(Event ev : Allitems)
            db.addEvent(ev);
            return null;
        }

        @Override
        protected void onPostExecute(Void a) {
            customAdapter.notifyDataSetChanged();
            progress.dismiss();
        }
    }

    public List<Event> getSportItems() {
        return sportItems;
    }

    public CustomEventAdapter getCustomAdapter() {
        return customAdapter;
    }

    public List<Event> getCulturalItems() {
        return culturalItems;
    }

    public List<Event> getAllitems() {
        return Allitems;
    }
}