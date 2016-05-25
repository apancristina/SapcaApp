package com.example.android.apanc.app.async.response;

import android.view.View;

/**
 * Created by apanc on 22-May-16.
 */
public interface AsyncResponse<T> extends View.OnClickListener {
    void processFinish(T output);
}
