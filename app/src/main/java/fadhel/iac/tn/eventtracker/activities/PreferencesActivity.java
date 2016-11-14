package fadhel.iac.tn.eventtracker.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import fadhel.iac.tn.eventtracker.utils.Locator;
import fadhel.iac.tn.eventtracker.adapters.PlacesAutoCompleteAdapter;
import fadhel.iac.tn.eventtracker.R;
import fadhel.iac.tn.eventtracker.utils.Utils;

/**
 * Created by Fadhel on 20/03/2016.
 */
public class PreferencesActivity extends AppCompatActivity implements LocationListener {
    private Toolbar toolbar;
    private Button chercher;
    private RadioButton geo;
    private TextView first_message;
    private RadioGroup country_group;
    private CheckBox sportifs;
    private TextView currentlocation;
    private CheckBox football;
    private CheckBox handball;
    private CheckBox basketball;
    private CheckBox tennis;
    private CheckBox culturels;
    private CheckBox theatre;
    private CheckBox musique;
    private CheckBox cinema;
    private CheckBox plastique;
    private CheckBox litterature;
    private AutoCompleteTextView autocompleteView;
    private ImageButton updateLocation;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.title_activity_preferences);
        sp = getSharedPreferences("myPrefs", MODE_PRIVATE);
        currentlocation = (TextView) findViewById(R.id.current_location);
        currentlocation.setText(sp.getString("location", getString(R.string.undefined)));
        updateLocation = (ImageButton) findViewById(R.id.update_location);
        first_message = (TextView) findViewById(R.id.first_message);
        first_message.setText(R.string.pref_options);

        updateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("location", new Locator(PreferencesActivity.this).execute().get());
                    ed.commit();
                    currentlocation.setText(sp.getString("location", getString(R.string.undefined)));
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });


        geo = (RadioButton) findViewById(R.id.geo);

        autocompleteView = (AutoCompleteTextView) findViewById(R.id.location);
        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.autocomplete_layout_item));

        country_group = (RadioGroup) findViewById(R.id.group_pays);
        country_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == geo.getId()) {
                    findViewById(R.id.update_location_row).setVisibility(View.VISIBLE);
                    findViewById(R.id.enter_location_row).setVisibility(View.GONE);
                    findViewById(R.id.location_row).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.update_location_row).setVisibility(View.GONE);
                    findViewById(R.id.enter_location_row).setVisibility(View.VISIBLE);
                    findViewById(R.id.location_row).setVisibility(View.GONE);
                }
            }
        });
        chercher = (Button) findViewById(R.id.chercher_button);
        sportifs = (CheckBox) findViewById(R.id.sportifs);
        culturels = (CheckBox) findViewById(R.id.culturels);
        football = (CheckBox) findViewById(R.id.football);
        handball = (CheckBox) findViewById(R.id.handball);
        tennis = (CheckBox) findViewById(R.id.tennis);
        basketball = (CheckBox) findViewById(R.id.basketball);
        cinema = (CheckBox) findViewById(R.id.cinema);
        litterature = (CheckBox) findViewById(R.id.litterature);
        musique = (CheckBox) findViewById(R.id.musique);
        plastique = (CheckBox) findViewById(R.id.plastique);
        theatre = (CheckBox) findViewById(R.id.theatre);

        findViewById(R.id.date_du_row).setVisibility(View.GONE);
        findViewById(R.id.date_au_row).setVisibility(View.GONE);
        chercher.setText(R.string.save);

        sportifs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    football.setEnabled(true);
                    handball.setEnabled(true);
                    tennis.setEnabled(true);
                    basketball.setEnabled(true);
                } else {
                    football.setEnabled(false);
                    handball.setEnabled(false);
                    tennis.setEnabled(false);
                    basketball.setEnabled(false);
                    football.setChecked(false);
                    handball.setChecked(false);
                    tennis.setChecked(false);
                    basketball.setChecked(false);
                }
            }
        });
        culturels.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cinema.setEnabled(true);
                    theatre.setEnabled(true);
                    plastique.setEnabled(true);
                    musique.setEnabled(true);
                    litterature.setEnabled(true);
                } else {
                    cinema.setEnabled(false);
                    theatre.setEnabled(false);
                    plastique.setEnabled(false);
                    musique.setEnabled(false);
                    litterature.setEnabled(false);
                    cinema.setChecked(false);
                    theatre.setChecked(false);
                    plastique.setChecked(false);
                    musique.setChecked(false);
                    litterature.setChecked(false);
                }

            }
        });

        chercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = false;

                if (geo.isChecked() && (sp.getString("location", "Aucune").equals("Aucune") || currentlocation.getText().equals(""))) {
                    currentlocation.setError(getApplicationContext().getString(R.string.plz_update_location));
                    error = true;
                }
                if (!error) {

                    String baseUrl = "";
                    String baseUrl2 = "";
                    String date = "date=";

                    date += Utils.updateDate();

                    String keywords = ";keywords=";
                    String location = "";
                    if (geo.isChecked()) {
                        location = sp.getString("location", "Mettez à jour votre localisation");
                        location = location.replace(" ","");
                    }
                    else
                        location = autocompleteView.getText().toString().replaceAll("\\s", "");

                    if (sportifs.isChecked()) {

                        baseUrl = "http://api.eventful.com/rest/events/search?app_key="+ ChoiceActivity.app_key;
                        baseUrl += date;
                        baseUrl += ";location=";
                        baseUrl += location;
                        baseUrl += ";category=sports,";
                        if (football.isChecked()) keywords += "football || soccer || ";
                        if (handball.isChecked()) keywords += " handball || ";
                        if (basketball.isChecked()) keywords += " basketball || ";
                        if (tennis.isChecked()) keywords += " tennis || ";
                        if (!football.isChecked() && !handball.isChecked() && !basketball.isChecked() && !tennis.isChecked())
                            keywords += "football || soccer ||  handball || basketball || tennis ";
                        baseUrl += keywords;
                        if (keywords.length() > 3)
                            if (keywords.substring(keywords.length() - 3, keywords.length() - 1).equals("|| "))
                                keywords = keywords.substring(0, keywords.length() - 3);

                        baseUrl = baseUrl.replace(" ", "%20");

                    }
                    if (culturels.isChecked()) {
                        baseUrl2 = "http://api.eventful.com/rest/events/search?app_key="+ ChoiceActivity.app_key;
                        baseUrl2 += date;
                        baseUrl2 += ";location=";
                        baseUrl2 += location;
                        baseUrl2 += ";category=";
                        if (musique.isChecked()) baseUrl2 += "music,";
                        if (theatre.isChecked()) baseUrl2 += "comedy,";
                        if (cinema.isChecked()) baseUrl2 += "movies_film,";
                        if (plastique.isChecked()) baseUrl2 += "art,";
                        if (litterature.isChecked()) baseUrl2 += "books,";
                        if (!musique.isChecked() && !theatre.isChecked() && !plastique.isChecked() && !litterature.isChecked() && !cinema.isChecked())
                            baseUrl2 += "music,comedy,movies_film,books,art";

                        if (baseUrl2.charAt(baseUrl2.length() - 1) == ',')
                            baseUrl2 = baseUrl2.substring(0, baseUrl2.length() - 1);

                    }
                    Log.d("keywords", keywords);
                    Log.d("size", keywords.length() + "");
                    Intent i = new Intent(PreferencesActivity.this, ResultActivity.class);
                    if(!baseUrl.isEmpty())
                    baseUrl+=";page_size=100";
                    if(!baseUrl2.isEmpty())
                    baseUrl2+=";page_size=100";
                    SharedPreferences.Editor e = sp.edit();
                    e.putString("urlsport", baseUrl);
                    e.putString("urlculture", baseUrl2);
                    e.putString("prefLocation",location);
                    i.putExtra("country", location.substring(location.lastIndexOf(",") + 1, location.length()));
                    i.putExtra("activity","prefs");
                    e.commit();
                    Log.d("URL", baseUrl);
                    Log.d("URL2", baseUrl2);
                    startActivity(i);

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.removeItem(R.id.action_search);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.favorites_main:
                Intent i = new Intent(PreferencesActivity.this, FavoritesActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
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