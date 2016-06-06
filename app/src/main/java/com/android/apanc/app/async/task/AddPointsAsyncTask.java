package com.android.apanc.app.async.task;

import android.content.Context;

import com.android.apanc.app.GameRoundActivityFragment;

import java.io.IOException;

/**
 * Created by apanc on 26-May-16.
 */
public class AddPointsAsyncTask extends AbstractAsyncTask<String> {

    public GameRoundActivityFragment fragment;

    public AddPointsAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = "";
        try {
            if (strings.length != 0) {
                response = sapcaClient.addPoints(strings[0], strings[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        fragment.finishAddPoints(response);
    }
}
