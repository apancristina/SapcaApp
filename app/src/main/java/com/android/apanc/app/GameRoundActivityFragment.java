package com.android.apanc.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.apanc.app.async.response.AsyncResponse;
import com.android.apanc.app.async.task.AddPointsAsyncTask;
import com.android.apanc.app.async.task.GameDetailsAsyncTask;
import com.android.apanc.app.async.task.NextRoundAsyncTask;
import com.android.apanc.app.model.Game;
import com.android.apanc.app.model.Round;
import com.android.apanc.app.model.Team;
import com.example.android.apanc.app.R;

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
    private boolean timerHasStarted = false;

    private TextView currentTeamTextView;
    private TextView roundQuestionTextView;
    private TextView roundPointsTextView;
    private ImageView mime;
    private ImageView talk;
    private ImageView draw;
    private TextView timerTextView;
    private final long startTime = 60 * 1000;
    private Button timerButton;
    private CountDownTimer countDownTimer;
    private Ringtone timerRingtone;
    private ProgressBar pbRed;
    private ProgressBar pbBlue;
    private ProgressBar pbGreen;
    private ProgressBar pbYellow;

    public GameRoundActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_game_round, container, true);

        Intent intent = getActivity().getIntent();
        gameId = intent.getStringExtra(GameFragment.GAME_ID);

        correct = (Button) rootView.findViewById(R.id.correctButton);
        correct.setOnClickListener(this);
        wrong = (Button) rootView.findViewById(R.id.wrongButton);
        wrong.setOnClickListener(this);

        pbRed = (ProgressBar) rootView.findViewById(R.id.progressBarRed);
        pbRed.setVisibility(View.INVISIBLE);
        pbBlue = (ProgressBar) rootView.findViewById(R.id.progressBarBlue);
        pbBlue.setVisibility(View.INVISIBLE);
        pbGreen = (ProgressBar) rootView.findViewById(R.id.progressBarGreen);
        pbGreen.setVisibility(View.INVISIBLE);
        pbYellow = (ProgressBar) rootView.findViewById(R.id.progressBarYellow);
        pbYellow.setVisibility(View.INVISIBLE);


        currentTeamTextView = (TextView) rootView.findViewById(R.id.currentTeam);
        roundQuestionTextView = (TextView) rootView.findViewById(R.id.roundQuestion);
        roundPointsTextView = (TextView) rootView.findViewById(R.id.points);

        mime = (ImageView) rootView.findViewById(R.id.mime);
        talk = (ImageView) rootView.findViewById(R.id.talk);
        draw = (ImageView) rootView.findViewById(R.id.draw);
        mime.setVisibility(View.INVISIBLE);
        talk.setVisibility(View.INVISIBLE);
        draw.setVisibility(View.INVISIBLE);

        timerButton = (Button) rootView.findViewById(R.id.timerButton);
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickTimer(view);
            }
        });
        timerTextView = (TextView) rootView.findViewById(R.id.timer);

        Uri timerSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        timerRingtone = RingtoneManager.getRingtone(context, timerSound);

        countDownTimer = new CountDownTimer(startTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                String text = "" + millisUntilFinished / 1000;
                timerTextView.setText(text);
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Time's up!");
                timerButton.setClickable(false);
                timerRingtone.play();
                unlocked = true;

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (timerRingtone.isPlaying())
                            timerRingtone.stop();
                    }
                }, 6000);
            }

        };
        timerTextView.setText(String.valueOf(startTime / 1000));

        //progressBarLinearLayout = (LinearLayout) rootView.findViewById(R.id.progressLinearLayout);


        getGameDetails();
        getNextRound();

        return rootView;
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
//                        progressBars.get(currentRound.getTeam().getId()).incrementProgressBy(Integer.valueOf(currentRound.getPoints()));
                        String teamColor = currentRound.getTeam().getColor().toUpperCase();
                        switch (teamColor) {
                            case "RED":
                                pbRed.incrementProgressBy(Integer.valueOf(currentRound.getPoints()));
                                break;
                            case "BLUE":
                                pbBlue.incrementProgressBy(Integer.valueOf(currentRound.getPoints()));
                                break;
                            case "GREEN":
                                pbGreen.incrementProgressBy(Integer.valueOf(currentRound.getPoints()));
                                break;
                            case "YELLOW":
                                pbYellow.incrementProgressBy(Integer.valueOf(currentRound.getPoints()));
                                break;
                            default:
                                break;

                        }
                        addPointsAsyncTask.execute(currentRound.getTeam().getId(), currentRound.getPoints());
                        timerButton.setText("START");
                        break;
                    case R.id.wrongButton:
                        addPointsAsyncTask.execute(currentRound.getTeam().getId(), ZERO_POINTS);
                        timerButton.setText("START");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void onClickTimer(View view) {
        unlocked = false;
        if (!timerHasStarted) {
            countDownTimer.start();
            timerHasStarted = true;
            timerButton.setText("STOP");
        } else {
            countDownTimer.cancel();
            timerTextView.setText(String.valueOf(startTime / 1000));
            timerHasStarted = false;
            timerButton.setText("RESTART");
            unlocked = true;
        }
    }

    @Override
    public void processFinish(Round round) {
        if (round != null) {
            timerButton.setClickable(true);
            mime.setVisibility(View.INVISIBLE);
            talk.setVisibility(View.INVISIBLE);
            draw.setVisibility(View.INVISIBLE);

            currentRound = round;
            String currentTeamColor = round.getTeam().getColor().toUpperCase();
            styleCurrentTeam(currentTeamColor);
            roundQuestionTextView.setText(round.getText());
            roundPointsTextView.setText(round.getPoints());

            String options = round.getOptions();
            switch (options) {
                case "MIMA":
                    mime.setVisibility(View.VISIBLE);
                    break;
                case "VORBESTE": {
                    talk.setVisibility(View.VISIBLE);
                    break;
                }
                case "DESENEAZA": {
                    draw.setVisibility(View.VISIBLE);
                    break;

                }
                case "TOTUL_ESTE_POSIBIL":
                    mime.setVisibility(View.VISIBLE);
                    talk.setVisibility(View.VISIBLE);
                    draw.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            currentTeamTextView.setText("Failed to retrieve next round information.");
        }
        unlocked = true;

    }

    private void getGameDetails() {
        GameDetailsAsyncTask gameDetailsAsyncTask = new GameDetailsAsyncTask(context);
        gameDetailsAsyncTask.fragment = this;
        gameDetailsAsyncTask.execute(gameId);
    }

    private void styleCurrentTeam(String color) {
        String teamText = color + " TEAM";
        currentTeamTextView.setText(teamText);
        switch (color) {
            case "RED":
                currentTeamTextView.setTextColor(Color.rgb(204, 23, 23));
                break;
            case "BLUE":
                currentTeamTextView.setTextColor(Color.rgb(29, 29, 255));
                break;
            case "GREEN":
                currentTeamTextView.setTextColor(Color.rgb(65, 162, 80));
                break;
            case "YELLOW":
                currentTeamTextView.setTextColor(Color.rgb(208, 218, 20));
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
            String text = currentRound.getTeam().getColor().toUpperCase() + " team has won!";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.FILL_HORIZONTAL, Gravity.FILL_VERTICAL, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
            // TODO: 29-May-16 new intent --> got to main activity
        }
    }

    public void finishProcessGameDetails(Game game){
        /*if (progressBars.isEmpty()) {
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
        }*/
        if (game != null) {
            for (Team team : game.getTeams()) {
                switch (team.getColor().toUpperCase()) {
                    case "RED":
                        pbRed.setVisibility(View.VISIBLE);
                        break;
                    case "BLUE":
                        pbBlue.setVisibility(View.VISIBLE);
                        break;
                    case "GREEN":
                        pbGreen.setVisibility(View.VISIBLE);
                        break;
                    case "YELLOW":
                        pbYellow.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;

                }
            }
        }

    }

    private void getNextRound() {
        NextRoundAsyncTask nextRoundAsyncTask = new NextRoundAsyncTask(context);
        nextRoundAsyncTask.delegate = this;
        nextRoundAsyncTask.execute(gameId);
    }
}
