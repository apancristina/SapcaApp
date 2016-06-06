package com.android.apanc.app.async.task;

import android.content.Context;

import com.android.apanc.app.model.Round;

import java.io.IOException;

/**
 * Created by apanc on 25-May-16.
 */
public class NextRoundAsyncTask extends AbstractAsyncTask<Round>  {

    public NextRoundAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected Round doInBackground(String... strings) {
        Round round = null;
        try {
            if (strings.length != 0) {
                round = sapcaClient.nextRound(strings[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return round;
    }

    @Override
    protected void onPostExecute(Round response) {
        delegate.processFinish(response);
    }
}
