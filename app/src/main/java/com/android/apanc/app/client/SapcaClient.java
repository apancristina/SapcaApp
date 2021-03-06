package com.android.apanc.app.client;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.JsonReader;

import com.android.apanc.app.model.Round;
import com.android.apanc.app.model.Team;
import com.example.android.apanc.app.R;
import com.android.apanc.app.model.Game;

import org.apache.http.client.HttpResponseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by apanc on 22-May-16.
 */
public class SapcaClient {
    private final OkHttpClient client;
    private final Context context;

    public SapcaClient(Context context) {
        client = new OkHttpClient();
        this.context = context;
    }

    //TODO: validations
    //TODO: extract strings in constants or context

    public String startGame(String nrOfTeams) throws IOException {
        return executeCall(buildStartGameRequest(nrOfTeams));
    }

    public Game getGame(String gameId) throws IOException {
        return getGameDetails(buildGameDetailsRequest(gameId));
    }

    public Round nextRound(String gameId) throws IOException {
        return getRound(buildNextRoundRequest(gameId));
    }

    public String addPoints(String teamId, String points) throws IOException {
        Response response = null;
        try {
            response = client.newCall(buildAddPointsRequest(teamId, points)).execute();
            if (response.isSuccessful()) {
                return readResponse(response.body().byteStream());
            } else {
                throw new HttpResponseException(response.code(), "Failed to retrieve game details :(");
            }
        } catch (IOException e) {
            //Log.e(TAG, "doInBack", e); // show to user
            throw e;
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
    }

    public String getWebping() throws IOException {
        return executeCall(buildGameDetailsRequest("webping"));
    }

    //TODO handle failure more pleasantly
    private String executeCall(Request request) throws IOException {
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return readResponse(response.body().byteStream());
            } else {
                throw new HttpResponseException(response.code(), "Failed to retrieve game details :(");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
    }

    private Round getRound(Request request) throws IOException {
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return readRound(response.body().byteStream());
            } else {
                throw new HttpResponseException(response.code(), "Failed to retrieve game details :(");
            }
        } catch (IOException e) {
            //Log.e(TAG, "doInBack", e); // show to user
            throw e;
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
    }

    private Game getGameDetails(Request request) throws IOException {
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return readGame(response.body().byteStream());
            } else {
                throw new HttpResponseException(response.code(), "Failed to retrieve game details :(");
            }
        } catch (IOException e) {
            //Log.e(TAG, "doInBack", e); // show to user
            throw e;
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private Game readGame(InputStream inputStream) throws IOException {
        Game game = new Game();
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    game.setId(reader.nextString());
                    break;
                case "teams":
                    game.setTeams(readTeams(reader));
                    break;
                default:
                    break;
            }
        }
        reader.endObject();
        reader.close();
        return game;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private List<Team> readTeams(JsonReader reader) throws IOException {
        List<Team> teams = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            teams.add(readTeam(reader));
        }
        reader.endArray();
        return teams;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private Round readRound(InputStream inputStream) throws IOException {
        Round round = new Round();
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        reader.beginObject();
        while (reader.hasNext()) { // todo instances exist
            String name = reader.nextName();
            switch (name) {
                case "text":
                    round.setText(reader.nextString());
                    break;
                case "points":
                    round.setPoints(reader.nextString());
                    break;
                case "options":
                    round.setOptions(reader.nextString());
                    break;
                case "team":
                    round.setTeam(readTeam(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        reader.close();
        return round;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private Team readTeam(JsonReader reader) throws IOException {
        Team team = new Team();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    team.setId(reader.nextString());
                    break;
                case "color":
                    team.setColor(reader.nextString());
                    break;
                case "points":
                    team.setPoints(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return team;
    }

    private String readResponse(InputStream inputStream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    private Request buildStartGameRequest(String... params) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(context.getString(R.string.sapca_url)).newBuilder();
        urlBuilder.addPathSegment("startGame");
        urlBuilder.addQueryParameter("noOfTeams", params[0]);
        String url = urlBuilder.build().toString();
        return new Request.Builder()
                .url(url)
                .build();
    }

    private Request buildGameDetailsRequest(String... params) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(context.getString(R.string.sapca_url)).newBuilder();
        urlBuilder.addPathSegment("game");
        urlBuilder.addPathSegment(params[0]);
        String url = urlBuilder.build().toString();
        return new Request.Builder()
                .url(url)
                .build();
    }

    private Request buildNextRoundRequest(String... params){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(context.getString(R.string.sapca_url)).newBuilder();
        urlBuilder.addPathSegment("nextRound");
        urlBuilder.addPathSegment(params[0]);
        String url = urlBuilder.build().toString();
        return new Request.Builder()
                .url(url)
                .build();
    }

    private Request buildAddPointsRequest(String... params) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(context.getString(R.string.sapca_url)).newBuilder();
        urlBuilder.addPathSegment("addPoints");
        urlBuilder.addPathSegment(params[0]);
        urlBuilder.addQueryParameter("points", params[1]);
        String url = urlBuilder.build().toString();
        return new Request.Builder()
                .url(url)
                .build();
    }
}
