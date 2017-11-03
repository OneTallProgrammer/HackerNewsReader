package com.onetallprogrammer.hackernewsreader.webinterface;

import com.google.gson.JsonArray;
import com.onetallprogrammer.hackernewsreader.model.Story;

import java.util.ArrayList;

/**
 * Created by joseph on 9/2/17.
 */

public interface GatherStoriesResponse {

    void gatherStoriesFinish(ArrayList<Story> output, JsonArray storyIds, int currentStoryIndex);

}
