package com.example.android.apanc.app.asyncTask;

import android.content.Context;

import java.io.IOException;

/**
 * Created by apanc on 22-May-16.
 */
public class GameDetailsAsycTask extends AbstractAsyncTask {

    public GameDetailsAsycTask(Context context) {
        super(context);
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = "";
        try {
            if (strings.length != 0) {
                response = sapcaClient.getGame(strings[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
