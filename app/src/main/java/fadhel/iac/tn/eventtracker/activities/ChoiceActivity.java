package fadhel.iac.tn.eventtracker.activities;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import fadhel.iac.tn.eventtracker.utils.Locator;
import fadhel.iac.tn.eventtracker.adapters.PlacesAutoCompleteAdapter;
import fadhel.iac.tn.eventtracker.R;
import fadhel.iac.tn.eventtracker.utils.Utils;

/**
 * Created by Fadhel on 13/02/2016.
 */
public class ChoiceActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {
    private Toolbar toolbar;
    private Button chercher;
    private RadioButton geo;
    private RadioGroup country_group;
    private CheckBox sportifs;
    private EditText du;
    private EditText au;
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
    private DatePickerDialog debPicker;
    private DatePickerDialog finPicker;
    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private ImageButton updateLocation;
    private SharedPreferences sp ;
    public static String app_key="cfnQxLNXHDHFvRmD;";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.rech_av));
        sp = getSharedPreferences("myPrefs",MODE_PRIVATE);
        currentlocation = (TextView) findViewById(R.id.current_location);
        currentlocation.setText(sp.getString("location",getString(R.string.undefined)));
        updateLocation = (ImageButton) findViewById(R.id.update_location);

        updateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              try{
                  SharedPreferences.Editor ed = sp.edit();
                  ed.putString("location",new Locator(ChoiceActivity.this).execute().get());
                  ed.commit();
                  currentlocation.setText(sp.getString("location", getString(R.string.undefined)));
              }catch(InterruptedException | ExecutionException e) {e.printStackTrace();}
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
                }
                else {
                    findViewById(R.id.update_location_row).setVisibility(View.GONE);
                    findViewById(R.id.location_row).setVisibility(View.GONE);
                    findViewById(R.id.enter_location_row).setVisibility(View.VISIBLE);
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


        du = (EditText) findViewById(R.id.date_debut);
        du.setInputType(InputType.TYPE_NULL);
        du.setOnClickListener(this);


        au = (EditText) findViewById(R.id.date_fin);
        au.setInputType(InputType.TYPE_NULL);
        au.setOnClickListener(this);

        Calendar cal = Calendar.getInstance();
        debPicker = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                du.setText(df.format(newDate.getTime()));
            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));


        finPicker = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                au.setText(df.format(newDate.getTime()));
            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        du.setText(df.format(cal.getTime()));
        cal.add(Calendar.DATE, 7);
        au.setText(df.format(cal.getTime()));

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
                Date deb = Utils.getDate(du.getText().toString(),"dd/MM/yyyy");
                Date fin = Utils.getDate(au.getText().toString(),"dd/MM/yyyy");
                if(deb.compareTo(fin)>0) {
                    au.setError(getApplicationContext().getString(R.string.date_error));
                    error=true;
                }
                if (geo.isChecked() && (sp.getString("location", "Aucune").equals("Aucune") || currentlocation.getText().equals(""))) {
                    currentlocation.setError(getApplicationContext().getString(R.string.plz_update_location));
                    error = true;
                }
                    if(!error) {
                    String baseUrl = "";
                    String baseUrl2 = "";
                    String baseUrlWS = "";
                    String date = "date=";
                    String d1,d2 = null;
                    d1 = Utils.transformDate(du.getText().toString(), "dd/MM/yyyy", "yyyyMMdd00");
                    d2 = Utils.transformDate(au.getText().toString(),"dd/MM/yyyy","yyyyMMdd00");
                    date += d1 + "-" + d2;
                    String keywords = ";keywords=";
                    String location = "";
                    if (geo.isChecked()) {
                        location = sp.getString("location", "Mettez à jour votre localisation");
                        location = location.replace(" ","");
                    }
                         else
                         location = autocompleteView.getText().toString().replaceAll("\\s", "");

                    String [] l = location.split(",");
                    String country ="",city="";

                    if(l.length==3) {
                         country = l[2];
                         city = l[1];
                    }
                    else if(l.length==2) {
                         country = l[1];
                         city = l[0];
                    }

                    baseUrlWS = "http://169.254.69.177/eventstracker/api//findAll?";
                        if (sportifs.isChecked()) {
                            baseUrl = "http://api.eventful.com/rest/events/search?app_key="+app_key;
                            baseUrl += date;
                            //baseUrlWS+=date;
                            baseUrl += ";location=";
                            baseUrl += location;
                            baseUrl += ";category=sports,";
                            baseUrlWS+="categorie=";
                            if (football.isChecked()) {
                                keywords += "football || soccer || ";
                                baseUrlWS+="football,";
                            }
                            if (handball.isChecked()) {
                                keywords += " handball || ";
                                baseUrlWS+="handball,";
                            }
                            if (basketball.isChecked()) {
                                keywords += " basketball || ";
                                baseUrlWS+="basketball,";
                            }
                            if (tennis.isChecked()) {
                                keywords += " tennis || ";
                                baseUrlWS+="tennis,";
                            }
                            if(!football.isChecked() && !handball.isChecked() && !basketball.isChecked() && !tennis.isChecked())
                            {
                                keywords += "football || soccer ||  handball || basketball || tennis ";
                                baseUrlWS +="football,handball,basketball,tennis,";
                            }

                            baseUrl += keywords;
                            if (keywords.length() > 3)
                                if (keywords.substring(keywords.length() - 3, keywords.length() - 1).equals("|| "))
                                    keywords = keywords.substring(0, keywords.length() - 3);

                            baseUrl = baseUrl.replace(" ", "%20");
                        }
                        if (culturels.isChecked()) {
                            baseUrl2 = "http://api.eventful.com/rest/events/search?app_key="+app_key;
                            baseUrl2 += date;
                            baseUrl2 += ";location=";
                            baseUrl2 += location;
                            baseUrl2 += ";category=";
                            if(!baseUrlWS.contains("categorie="))
                                baseUrlWS+="categorie=";
                            if (musique.isChecked()) {
                                baseUrl2 += "music,";
                                baseUrlWS+="musique,";
                            }
                            if (theatre.isChecked()) {
                                baseUrl2 += "comedy,";
                                baseUrlWS+="theatre,";
                            }
                            if (cinema.isChecked()) {
                                baseUrl2 += "movies_film,";
                                baseUrlWS+="cinema,";
                            }
                            if (plastique.isChecked()) {
                                baseUrl2 += "art,";
                                baseUrlWS+="art,";
                            }
                            if (litterature.isChecked()) {
                                baseUrl2 += "books,";
                                baseUrlWS+="litterature,";
                            }
                            if (!musique.isChecked() && !theatre.isChecked() && !plastique.isChecked() && !litterature.isChecked() && !cinema.isChecked()) {
                                baseUrl2 += "music,comedy,movies_film,books,art";
                                baseUrlWS+="musique,theatre,cinema,art,litterature";
                            }

                            if (baseUrl2.charAt(baseUrl2.length() - 1) == ',')
                                baseUrl2 = baseUrl2.substring(0, baseUrl2.length() - 1);
                            if (baseUrlWS.charAt(baseUrlWS.length() - 1) == ',')
                                baseUrlWS = baseUrlWS.substring(0, baseUrlWS.length() - 1);
                        }
                        Log.d("keywords", keywords);
                        Log.d("Keywords size", keywords.length() + "");
                    baseUrlWS+="&ville="+city;
                    baseUrlWS = baseUrlWS.replace(" ", "%20");
                    Intent i = new Intent(ChoiceActivity.this, ResultActivity.class);
                     if(!baseUrl.isEmpty())
                     baseUrl+=";page_size=100";
                    if(!baseUrl2.isEmpty())
                    baseUrl2+=";page_size=100";
                    i.putExtra("urlsport", baseUrl);
                    i.putExtra("urlculture", baseUrl2);
                    i.putExtra("url1ws", baseUrlWS);
                    i.putExtra("activity", "choix");
                    i.putExtra("country", country);
                    i.putExtra("city", city);
                        Log.d("URL", baseUrl);
                        Log.d("URL2", baseUrl2);
                    if(country.equalsIgnoreCase("Tunisie")||country.equalsIgnoreCase("Tunisia"))
                    {
                        i.putExtra("wsurl",baseUrlWS);
                        Log.d("WSurl",baseUrlWS);
                    }
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
               Intent i = new Intent(ChoiceActivity.this, FavoritesActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == du) {
            du.setError(null);
            debPicker.show();
        } else if (v == au) {
            au.setError(null);
            finPicker.show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(),getApplicationContext().getString(R.string.gps_not_provided),
                Toast.LENGTH_SHORT).show();
    }

}
