package fadhel.iac.tn.eventtracker.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import fadhel.iac.tn.eventtracker.R;

/**
 * Created by Fadhel on 30/03/2016.
 */
public class ConnectionController extends AsyncTask<Void,Void,Boolean> {
    private ProgressDialog progress = null;
    private Context context;

    public ConnectionController(Context context) {this.context=context;}
    @Override
    protected void onPreExecute() {

        progress = ProgressDialog.show(
                context, null, context.getString(R.string.verify_connection));

        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
    return NetworkUtil.hasActiveConnection(context);
    }

    @Override
    protected void onPostExecute(Boolean  res) {
        progress.dismiss();

    }
}
