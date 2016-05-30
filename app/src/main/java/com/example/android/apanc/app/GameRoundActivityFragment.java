package com.example.android.apanc.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.apanc.app.async.response.AsyncResponse;
import com.example.android.apanc.app.async.task.AddPointsAsyncTask;
import com.example.android.apanc.app.async.task.GameDetailsAsyncTask;
import com.example.android.apanc.app.async.task.NextRoundAsyncTask;
import com.example.android.apanc.app.model.Game;
import com.example.android.apanc.app.model.Round;
import com.example.android.apanc.app.model.Team;

import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class GameRoundActivityFragment extends Fragment implements AsyncResponse<Round> {

    private static final String ZERO_POINTS = "0";
    private Context context;
    private Round currentRound;
    private Button correct;
    private Button wrong;
    private String gameId;
    private LinearLayout progressBarLinearLayout;
    private Map<String, ProgressBar> progressBars = new HashMap<>(4);
    private boolean unlocked = true;

    private TextView currentTeamTextView;
    private TextView roundQuestionTextView;
    private TextView roundPointsTextView;
    private ImageView mime;
    private ImageView talk;
    private ImageView draw;
    private TextView timer;

    public GameRoundActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_game_round, container, false);

        correct = (Button) rootView.findViewById(R.id.correctButton);
        correct.setOnClickListener(this);
        wrong = (Button) rootView.findViewById(R.id.wrongButton);
        wrong.setOnClickListener(this);

        Intent intent = getActivity().getIntent();
        gameId = intent.getStringExtra(GameFragment.GAME_ID);

        currentTeamTextView = (TextView) rootView.findViewById(R.id.currentTeam);
        roundQuestionTextView = (TextView) rootView.findViewById(R.id.roundQuestion);
        roundPointsTextView = (TextView) rootView.findViewById(R.id.points);

        mime = (ImageView) rootView.findViewById(R.id.mime);
        talk = (ImageView) rootView.findViewById(R.id.talk);
        draw = (ImageView) rootView.findViewById(R.id.draw);

        timer = (TextView) rootView.findViewById(R.id.timer);
        timer.setText("timer is here");

        progressBarLinearLayout = (LinearLayout) rootView.findViewById(R.id.progressLinearLayout);

        getGameDetails();
        getNextRound();

        return rootView;
    }

    private void getGameDetails() {
        GameDetailsAsyncTask gameDetailsAsyncTask = new GameDetailsAsyncTask(context);
        gameDetailsAsyncTask.fragment = this;
        gameDetailsAsyncTask.execute(gameId);
    }

    @Override
    public void onClick(View view) {
        if (unlocked) {
            unlocked = false;
            AddPointsAsyncTask addPointsAsyncTask = new AddPointsAsyncTask(context);
            addPointsAsyncTask.fragment = this;
            if (currentRound != null) {
                switch (view.getId()) {

                    case R.id.correctButton:
                        // TODO: 29-May-16 Move integer.valueof in model classes
                        progressBars.get(currentRound.getTeam().getId()).incrementProgressBy(Integer.valueOf(currentRound.getPoints()));
                        addPointsAsyncTask.execute(currentRound.getTeam().getId(), currentRound.getPoints());
                        break;
                    case R.id.wrongButton:
                        addPointsAsyncTask.execute(currentRound.getTeam().getId(), ZERO_POINTS);
                        break;
                    default:
                        break;
                }
            }
        }
        //put actions on click here
    }

    @Override
    public void processFinish(Round round) {
        if (round != null) {
            mime.setVisibility(View.INVISIBLE);
            talk.setVisibility(View.INVISIBLE);
            draw.setVisibility(View.INVISIBLE);

            currentRound = round;
            String displayMessage = round.getTeam().getColor() + "'s turn";
            currentTeamTextView.setText(displayMessage);
            roundQuestionTextView.setText(round.getText());
            roundPointsTextView.setText(round.getPoints());
            String options = round.getOptions();
            switch (options) {
                case "mima":
                    mime.setVisibility(View.VISIBLE);
                    break;
                case "deseneaza, mimeaza, vorbeste": {
                    talk.setVisibility(View.VISIBLE);
                }
                case "anything":
                    mime.setVisibility(View.VISIBLE);
                    talk.setVisibility(View.VISIBLE);
                    draw.setVisibility(View.VISIBLE);
            }

            //setBackgroundColor(round);
        } else {
            currentTeamTextView.setText("No Current Team -> nextRound failed");
        }
        unlocked = true;

    }

    private void setBackgroundColor(Round round) {
        switch (round.getTeam().getColor()) {
            case "blue":
                int blue = Color.rgb(106, 186, 252);
                currentTeamTextView.setBackgroundColor(blue);
                break;
            case "green":
                int green = Color.rgb(95, 223, 75);
                currentTeamTextView.setBackgroundColor(green);
                break;
            default:
                break;
        }
    }

    public void finishAddPoints(String response) {
        if ("false".equals(response)) {
            getNextRound();
        } else {
            //Toast.makeText(getActivity(), "Team " + currentRound.getTeamId() + " won!", Toast.LENGTH_SHORT).show();
            String text = "Team " + currentRound.getTeam().getId() + " won!";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.FILL_HORIZONTAL, Gravity.FILL_VERTICAL, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
            // TODO: 29-May-16 new intent --> got to main activity
        }
    }

    public void finishProcessGameDetails(Game game){
        currentTeamTextView.append(game.getId());
        if (progressBars.isEmpty()) {
            RelativeLayout.LayoutParams progressBarParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            for (Team team : game.getTeams()) {
                ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(false);
                progressBar.setBackgroundColor(Color.rgb(193, 189, 204));
                progressBar.setLayoutParams(progressBarParams);
                progressBar.setMax(25);
                progressBarLinearLayout.addView(progressBar);
                progressBars.put(team.getId(), progressBar);
            }
        }

    }

    private void getNextRound() {
        NextRoundAsyncTask nextRoundAsyncTask = new NextRoundAsyncTask(context);
        nextRoundAsyncTask.delegate = this;
        nextRoundAsyncTask.execute(gameId);
    }
}
