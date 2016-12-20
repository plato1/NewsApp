/*
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
 */
package com.abnanodegree.jk.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * An {@link NewsAdapter} knows how to create a list item layout for each news
 * in the data source (a list of {@link News} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    // int val=5;

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param news   is the list of news, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    /**
     * Returns a list item view that displays information about the earthquake at the given position
     * in the list of news.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Find the news at the given position in the list of news
        News currentNews = getItem(position);

        // Find the TextView with view "title"
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        // Display the title of the current news in that TextView
        titleView.setText(currentNews.title);

        // Use url for image to get Glide to retrieve and display small image
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);
        Glide.with(getContext()).load(currentNews.url).into(imageView);

        // Find the TextView with view "author"
        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        // Display the author of the current news in that TextView
        authorView.setText(currentNews.author);

        // Find the TextView with view "date"
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        // Display the publishing date of the current news in that TextView
        dateView.setText(currentNews.publishedDate);

        // Find the TextView with view "sectinName"
        TextView priceView = (TextView) listItemView.findViewById(R.id.sectionName);
        // Display the price of the current news in that TextView
        priceView.setText(currentNews.sectionName);


        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
