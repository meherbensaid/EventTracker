package fadhel.iac.tn.eventtracker.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fadhel on 23/04/2016.
 */
public class FavouriteEvent extends Event {

    private String notif_1;
    private String notif_2;
    private String id_notif_1;
    private String id_notif_2;


    public FavouriteEvent() {}

    public FavouriteEvent(Event ev,String id_notif_1, String id_notif_2, String notif_1, String notif_2) {
        super(ev);
        this.id_notif_1 = id_notif_1;
        this.id_notif_2 = id_notif_2;
        this.notif_1 = notif_1;
        this.notif_2 = notif_2;
    }

    public String getNotif_1() {
        return notif_1;
    }

    public void setNotif_1(String notif_1) {
        this.notif_1 = notif_1;
    }

    public String getNotif_2() {
        return notif_2;
    }

    public void setNotif_2(String notif_2) {
        this.notif_2 = notif_2;
    }

    public String getId_notif_1() {
        return id_notif_1;
    }

    public void setId_notif_1(String id_notif_1) {
        this.id_notif_1 = id_notif_1;
    }

    public String getId_notif_2() {
        return id_notif_2;
    }

    public void setId_notif_2(String id_notif_2) {
        this.id_notif_2 = id_notif_2;
    }

    @Override
    public boolean equals(Object o) {
       return super.equals(o);

    }

    @Override
    public int hashCode() {
     return super.hashCode();
    }

    @Override
    public String toString() {
        return "FavouriteEvent{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", eventUrl='" + eventUrl + '\'' +
                ", description='" + description + '\'' +
                ", dateStart='" + dateStart + '\'' +
                ", timeStart='" + timeStart + '\'' +
                ", dateStop='" + dateStop + '\'' +
                ", timeStop='" + timeStop + '\'' +
                ", placeUrl='" + placeUrl + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", category='" + catégorie + '\'' +
                ", dateReal='" + dateReal + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ",notif_1='" + notif_1 + '\'' +
                ", notif_2='" + notif_2 + '\'' +
                ",id_notif_1='" + id_notif_1 + '\'' +
                ", id_notif_2='" + id_notif_2 + '\'' +
                '}';

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public FavouriteEvent(Parcel in){
        String[] data = new String[18];

        in.readStringArray(data);
        this.id = data[0];
        this.name = data[1];
        this.catégorie = data[2];
        this.dateStart = data[3];
        this.dateStop = data[4];
        this.description = data[5];
        this.eventUrl = data[6];
        this.imageUrl = data[7];
        this.city = data[8];
        this.country = data[9];
        this.address = data[10];
        this.timeStart = data[11];
        this.timeStop = data[12];
        this.dateReal = data[13];
        this.notif_1 = data[14];
        this.notif_2 = data[15];
        this.id_notif_1 = data[16];
        this.id_notif_2 = data[17];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.id,this.name,this.catégorie,this.dateStart,this.dateStop,this.description,this.eventUrl,this.imageUrl,this.city,this.country,this.address,this.timeStart,this.timeStop,this.dateReal,this.notif_1,this.notif_2,this.id_notif_1,this.id_notif_2});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Event createFromParcel(Parcel in) {
            return new FavouriteEvent(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
