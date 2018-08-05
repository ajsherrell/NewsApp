/**
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *-- used some code from Quake Report App -Udacity
 */

package com.example.android.newsapp;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /** Tag for log messages */
    private static final String TAG = NewsLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * construct a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    // start loading and implement forceLoad
    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i(TAG, "onStartLoading: Start loading!!!!!!!!!");
    }

    // this is on a background thread
    @Override
    public List<News> loadInBackground() {
        // check url
        if (mUrl == null) {
            return null;
        }
        Log.i(TAG, "loadInBackground: What is loading in the background!!!!!");

        // perform the network request, parse the response, and extract a list of
        // news articles
    List<News> news = QueryUtils.fetchNewsData(mUrl);
    return news;
    }

}
