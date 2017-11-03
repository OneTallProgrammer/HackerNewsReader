package com.onetallprogrammer.hackernewsreader.webinterface;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.onetallprogrammer.hackernewsreader.model.Story;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;

public class GetExtraStoryTask extends AsyncTask<String, Void, String> {
    private GetExtraStoryResponse delegate;
    private Story newStory = null;

    @Override
    protected String doInBackground(String... urls) {

        try {

            URL url = new URL(urls[0]);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = connection.getInputStream();

            InputStreamReader reader = new InputStreamReader(inputStream);

            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuilder builder = new StringBuilder();

            String line;
            while((line = bufferedReader.readLine()) != null){

                builder.append(line);

            }

            JsonParser parser = new JsonParser();

            JsonElement element = parser.parse(builder.toString());

            if(element.isJsonObject()){

                JsonObject jsonObject = element.getAsJsonObject();

                if(jsonObject.has("title") && jsonObject.has("url")){

                    String title = jsonObject.get("title").toString();

                    String link = jsonObject.get("url").toString();

                    newStory = new Story(title, link);

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        delegate.getExtraStoryFinish(newStory);

    }

    public void setDelegate(GetExtraStoryResponse delegate) {
        this.delegate = delegate;
    }
}
