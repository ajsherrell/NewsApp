package com.example.android.newsapp;

public class News {

    // title of new article
    private String mTitle;

    // date of article published
    private String mDate = NO_DATE_PROVIDED;

    // section name for article
    private String mSection;

    // author of article
    private String mAuthor = NO_AUTHOR_PROVIDED;

    // url of article
    private String mUrl;

    // constant value that represents no date provided for this news article
    private static final String NO_DATE_PROVIDED = null;

    // constant value that represents no author provided for this news article
    private static final String NO_AUTHOR_PROVIDED = null;

    /**
     * create a new News object
     *
     * @param title is title of the article
     * @param date is the date of the article
     * @param section is the section the article is in
     * @param author is the author of the article
     * @param url is website of article
     */

    public News(String title, String date, String section, String author, String url) {
        mTitle = title;
        mDate = date;
        mSection = section;
        mAuthor = author;
        mUrl = url;
    }

    /**
     * create a new News object for no author or date
     *
     * @param title is title of the article
     * @param section is the section the article is in
     * @param url is website of article
     */

    public News(String title, String section, String url) {
        mTitle = title;
        mSection = section;
        mUrl = url;
    }

    // getters

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getUrl() {
        return mUrl;
    }

    // booleans for date and author
    public boolean hasDate() {
        return mDate != NO_DATE_PROVIDED;
    }

    public boolean hasAuthor() {
        return mAuthor != NO_AUTHOR_PROVIDED;
    }

}
