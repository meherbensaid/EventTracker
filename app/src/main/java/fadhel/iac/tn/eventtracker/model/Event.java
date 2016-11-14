package fadhel.iac.tn.eventtracker.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fadhel on 24/02/2016.
 */
public class Event implements Parcelable {


    protected String id = null;
    protected String name = null;
    protected String eventUrl = null;
    protected String description = null;
    protected String dateStart = null;
    protected String dateReal = null;
    protected String timeStart = null;
    protected String dateStop = null;
    protected String timeStop = null;
    protected String placeUrl = null;
    protected String address = null;
    protected String city = null;
    protected String country = null;
    protected String imageUrl = null;
    protected String catégorie = null;

    public Event(){
        id = "";
        name = "";
        eventUrl = "";
        description = "";
        dateStart = "";
        timeStart = "";
        dateStop = "";
        timeStop = "";
        placeUrl = "";
        address = "";
        city = "";
        country = "";
        imageUrl = "";
        catégorie="";
        dateReal="";
    }

    protected Event(Event o) {
        id = o.getId();
        name = o.getName();
        eventUrl = o.getEventUrl();
        description = o.getDescription();
        dateStart = o.getDateStart();
        timeStart = o.getTimeStart();
        dateStop = o.getDateStop();
        timeStop = o.getTimeStop();
        placeUrl = o.getPlaceUrl();
        address = o.getAddress();
        city = o.getCity();
        country = o.getCountry();
        imageUrl = o.getImageUrl();
        catégorie = o.getCatégorie();
        dateReal = o.getDateReal();
    }

    public String getDateReal() {return dateReal;}
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getEventUrl(){
        return eventUrl;
    }
    public String getDescription(){
        return description;
    }
    public String getDateStart(){
        return dateStart;
    }
    public String getTimeStart(){
        return timeStart;
    }
    public String getDateStop(){
        return dateStop;
    }
    public String getTimeStop(){
        return timeStop;
    }
    public String getPlaceUrl(){
        return placeUrl;
    }
    public String getAddress(){
        return address;
    }
    public String getCity(){
        return city;
    }
    public String getCountry(){
        return country;
    }
    public String getImageUrl(){
        return imageUrl;
    }
    public String getCatégorie(){
        return catégorie;
    }

    public void setDateReal(String dateReal) {this.dateReal = dateReal;}
    public void setId(String value){
        id = value;
    }
    public void setName(String value){
        name = value;
    }
    public void setEventUrl(String value){
        eventUrl = value;
    }
    public void setDescription(String value){
        description = value;
    }
    public void setDateStart(String value){
        dateStart = value;
    }
    public void setTimeStart(String value){
        timeStart = value;
    }
    public void setDateStop(String value){
        dateStop = value;
    }
    public void setTimeStop(String value){
        timeStop = value;
    }
    public void setPlaceUrl(String value){
        placeUrl = value;
    }
    public void setAddress(String value){
        address = value;
    }
    public void setCity(String value){
        city = value;
    }
    public void setCatégorie(String value){
        catégorie = value;
    }
    public void setCountry(String value){
        country = value;
    }
    public void setImageUrl(String value){
        imageUrl = value;
    }

    @Override
    public String toString() {
        return "Event{" +
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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return id.equals(event.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Event(Parcel in){
        String[] data = new String[14];
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
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
     dest.writeStringArray(new String[]{this.id,this.name,this.catégorie,this.dateStart,this.dateStop,this.description,this.eventUrl,this.imageUrl,this.city,this.country,this.address,this.timeStart,this.timeStop,this.dateReal});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
