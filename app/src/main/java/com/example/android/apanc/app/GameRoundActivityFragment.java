package com.example.android.apanc.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.apanc.app.Model.Round;
import com.example.android.apanc.app.async.response.AsyncResponse;
import com.example.android.apanc.app.async.task.AddPointsAsyncTask;
import com.example.android.apanc.app.async.task.NextRoundAsyncTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class GameRoundActivityFragment extends Fragment implements AsyncResponse<Round> {

    private static final String ZERO_POINTS = "0";
    private static final String TEAM_ID = "com.example.android.apanc.app.TEAM_ID";
    private Context context;
    private TextView roundDetailsTextView;
    private Round currentRound;
    private Button correct;
    private Button wrong;
    private TextView textView;
    private NextRoundAsyncTask nextRoundAsyncTask;
    private String gameId;


    public GameRoundActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_game_round, container, false);

        correct = (Button) rootView.findViewById(R.id.CORRECT_BUTTON);
        correct.setOnClickListener(this);
        wrong = (Button) rootView.findViewById(R.id.WRONG_BUTTON);
        wrong.setOnClickListener(this);

        Intent intent = getActivity().getIntent();
        gameId = intent.getStringExtra(GameFragment.GAME_ID);

        extractViewItems(rootView, gameId);

        //get next round
        nextRoundAsyncTask = new NextRoundAsyncTask(context);
        nextRoundAsyncTask.delegate = this;
        nextRoundAsyncTask.execute(gameId);

        return rootView;
    }

    private void extractViewItems(View rootView, String gameId) {
        textView = (TextView) rootView.findViewById(R.id.testViewGameId);

        textView.setText(gameId);
        roundDetailsTextView = (TextView) rootView.findViewById(R.id.ROUND_DETAILS);
    }

    @Override
    public void processFinish(Round round) {
        //collect next round response here
        if (currentRound != null) {
            currentRound = round;
        }
        String displayMessage;

        if (round != null) {
            displayMessage = round.getText() + "\n" + round.getOptions() + "\n" + round.getRoundPoints() + "\n" + round.getTeamColour();
        } else {
            displayMessage = "Nothing to see here :(";
        }
        roundDetailsTextView.setText(displayMessage);
    }

    @Override
    public void onClick(View view) {
        //put actions on click here
        AddPointsAsyncTask addPointsAsyncTask = new AddPointsAsyncTask(context);
        addPointsAsyncTask.fragment = this;

        switch (view.getId()) {

            case R.id.CORRECT_BUTTON:
                addPointsAsyncTask.execute(currentRound.getTeamId(), currentRound.getRoundPoints());
                break;
            case R.id.WRONG_BUTTON:
                addPointsAsyncTask.execute(currentRound.getTeamId(), ZERO_POINTS);
                break;
            default:
                break;
        }
    }

    public void finishAddpoints(String response) {
        //// TODO: 26-May-16 Verify if game is completed
        textView.setText(response);
        NextRoundAsyncTask nextRound = new NextRoundAsyncTask(context);
        nextRound.delegate = this;
        nextRound.execute(gameId);
    }
}
