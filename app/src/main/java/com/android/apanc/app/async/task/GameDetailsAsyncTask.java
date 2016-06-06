package com.android.apanc.app.async.task;

import android.content.Context;

import com.android.apanc.app.GameRoundActivityFragment;
import com.android.apanc.app.model.Game;

import java.io.IOException;

/**
 * Created by apanc on 22-May-16.
 */
public class GameDetailsAsyncTask extends AbstractAsyncTask<Game> {

    public GameRoundActivityFragment fragment;
    public GameDetailsAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected Game doInBackground(String... strings) {
        Game response = null;
        try {
            if (strings.length != 0) {
                response = sapcaClient.getGame(strings[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(Game response) {

        fragment.finishProcessGameDetails(response);
    }
}
