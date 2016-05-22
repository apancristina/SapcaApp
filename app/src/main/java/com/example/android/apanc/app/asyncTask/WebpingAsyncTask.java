package com.example.android.apanc.app.asyncTask;

import android.content.Context;

import java.io.IOException;

/**
 * Created by apanc on 22-May-16.
 */
public class WebpingAsyncTask extends AbstractAsyncTask {

    public WebpingAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = "";
        try {
            response = sapcaClient.getWebping();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
