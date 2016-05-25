package com.example.android.apanc.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.apanc.app.Model.Round;
import com.example.android.apanc.app.async.response.AsyncResponse;
import com.example.android.apanc.app.async.task.NextRoundAsyncTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class GameRoundActivityFragment extends Fragment implements AsyncResponse<Round> {

    private Context context;
    private TextView teamDetails;
    private TextView roundDetailsTextView;

    public GameRoundActivityFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_round, container, false);

        Intent intent = getActivity().getIntent();
        String gameId = intent.getStringExtra(GameFragment.GAME_ID);

        TextView textView = (TextView) rootView.findViewById(R.id.testViewGameId);
        textView.setText(gameId);
        roundDetailsTextView = (TextView) rootView.findViewById(R.id.ROUND_DETAILS);

        //get next round
        NextRoundAsyncTask nextRoundAsyncTask = new NextRoundAsyncTask(context);
        nextRoundAsyncTask.delegate = this;
        nextRoundAsyncTask.execute(gameId);

        return rootView;
    }

    @Override
    public void processFinish(Round round) {
        //collect next round response here
        String displayMessage = round.getText() + "\n" + round.getOptions() + "\n" + round.getRoundPoints() + "\n" + round.getTeamColour();
        roundDetailsTextView.setText(displayMessage);
    }

    @Override
    public void onClick(View view) {
        //put actions on click here
    }
}
