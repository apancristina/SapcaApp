package com.android.apanc.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.apanc.app.async.task.StartGameAsyncTask;
import com.example.android.apanc.app.R;
import com.android.apanc.app.async.response.AsyncResponse;

/**
 * Created by apanc on 21-May-16.
 */
public class GameFragment extends Fragment implements AsyncResponse<String> {

    public static final String GAME_ID = "com.example.android.apanc.app.GAME_ID";
    private Context context;
    private Button button2Teams;
    private Button button3Teams;
    private Button button4Teams;

    public GameFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        button2Teams = (Button) rootView.findViewById(R.id.button_2_teams);
        button2Teams.setOnClickListener(this);
        button3Teams = (Button) rootView.findViewById(R.id.button_3_teams);
        button3Teams.setOnClickListener(this);
        button4Teams = (Button) rootView.findViewById(R.id.button_4_teams);
        button4Teams.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void processFinish(String result) {
        Intent intent = new Intent(context, GameRoundActivity.class);
        intent.putExtra(GAME_ID, result);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        StartGameAsyncTask startGameAsyncTask = new StartGameAsyncTask(context);
        //this to set comparable/listener back to this class
        startGameAsyncTask.delegate = this;

        switch (view.getId()) {

            case R.id.button_2_teams:
                startGameAsyncTask.execute(button2Teams.getText().toString());
                break;
            case R.id.button_3_teams:
                startGameAsyncTask.execute(button3Teams.getText().toString());
                break;
            case R.id.button_4_teams:
                startGameAsyncTask.execute(button4Teams.getText().toString());
                break;
            default:
                break;
        }
    }
}
