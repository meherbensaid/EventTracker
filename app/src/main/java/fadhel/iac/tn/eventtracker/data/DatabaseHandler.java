package fadhel.iac.tn.eventtracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fadhel.iac.tn.eventtracker.model.Event;
import fadhel.iac.tn.eventtracker.model.FavouriteEvent;

/**
 * Created by Fadhel on 29/03/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "EventsDatabase";

    // LastEvents table name
    private static final String TABLE_EVENTS = "last_events";
    // FavoriteEvents table name
    private static final String TABLE_FAVOURITE = "favourite_events";


    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NOTIF_1_FIRED = "fired1_notif";
    private static final String KEY_NOTIF_2_FIRED = "fired2_notif";
    private static final String KEY_ID_NOTIF_2 = "id_notif2";
    private static final String KEY_ID_NOTIF_1 = "id_notif1";
    private static final String KEY_NAME = "name";
    private static final String KEY_EVENT_URL = "event_url";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATESTART = "date_start";
    private static final String KEY_DATEREAL = "date_real";
    private static final String KEY_TIMESTART = "time_start";
    private static final String KEY_DATESTOP = "date_stop";
    private static final String KEY_TIMESTOP = "time_stop";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CITY = "city";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_IMAGEURL = "image_url";
    private static final String KEY_CATEGORIE = "categorie";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String columns = "("
                + KEY_ID + " TEXT," + KEY_NAME + " TEXT," + KEY_EVENT_URL + " TEXT,"+ KEY_DESCRIPTION + " TEXT,"
                + KEY_DATESTART + " TEXT," + KEY_TIMESTART + " TEXT," + KEY_DATESTOP + " TEXT," + KEY_TIMESTOP + " TEXT,"
                + KEY_ADDRESS + " TEXT," + KEY_CITY + " TEXT," + KEY_COUNTRY + " TEXT,"+ KEY_IMAGEURL + " TEXT,"
                + KEY_CATEGORIE + " TEXT," + KEY_DATEREAL + " TEXT" ;
      String createTable= "CREATE TABLE " + TABLE_EVENTS + columns +" )";
        db.execSQL(createTable);
        createTable = "CREATE TABLE " + TABLE_FAVOURITE + columns + " , "+ KEY_NOTIF_1_FIRED + " TEXT , "+ KEY_NOTIF_2_FIRED + " TEXT ," + KEY_ID_NOTIF_1 + " TEXT , " +KEY_ID_NOTIF_2 +" TEXT  )";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE);
        onCreate(db);
    }
    // Adding new Event
    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, event.getId());
        values.put(KEY_NAME, event.getName());
        values.put(KEY_EVENT_URL, event.getEventUrl());
        values.put(KEY_DESCRIPTION, event.getEventUrl());
        values.put(KEY_DATESTART, event.getDateStart());
        values.put(KEY_TIMESTART, event.getTimeStart());
        values.put(KEY_DATESTOP, event.getDateStop());
        values.put(KEY_TIMESTOP, event.getTimeStop());
        values.put(KEY_ADDRESS, event.getAddress());
        values.put(KEY_CITY, event.getCity());
        values.put(KEY_COUNTRY, event.getCountry());
        values.put(KEY_IMAGEURL, event.getImageUrl());
        values.put(KEY_CATEGORIE, event.getCatégorie());
        values.put(KEY_DATEREAL, event.getDateReal());
        // Inserting Row
        if (event instanceof FavouriteEvent) {
            values.put(KEY_NOTIF_1_FIRED,((FavouriteEvent) event).getNotif_1());
            values.put(KEY_NOTIF_2_FIRED,((FavouriteEvent) event).getNotif_2());
            values.put(KEY_ID_NOTIF_1,((FavouriteEvent) event).getId_notif_1());
            values.put(KEY_ID_NOTIF_2,((FavouriteEvent) event).getId_notif_2());
            db.insert(TABLE_FAVOURITE, null, values);
        }
        else db.insert(TABLE_EVENTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting an Event
    public Event getEvent(String id) {
        String selectQuery = "";
        String dbName = "";
        Event event=null;

        selectQuery = "SELECT  * FROM " + TABLE_EVENTS + " WHERE id='" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                event = new Event();
                event.setId(cursor.getString(0));
                event.setName(cursor.getString(1));
                event.setEventUrl(cursor.getString(2));
                event.setDescription(cursor.getString(3));
                event.setDateStart(cursor.getString(4));
                event.setTimeStart(cursor.getString(5));
                event.setDateStop(cursor.getString(6));
                event.setTimeStop(cursor.getString(7));
                event.setAddress(cursor.getString(8));
                event.setCity(cursor.getString(9));
                event.setCountry(cursor.getString(10));
                event.setImageUrl(cursor.getString(11));
                event.setCatégorie(cursor.getString(12));
                event.setDateReal(cursor.getString(13));
            } while (cursor.moveToNext());

        }
        return event;
    }
    // Getting a Favourite Event
    public FavouriteEvent getFavouriteEvent(String id) {
        String selectQuery = "";
        FavouriteEvent fevent = null;
        selectQuery = "SELECT  * FROM " + TABLE_FAVOURITE + " WHERE id='" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                fevent = new FavouriteEvent();
                fevent.setId(cursor.getString(0));
                fevent.setName(cursor.getString(1));
                fevent.setEventUrl(cursor.getString(2));
                fevent.setDescription(cursor.getString(3));
                fevent.setDateStart(cursor.getString(4));
                fevent.setTimeStart(cursor.getString(5));
                fevent.setDateStop(cursor.getString(6));
                fevent.setTimeStop(cursor.getString(7));
                fevent.setAddress(cursor.getString(8));
                fevent.setCity(cursor.getString(9));
                fevent.setCountry(cursor.getString(10));
                fevent.setImageUrl(cursor.getString(11));
                fevent.setCatégorie(cursor.getString(12));
                fevent.setDateReal(cursor.getString(13));
                fevent.setNotif_1(cursor.getString(14));
                fevent.setNotif_2(cursor.getString(15));
                fevent.setId_notif_1(cursor.getString(16));
                fevent.setId_notif_2(cursor.getString(17));

            } while (cursor.moveToNext());

        }

            return fevent;
    }
    // Getting All Events
    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<Event>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(cursor.getString(0));
                event.setName(cursor.getString(1));
                event.setEventUrl(cursor.getString(2));
                event.setDescription(cursor.getString(3));
                event.setDateStart(cursor.getString(4));
                event.setTimeStart(cursor.getString(5));
                event.setDateStop(cursor.getString(6));
                event.setTimeStop(cursor.getString(7));
                event.setAddress(cursor.getString(8));
                event.setCity(cursor.getString(9));
                event.setCountry(cursor.getString(10));
                event.setImageUrl(cursor.getString(11));
                event.setCatégorie(cursor.getString(12));
                event.setDateReal(cursor.getString(13));
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        // return contact list
        return eventList;
    }
    // Getting All FavouriteEvents
    public List<Event> getAllFavouriteEvents() {
        List<Event> eventList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FavouriteEvent event = new FavouriteEvent();

                event.setId(cursor.getString(0));
                event.setName(cursor.getString(1));
                event.setEventUrl(cursor.getString(2));
                event.setDescription(cursor.getString(3));
                event.setDateStart(cursor.getString(4));
                event.setTimeStart(cursor.getString(5));
                event.setDateStop(cursor.getString(6));
                event.setTimeStop(cursor.getString(7));
                event.setAddress(cursor.getString(8));
                event.setCity(cursor.getString(9));
                event.setCountry(cursor.getString(10));
                event.setImageUrl(cursor.getString(11));
                event.setCatégorie(cursor.getString(12));
                event.setDateReal(cursor.getString(13));
                event.setNotif_1(cursor.getString(14));
                event.setNotif_2(cursor.getString(15));
                event.setId_notif_1(cursor.getString(16));
                event.setId_notif_2(cursor.getString(17));
                eventList.add(event);

              } while (cursor.moveToNext());
        }

        // return contact list
        return eventList;
    }
    // Deleting an event
    public void deleteEvent(String database,String id) {
        String dbName = "";
        if (database.equals("favourite")) dbName = TABLE_FAVOURITE;
        else dbName = TABLE_EVENTS;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(dbName, "id=?", new String[]{id});
    }
    // Delete All
    public void deleteAll(String database) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(database.equals("favourite"))
        db.delete(TABLE_FAVOURITE,null,null);
        else db.delete(TABLE_EVENTS,null,null);
        db.close();
    }

    // Getting contacts Count
    public int getEventsCount(String database) {
        String countQuery="";
        if(database.equals("favourite")) countQuery = "SELECT  * FROM " + TABLE_FAVOURITE;
        else countQuery = "SELECT  * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
    public void setNotified(String notif,String id) {
        String field="";
        String not="";
        if(notif.equals("1")) {
            field = KEY_NOTIF_1_FIRED;
            not = KEY_ID_NOTIF_1;
        }
        else {
            field = KEY_NOTIF_2_FIRED;
            not = KEY_ID_NOTIF_2;
        }
        SQLiteDatabase db = this.getWritableDatabase();

        String query = " update "+TABLE_FAVOURITE+" set "+field+ " = 'YES' where " + not + " = "+id;
        db.execSQL(query);
    }
    public void updateNotifId (String notif,String id,String idNotif) {

        String not="";
        if(notif.equals("1"))
            not = KEY_ID_NOTIF_1;

        else
            not = KEY_ID_NOTIF_2;
        SQLiteDatabase db = this.getWritableDatabase();

        String query = " update "+TABLE_FAVOURITE+" set "+not+ " = '" + idNotif+ "' where " + KEY_ID + " = '"+id+"'";
        db.execSQL(query);
    }


    public boolean checkNotified(String notif,String id) {
        String field="";
        if(notif.equals("1"))
            field = KEY_NOTIF_1_FIRED;
        else
            field = KEY_NOTIF_2_FIRED;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = " select * from "+TABLE_FAVOURITE+ " where "+field+ " = "+" 'YES'  and "+ KEY_ID + " = "+id;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) return true;
        else return false;

    }

    public void updateTables(String date) {

        String query = " delete from " + TABLE_FAVOURITE + " where " + KEY_DATEREAL +"<="+"'"+date+"'";
        Log.d("Query",query);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        query = " delete from " + TABLE_EVENTS + " where " + KEY_DATESTART +"<="+"'"+date+"' and "+KEY_DATESTOP+"==''";
        db.execSQL(query);
        query = " delete from " + TABLE_EVENTS + " where " + KEY_DATESTOP +"<="+"'"+date+"' and "+KEY_DATESTOP+"!=''";
        db.execSQL(query);
    }

}
