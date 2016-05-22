package com.example.android.apanc.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class GameRoundActivityFragment extends Fragment {

    public GameRoundActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_round, container, false);

        Intent intent = getActivity().getIntent();
        String gameId = intent.getStringExtra(GameFragment.GAME_ID);

        TextView textView = (TextView) rootView.findViewById(R.id.testViewGameId);
        textView.setText(gameId);
        return rootView;
    }
}
