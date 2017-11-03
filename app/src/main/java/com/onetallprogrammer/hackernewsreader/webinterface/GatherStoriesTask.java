package com.onetallprogrammer.hackernewsreader.webinterface;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.onetallprogrammer.hackernewsreader.model.Story;
import com.onetallprogrammer.hackernewsreader.ui.StorySelectionActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class GatherStoriesTask extends AsyncTask<String, Void, String> {
    private final int INITIAL_STORIES_DISPLAYED = 25;
    private GatherStoriesResponse delegate = null;
    private ArrayList<Story> stories = new ArrayList<>();
    private JsonArray storyIds = new JsonArray();
    private int storiesToDisplay = INITIAL_STORIES_DISPLAYED;


    @Override
    protected String doInBackground(String... urls) {

        JsonParser parser = new JsonParser();

        String rawJsonAsString = getJsonAsString(urls[0]);

        JsonElement element = parser.parse(rawJsonAsString);

        if(element.isJsonArray()){

            storyIds = element.getAsJsonArray();

        } else {

            return null;

        }

        if(storyIds.size() < INITIAL_STORIES_DISPLAYED){
            storiesToDisplay = storyIds.size();
        }

        for(int i = 0; i < storiesToDisplay; i++){

            String url = String.format(StorySelectionActivity.API_CALL, "item/" + storyIds.get(i));

            rawJsonAsString = getJsonAsString(url);

            element = parser.parse(rawJsonAsString);

            if(element.isJsonObject()){

                JsonObject jsonObject = element.getAsJsonObject();


                if(jsonObject.has("title") && jsonObject.has("url")) {

                    String title = jsonObject.get("title").toString();

                    String link = jsonObject.get("url").toString();

                    stories.add(new Story(title, link));

                }


            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        delegate.gatherStoriesFinish(stories, storyIds, storiesToDisplay);

    }

    public String getJsonAsString(String stringUrl){

        URL url;

        HttpURLConnection connection;

        try {

            url = new URL(stringUrl);

            connection = (HttpURLConnection) url.openConnection();

            StringBuilder builder = new StringBuilder();

            InputStream inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = reader.readLine()) != null){

                builder.append(line);

            }

            return builder.toString();


        } catch (IOException e) {

            e.printStackTrace();

        }

        return null;

    }

    public void setDelegate(GatherStoriesResponse delegate) {
        this.delegate = delegate;
    }
}

