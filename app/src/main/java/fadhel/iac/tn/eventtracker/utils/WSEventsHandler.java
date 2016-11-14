package fadhel.iac.tn.eventtracker.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fadhel.iac.tn.eventtracker.model.Event;

/**
 * Created by Fadhel on 02/06/2016.
 */
public class WSEventsHandler {

    private Context context;
    private String url;

    public WSEventsHandler(Context context, String url) {
        this.context=context;
        this.url = url;
    }

    public List<Event> loadEvents()
    {
        ServiceHandler sh = new ServiceHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url);
        List<Event> events = new ArrayList<>();
        Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray contacts = jsonObj.getJSONArray("events");

                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);

                    String id = c.getString("id");
                    String titre = c.getString("titre");
                    String eventStartDate = c.getString("eventStartDate");
                    String eventEndDate = c.getString("eventEndDate");
                    String eventTimeStart = c.getString("eventTimeStart");
                    String eventTimeStop = c.getString("eventTimeStop");
                    String pays = c.getString("pays");
                    String category = c.getString("category");
                    String ville = c.getString("ville");
                    String adresse = c.getString("addresse");
                    String description = c.getString("Description");
                    // tmp hashmap for single contact
                    Event e = new Event();
                    e.setId(id);
                    e.setName(titre);
                    e.setDateStart(eventStartDate);
                    e.setDateStop(eventEndDate);
                    e.setTimeStart(eventTimeStart);
                    e.setTimeStop(eventTimeStop);
                    e.setCatégorie(category);
                    e.setCity(ville);
                    e.setCountry(pays);
                    e.setAddress(adresse);
                    e.setDescription(description);
                    Log.d("event", e.toString());
                    events.add(e);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return events;
} }
