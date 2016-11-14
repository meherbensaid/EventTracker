package fadhel.iac.tn.eventtracker.parsers;

import android.text.Html;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import fadhel.iac.tn.eventtracker.model.Event;

/**
 * Created by Fadhel on 24/02/2016.
 */
public class HandleRest {
    private String url_link= null;
    private String catégorie = null;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;


    public HandleRest(String url_link,String catégorie) {
        this.url_link= url_link;
        this.catégorie = catégorie;
    }

    public int getCount() {
        int tag;
        int n=0;
        String text=null;
        XmlPullParser myParser = null;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
             myParser = xmlFactoryObject.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        try {

            tag = myParser.getEventType();

            while (tag != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();

                switch (tag) {
                    case XmlPullParser.START_TAG:

                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        switch (name) {
                            case "total_items":
                                n = Integer.valueOf(text);
                                break;

                        }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return n;
    }

    public int parseXMLAndStoreIt(XmlPullParser myParser,List<Event> res) {
        int event;
        int total=0;
        String text=null;
        Event item = new Event();


        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();

                    switch (event){
                        case XmlPullParser.START_TAG:
                            if(name.equals("event"))
                               item.setId(myParser.getAttributeValue(null,"id"));
                            break;

                        case XmlPullParser.TEXT:
                            text = myParser.getText();
                            break;

                        case XmlPullParser.END_TAG:
                            switch(name) {
                                case "total_items" :

                                    total = Integer.valueOf(text);
                                    Log.d("TOTO",total+"");
                                    break;
                                case "title" : item.setName(text);
                                    break;
                                case "url":
                                    if(text.contains("/images/medium/"))
                                        item.setImageUrl(text);
                                    else if(text.contains("/events/"))
                                        item.setEventUrl(text);
                                    break;
                                case "description":item.setDescription(Html.fromHtml(text).toString());
                                    break;
                                case "start_time":
                                    if(text.length()>0) {
                                        item.setDateStart(text.substring(0,10));
                                        item.setTimeStart(text.substring(11, text.length()));
                                    }
                                    break;
                                case "stop_time":
                                    if(text.length()>7) {
                                        item.setDateStop(text.substring(0, 10));
                                        item.setTimeStop(text.substring(11, text.length()));
                                    }
                                    break;
                                case "venue_address": item.setAddress(text);
                                    break;
                                case "city_name": item.setCity(text);
                                    break;

                                case "country_name": item.setCountry(text);
                                    break;

                                case "event":
                                    if(catégorie.equals("sport")) item.setCatégorie("sport");
                                    else item.setCatégorie("culture");
                                    if(item.getDateStop().isEmpty() || item.getDateStop().equals(item.getDateStart()))
                                        item.setDateReal(item.getDateStart());
                                    res.add(item);
                                    item = new Event();
                                    break;
                                default:break;
                            }
                            break;

                    }

                event = myParser.next();
            }

            parsingComplete = false;
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public int ParseAndStore(List<Event> res,int pagenumber) {
        int total=0;
        try {
            Log.d("URL",url_link+";page_number="+pagenumber);
            URL url = new URL(url_link+";page_number="+pagenumber);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.d("Run","Running");
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            Log.d("Connection", "Before connection");
            // Starts the query
            conn.connect();
            Log.d("Connection", "Connected");
            InputStream stream = conn.getInputStream();
            Log.d("Connection", "Getting Input Stream");
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();

            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myparser.setInput(stream, null);
            total =  parseXMLAndStoreIt(myparser,res);
            stream.close();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
}
