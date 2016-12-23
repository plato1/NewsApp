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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * {@News} represents a single volume in the Google News db. It holds the details
 * of that News such as title, first author, published date, retail price.
 */
public class News implements Parcelable {

    /**
     * Title of the news article
     */
    public final String title;
    /**
     * Name of section where article is located
     */
    public final String sectionName;
    /**
     * date News published
     */
    public final String publishedDate;
    public final String url;


    /**
     * Constructs a new {@link News}.
     *
     * @param NewsTitle         is the title of the News
     * @param NewsPublishedDate is date News published
     * @param SectionName       is name of section where article appears
     */
    public News(String NewsTitle, String NewsPublishedDate, String SectionName, String NewsUrl) {
        title = NewsTitle;
        publishedDate = NewsPublishedDate;
        sectionName = SectionName;
        url = NewsUrl;
    }

    private News(Parcel in) {
        title = in.readString();
        publishedDate = in.readString();
        sectionName = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return title + "--" + "--" + publishedDate + "--" + sectionName + "--" + url;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(publishedDate);
        parcel.writeString(sectionName);
        parcel.writeString(url);
    }

    public final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel parcel) {
            return new News(parcel);
        }

        @Override
        public News[] newArray(int i) {
            return new News[i];
        }

    };
}