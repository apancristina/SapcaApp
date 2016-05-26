package com.example.android.apanc.app.client;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.JsonReader;

import com.example.android.apanc.app.Model.Round;
import com.example.android.apanc.app.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;
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

    public String getGame(String gameId) throws IOException {
        return executeCall(buildGameDetailsRequest(gameId));
    }

    public Round nextRound(String gameId) throws IOException {
        return getRound(buildNextRoundRequest(gameId));
    }

    public String addPoints(String teamId, String points) throws IOException {
        Response response;
        Call call = null;
        try {
            call = client.newCall(buildAddPointsRequest(teamId, points));
            response = call.execute();
            return readResponse(response.body().byteStream());
        } catch (IOException e) {
            //Log.e(TAG, "doInBack", e); // show to user
            throw e;
        } finally {
            if (call != null) {
                //call.
            }
        }
    }

    public String getWebping() throws IOException {
        return executeCall(buildGameDetailsRequest("webping"));
    }

    //TODO handle failure more pleasantly
    private String executeCall(Request request) throws IOException {
        Response response;
        Call call = null;
        try {
            call = client.newCall(request);
            response = call.execute();
            return readResponse(response.body().byteStream());
        } catch (IOException e) {
            //Log.e(TAG, "doInBack", e); // show to user
            throw e;
        } finally {
            if (call != null) {
                //call.
            }
        }
    }

    private Round getRound(Request request) throws IOException {
        Response response;
        Call call = null;
        try {
            call = client.newCall(request);
            response = call.execute();
            if (response.isSuccessful()) {
                return readRound(response.body().byteStream());
            } else {
                response.body().close();
            }
        } catch (IOException e) {
            //Log.e(TAG, "doInBack", e); // show to user
            throw e;
        } finally {
            if (call != null) {
            }
        }
        return null;
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
                    round.setRoundPoints(reader.nextString());
                    break;
                case "options":
                    round.setOptions(reader.nextString());
                    break;
                case "team":
                    round = readTeam(reader, round);
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
    private Round readTeam(JsonReader reader, Round round) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    round.setTeamId(reader.nextString());
                    break;
                case "color":
                    round.setTeamColour(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return round;
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
