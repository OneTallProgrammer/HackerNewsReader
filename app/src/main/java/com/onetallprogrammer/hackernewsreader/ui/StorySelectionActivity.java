package com.onetallprogrammer.hackernewsreader.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.onetallprogrammer.hackernewsreader.R;
import com.onetallprogrammer.hackernewsreader.model.Story;
import com.onetallprogrammer.hackernewsreader.webinterface.GatherStoriesResponse;
import com.onetallprogrammer.hackernewsreader.webinterface.GatherStoriesTask;
import com.onetallprogrammer.hackernewsreader.webinterface.GetExtraStoryResponse;
import com.onetallprogrammer.hackernewsreader.webinterface.GetExtraStoryTask;

import java.util.ArrayList;

public class StorySelectionActivity extends AppCompatActivity implements GatherStoriesResponse, GetExtraStoryResponse {

    public final static String API_CALL = "https://hacker-news.firebaseio.com/v0/%s.json?print=pretty"; // must format

    private ArrayList<Story> stories = new ArrayList<>();
    private RelativeLayout loadingPanel;
    private ArrayAdapter<Story> storiesAdapter;
    private JsonArray storyIds = new JsonArray();
    private int storyIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_selection);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);

        final ListView storyListView = (ListView) findViewById(R.id.storyListView);

        getNewStories("topstories"); // default displayed in reader

        storyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), ReaderActivity.class);

                intent.putExtra("url", stories.get(i).url);

                startActivity(intent);

            }
        });

        storyListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

                if(storyListView.getCount() == storyListView.getLastVisiblePosition() + 1){

                    loadingPanel.setVisibility(View.VISIBLE);

                    for(int counter = 0; counter < 3; counter++) {

                        GetExtraStoryTask getExtraStoryTask = new GetExtraStoryTask();

                        getExtraStoryTask.setDelegate(StorySelectionActivity.this);

                        getExtraStoryTask.execute(formatApiCall("item/" + storyIds.get(storyIndex).toString()));

                        storyIndex++;
                    }





                }

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);

        inflater.inflate(R.menu.selector_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView storyTypeLabel = (TextView) findViewById(R.id.storyTypeLabel);

        switch(item.getItemId()){
            case R.id.topStoriesMenuItem:
                getNewStories("topstories");
                storyTypeLabel.setText(R.string.top_stories);
                break;
            case R.id.bestStoriesMenuItem:
                getNewStories("beststories");
                storyTypeLabel.setText(R.string.best_stories);
                break;
            case R.id.newStoriesMenuItem:
                getNewStories("newstories");
                storyTypeLabel.setText(R.string.new_stories);
                break;
            case R.id.askStoriesMenuItem:
                getNewStories("askstories");
                storyTypeLabel.setText(R.string.ask_stories);
                break;
            case R.id.jobStoriesMenuItem:
                getNewStories("jobstories");
                storyTypeLabel.setText(R.string.job_stories);
                break;
            case R.id.showStoriesMenuItem:
                getNewStories("showstories");
                storyTypeLabel.setText(R.string.show_stories);
                break;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getNewStories(String tag){

        loadingPanel.setVisibility(View.VISIBLE);

        GatherStoriesTask gatherStoriesTask = new GatherStoriesTask();

        gatherStoriesTask.setDelegate(this);

        gatherStoriesTask.execute(String.format(API_CALL, tag));

    }


    public String formatApiCall(String item){

        return String.format(API_CALL, item);

    }

    @Override
    public void gatherStoriesFinish(ArrayList<Story> output, JsonArray storyIds, int currentStoryIndex) {

        loadingPanel.setVisibility(View.INVISIBLE);

        ListView storyListView = (ListView) findViewById(R.id.storyListView);

        this.stories = output;

        this.storyIds = storyIds;

        this.storyIndex = currentStoryIndex;

        this.storiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stories);

        storyListView.setAdapter(storiesAdapter);

    }

    @Override
    public void getExtraStoryFinish(Story newStory) {

        loadingPanel.setVisibility(View.INVISIBLE);

        if(newStory != null) {

            stories.add(newStory);

            storiesAdapter.notifyDataSetChanged();

        }

    }
}
