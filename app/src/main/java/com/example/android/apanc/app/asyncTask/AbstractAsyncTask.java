package com.example.android.apanc.app.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.apanc.app.client.SapcaClient;

/**
 * Created by apanc on 22-May-16.
 */
public abstract class AbstractAsyncTask extends AsyncTask<String, Void, String> {

    SapcaClient sapcaClient;
    Context context;
    public AsyncResponse delegate = null;

    public AbstractAsyncTask(Context context) {
        this.context = context;
        this.sapcaClient = new SapcaClient(context);
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    public void setDelegate(AsyncResponse delegate) {
        this.delegate = delegate;
    }
}
