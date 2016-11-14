package fadhel.iac.tn.eventtracker.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import fadhel.iac.tn.eventtracker.model.Event;

/**
 * Created by Fadhel on 18/03/2016.
 */
public class DateComparator implements Comparator<Event> {
    @Override
    public int compare(Event o1,Event o2) {
        Date d1 = null;
        Date d2 = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            d1 = df.parse(o1.getDateStart());
            d2 = df.parse(o2.getDateStart());
        }catch(ParseException e) {e.printStackTrace();}
      return d1.compareTo(d2);
    }
}
