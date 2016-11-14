package fadhel.iac.tn.eventtracker.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
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

import fadhel.iac.tn.eventtracker.adapters.CustomEventAdapter;
import fadhel.iac.tn.eventtracker.data.DatabaseHandler;
import fadhel.iac.tn.eventtracker.utils.ConnectionController;
import fadhel.iac.tn.eventtracker.utils.DateComparator;
import fadhel.iac.tn.eventtracker.R;
import fadhel.iac.tn.eventtracker.model.Event;

/**
 * Created by Fadhel on 14/04/2016.
 */
public class FavoritesActivity extends BaseActivity {
    FloatingActionButton cultureFab;
    FloatingActionButton sportFab;
    FloatingActionButton allFab;
    FloatingActionButton asc;
    FloatingActionButton desc;
    private RecyclerView list = null;
    private List<Event> Allitems = new ArrayList<>();
    private List<Event> culturalItems = new ArrayList<>();
    private List<Event> sportItems = new ArrayList<>();
    private CustomEventAdapter customAdapter = new CustomEventAdapter(culturalItems,FavoritesActivity.this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle(getString(R.string.favorites));

        cultureFab = (FloatingActionButton) findViewById(R.id.cultureButton);
        sportFab = (FloatingActionButton) findViewById(R.id.sportButton);
        allFab = (FloatingActionButton) findViewById(R.id.allButton);
        desc = (FloatingActionButton) findViewById(R.id.descButton);
        asc = (FloatingActionButton) findViewById(R.id.ascButton);

        allFab.setBackgroundTintList(ContextCompat.getColorStateList(FavoritesActivity.this, R.color.fabpressed));

        sportFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sportFab.setBackgroundTintList(ContextCompat.getColorStateList(FavoritesActivity.this, R.color.fabpressed));
                cultureFab.setBackgroundTintList(ContextCompat.getColorStateList(FavoritesActivity.this, R.color.fabnotpressed));
                allFab.setBackgroundTintList(ContextCompat.getColorStateList(FavoritesActivity.this, R.color.fabnotpressed));


                Toast t;
                if (!sportItems.isEmpty()) {
                    t = Toast.makeText(FavoritesActivity.this, getApplicationContext().getString(R.string.events_sport), Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    t.show();
                    customAdapter.setItems(sportItems);
                    customAdapter.notifyDataSetChanged();
                } else {
                    t = Toast.makeText(FavoritesActivity.this, getApplicationContext().getString(R.string.no_sportive), Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    t.show();
                }
            }
        });
        cultureFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cultureFab.setBackgroundTintList(ContextCompat.getColorStateList(FavoritesActivity.this, R.color.fabpressed));
                sportFab.setBackgroundTintList(ContextCompat.getColorStateList(FavoritesActivity.this, R.color.fabnotpressed));
                allFab.setBackgroundTintList(ContextCompat.getColorStateList(FavoritesActivity.this, R.color.fabnotpressed));

                Toast t;
                if (!culturalItems.isEmpty()) {
                    t = Toast.makeText(FavoritesActivity.this, getApplicationContext().getString(R.string.events_culturel), Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    t.show();
                    customAdapter.setItems(culturalItems);
                    customAdapter.notifyDataSetChanged();

                } else {
                    t = Toast.makeText(FavoritesActivity.this, getApplicationContext().getString(R.string.no_cultural), Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    t.show();

                }
            }
        });
        allFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allFab.setBackgroundTintList(ContextCompat.getColorStateList(FavoritesActivity.this, R.color.fabpressed));
                sportFab.setBackgroundTintList(ContextCompat.getColorStateList(FavoritesActivity.this, R.color.fabnotpressed));
                cultureFab.setBackgroundTintList(ContextCompat.getColorStateList(FavoritesActivity.this, R.color.fabnotpressed));

                Toast t = Toast.makeText(FavoritesActivity.this, getApplicationContext().getString(R.string.all_events), Toast.LENGTH_SHORT);
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
                Toast t = Toast.makeText(FavoritesActivity.this, getApplicationContext().getString(R.string.tri_date_croi), Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                t.show();
            }
        });

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(customAdapter.getItems(), Collections.reverseOrder(new DateComparator()));
                customAdapter.notifyDataSetChanged();
                Toast t = Toast.makeText(FavoritesActivity.this, getApplicationContext().getString(R.string.tri_date_decroi), Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                t.show();

            }
        });
        list = (RecyclerView) findViewById(R.id.list);


        LinearLayoutManager llm = new LinearLayoutManager(FavoritesActivity.this);
        list.setLayoutManager(llm);
        list.setAdapter(customAdapter);
        loadEvents();
        customAdapter.setItems(Allitems);
        Log.d("EBDSIZE",Allitems.size()+"");
        Collections.sort(customAdapter.getItems(), new DateComparator());
        customAdapter.notifyDataSetChanged();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        loadEvents();
        if (allFab.getBackgroundTintList().equals(ContextCompat.getColorStateList(FavoritesActivity.this, R.color.fabpressed)))
            customAdapter.setItems(Allitems);
        else if(sportFab.getBackgroundTintList().equals(ContextCompat.getColorStateList(FavoritesActivity.this, R.color.fabpressed)))
            customAdapter.setItems(sportItems);
        else customAdapter.setItems(culturalItems);
            customAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.removeItem(R.id.favorites_main);
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
                boolean connected = false;
                try {
                    connected = new ConnectionController(FavoritesActivity.this).execute().get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if (connected) {
                    Intent i = new Intent(FavoritesActivity.this, ChoiceActivity.class);
                    startActivity(i);
                } else {
                    Toast t = Toast.makeText(FavoritesActivity.this, getApplicationContext().getString(R.string.connectez_internet), Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    t.show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //getters

    public List<Event> getAllitems() {
        return Allitems;
    }

    public List<Event> getCulturalItems() {
        return culturalItems;
    }

    public List<Event> getSportItems() {
        return sportItems;
    }

    public void loadEvents() {
        DatabaseHandler db = new DatabaseHandler(FavoritesActivity.this);
        Allitems = db.getAllFavouriteEvents();
        Log.d("size",Allitems.size()+"");
        culturalItems.clear();
        sportItems.clear();
        for(Event e : Allitems) {
            if (e.getCatégorie().equals("sport"))
            {
                if (!sportItems.contains(e))
                    sportItems.add(e);
            }
           else {
                if (!culturalItems.contains(e))
                    culturalItems.add(e);
            }
            Log.d(e.getName(),e.toString());
        }
        Log.d("ALl",Allitems.size()+"");
        Log.d("Sport",sportItems.size()+"");
        Log.d("Culture",culturalItems.size()+"");

    }
}
