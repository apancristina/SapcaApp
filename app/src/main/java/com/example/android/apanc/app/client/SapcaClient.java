package com.example.android.apanc.app.client;

import android.content.Context;

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
        return executeCall(buildGameRequest("game", gameId));
    }

    public String nextRound(String gameId) throws IOException {
        return executeCall(buildGameRequest("nextRound", gameId));
    }

    public String addPoints(String gameId) throws IOException {
        return executeCall(buildGameRequest("addPoints", gameId));
    }

    public String getWebping() throws IOException {
        return executeCall(buildGameRequest("webping"));
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

    private String readResponse(InputStream inputStream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    private Request buildGameRequest(String... params) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(context.getString(R.string.sapca_url)).newBuilder();
        for (String p : params) {
            urlBuilder.addPathSegment(p);
        }
        String url = urlBuilder.build().toString();
        return new Request.Builder()
                .url(url)
                .build();
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
}
