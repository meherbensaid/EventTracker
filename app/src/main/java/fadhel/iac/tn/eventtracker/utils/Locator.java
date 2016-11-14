package fadhel.iac.tn.eventtracker.utils;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import fadhel.iac.tn.eventtracker.R;

/**
 * Created by Fadhel on 22/03/2016.
 */
public class Locator extends AsyncTask<String, Void, String> {
    private ProgressDialog progress = null;
    private Context context;

    public Locator(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

        progress = ProgressDialog.show(
                context, null, context.getString(R.string.locating_device));

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        double longitude = 0, latitude = 0;
        Location detectedlocation = null;
        String location="";
        LocationManager locationManager;
        try {
            locationManager = (LocationManager)
                    context.getSystemService(context.LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
                    locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, (LocationListener) context, Looper.getMainLooper());
                    Log.d("Network", "Network Enabled");
                    if (locationManager != null) {
                        Log.d("Location Manager","not null");
                        detectedlocation = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (detectedlocation != null) {
                            latitude = detectedlocation.getLatitude();
                            longitude = detectedlocation.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (detectedlocation == null) {
                        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,(LocationListener)context,Looper.getMainLooper());
                        Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            Log.d("Location Manager","not null");
                            detectedlocation = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (detectedlocation != null) {
                                latitude = detectedlocation.getLatitude();
                                longitude = detectedlocation.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Longitude",longitude+"");
        Log.d("Latitude",latitude+"");
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {

            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(!addresses.isEmpty()) {
                location += addresses.get(0).getAdminArea() + ",";
                location += addresses.get(0).getLocality() + ",";
                location += addresses.get(0).getCountryName();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    protected void onPostExecute(String  res) {
        progress.dismiss();

    }
}