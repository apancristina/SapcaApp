package com.android.apanc.app.async.task;

import android.content.Context;
import android.os.AsyncTask;

import com.android.apanc.app.async.response.AsyncResponse;
import com.android.apanc.app.client.SapcaClient;

/**
 * Created by apanc on 22-May-16.
 */
public abstract class AbstractAsyncTask<T> extends AsyncTask<String, Void, T> {

    protected SapcaClient sapcaClient;
    protected Context context;
    public AsyncResponse<T> delegate = null;

    public AbstractAsyncTask(Context context) {
        this.context = context;
        this.sapcaClient = new SapcaClient(context);
    }

    @Override
    protected void onPostExecute(T response) {
        delegate.processFinish(response);
    }


}
