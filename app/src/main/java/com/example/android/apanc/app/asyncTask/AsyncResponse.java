package com.example.android.apanc.app.asyncTask;

import android.view.View;

/**
 * Created by apanc on 22-May-16.
 */
public interface AsyncResponse extends View.OnClickListener {
    void processFinish(String output);
}
