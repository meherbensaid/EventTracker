package fadhel.iac.tn.eventtracker.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.concurrent.ExecutionException;

import fadhel.iac.tn.eventtracker.model.FavouriteEvent;
import fadhel.iac.tn.eventtracker.utils.ConnectionController;
import fadhel.iac.tn.eventtracker.data.DatabaseHandler;
import fadhel.iac.tn.eventtracker.utils.EventUtils;
import fadhel.iac.tn.eventtracker.R;
import fadhel.iac.tn.eventtracker.utils.Utils;
import fadhel.iac.tn.eventtracker.model.Event;

public class DetailActivity extends BaseActivity {
      private TextView title;
      private TextView desc;
      private TextView date;
     private TextView heure;
     private TextView lieu;
      private ImageView img;
      private FloatingActionButton link ;
     private  FloatingActionButton favoris ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        title = (TextView) findViewById(R.id.detail_title);
        desc = (TextView) findViewById(R.id.detail_description);
        date = (TextView) findViewById(R.id.detail_date);
        heure = (TextView) findViewById(R.id.detail_heure);
        lieu = (TextView) findViewById(R.id.lieu);
        img = (ImageView) findViewById(R.id.detail_image);
        link = (FloatingActionButton) findViewById(R.id.link);
        favoris = (FloatingActionButton) findViewById(R.id.ajouter_favoris);
        setTitle(getResources().getString(R.string.details));
        Bundle data = getIntent().getExtras();
        Event e =  (Event) data.getParcelable("event");
        if(e instanceof FavouriteEvent) {
            FavouriteEvent fav = (FavouriteEvent) e;
            if(!e.getDateStop().isEmpty()) {
                TextView dateChoisie = (TextView) findViewById(R.id.detail_date_choisie);
                TableRow trow = (TableRow) findViewById(R.id.row_date_choisie);
                trow.setVisibility(View.VISIBLE);
                dateChoisie.setText(getString(R.string.chosenDate)+":"+Utils.transformDate(fav.getDateReal(),"yyyy-MM-dd","dd-MM-yyyy"));
            }
        }
        title.setText(e.getName());
        String d1,d=null;
        d = Utils.transformDate(e.getDateStart(), "yyyy-MM-dd", "dd-MM-yyyy");
        if(e.getDateStop().isEmpty())
            date.setText("Le  "+d);
        else {
            d1 = Utils.transformDate(e.getDateStop(),"yyyy-MM-dd","dd-MM-yyyy");
            date.setText(getApplicationContext().getString(R.string.du)+" " + d + " "+getApplicationContext().getString(R.string.au)+" " + d1);
        }
        if(e.getTimeStart()!=null && !e.getTimeStart().equals("00:00:00")) {
            Log.d("timeStop","="+e.getTimeStop());
            if (e.getTimeStop()!=null && !e.getTimeStop().isEmpty() && !e.getTimeStart().equals(e.getTimeStop()))
                heure.setText(getApplicationContext().getString(R.string.de)+" " + e.getTimeStart() + " "+ getApplicationContext().getString(R.string.a) +" " + e.getTimeStop());
            else
                heure.setText("A " + e.getTimeStart());
        }
        String adr = "";
        if(!e.getAddress().trim().isEmpty()) adr+=e.getAddress() + ",";
        if(!e.getCity().trim().isEmpty()) adr+=e.getCity()+",";
        if(!e.getCountry().trim().isEmpty()) adr+=e.getCountry();
        lieu.setText(adr);
        desc.setText(e.getDescription());
        if(!e.getImageUrl().isEmpty())
            Picasso.with(DetailActivity.this).load(e.getImageUrl()).into(img);
        else {
            if(e.getCatégorie().equals("sport"))
                img.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this, R.drawable.sports));
            else  img.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this, R.drawable.culture));
        }

        final Event finalE = e;
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(finalE.getEventUrl());
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }
        });


        DatabaseHandler dbH = new DatabaseHandler(DetailActivity.this);
        if(dbH.getFavouriteEvent(e.getId())!=null)
            favoris.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this,R.drawable.star_enabled));
        else favoris.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this,R.drawable.star_disabled));

        favoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoris.getDrawable().getConstantState().equals(ContextCompat.getDrawable(DetailActivity.this, R.drawable.star_enabled).getConstantState())) {
                    EventUtils.removeFromFavourite(DetailActivity.this, finalE);
                    favoris.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this,R.drawable.star_disabled));
                    Toast t = Toast.makeText(DetailActivity.this, getApplicationContext().getString(R.string.retirer_favoris), Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    t.show();
                }
              else {
                    if(EventUtils.addToFavourite(DetailActivity.this, finalE))
                    favoris.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this, R.drawable.star_enabled));

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle data = getIntent().getExtras();
        Event e =  (Event) data.getParcelable("event");
        DatabaseHandler dbH = new DatabaseHandler(DetailActivity.this);
        if(dbH.getFavouriteEvent(e.getId())!=null)
            favoris.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this,R.drawable.star_enabled));
        else favoris.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this,R.drawable.star_disabled));
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
                    connected = new ConnectionController(DetailActivity.this).execute().get();
                }catch(InterruptedException | ExecutionException e) {e.printStackTrace();}
                if(connected) {
                       Intent i = new Intent(DetailActivity.this, ChoiceActivity.class);
                        startActivity(i);
                }
                else {
                    Toast t = Toast.makeText(DetailActivity.this, getApplicationContext().getString(R.string.connectez_internet), Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    t.show();
                }
                break;
            case R.id.favorites_main:
                Intent i = new Intent(DetailActivity.this, FavoritesActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
