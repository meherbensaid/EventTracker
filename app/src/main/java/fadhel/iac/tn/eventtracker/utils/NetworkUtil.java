package fadhel.iac.tn.eventtracker.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Fadhel on 30/03/2016.
 */
public class NetworkUtil {
    public static int CONNECTED = 1;
    public static int DISCONNECTED = 0 ;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNet = cm.getActiveNetworkInfo();
        if(activeNet!=null)
            if(activeNet.getType()==ConnectivityManager.TYPE_WIFI || activeNet.getType()==ConnectivityManager.TYPE_MOBILE)
                return CONNECTED;
        return DISCONNECTED;
    }

    public static boolean hasActiveConnection(Context context) {
        if(getConnectivityStatus(context)==1){
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
            } catch (IOException e) {
                Log.e("Connection", "Error checking internet connection", e);
            }
        } else {
            Log.d("Connection", "No network available!");
        }
        return false;
    }
}