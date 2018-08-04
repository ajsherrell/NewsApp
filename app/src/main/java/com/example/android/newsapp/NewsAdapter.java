package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * This is the custom constructor. The content is used to inflate the layout file,
     * and the list is the date we want to populate into the lists.
     *
     * @param context is the current context used to inflate the layout file.
     * @param news is a list of news article objects to display in a list.
     */
    public NewsAdapter(Context context, List<News> news) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        super(context, 0, news);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // get the {@link News} object located at this position in the list
        News currentNewsArticle = getItem(position);

        // find the TextView with view Id title
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        // set the text of news article title
        titleView.setText(currentNewsArticle.getTitle());

        // find the TextView with the view Id section
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);
        // set the text of new article section
        sectionView.setText(currentNewsArticle.getSection());

        // find the TextView with the date of news article
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        // check if date object text present
        if (currentNewsArticle.hasDate()) {
            // Create a new Date object from the time in milliseconds of the earthquake
            Date dateObject = new Date(currentNewsArticle.getDate());

            // Format the date string (i.e. "Mar 3, 1984") -used from QuakeReport App
            String formattedDate = formatDate(dateObject);
            // display the date
            dateView.setText(formattedDate);

            // make date view visible
            dateView.setVisibility(View.VISIBLE);
        } else {
            // make view invisible if not available
            dateView.setVisibility(View.GONE);
        }

        // find the TextView with the author of the news article
        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        // check author text boolean
        if (currentNewsArticle.hasAuthor()) {
            // if present, display
            authorView.setText(currentNewsArticle.getAuthor());
            // make view visible
            authorView.setVisibility(View.VISIBLE);
        } else {
            // hide the view if not available
            authorView.setVisibility(View.GONE);
        }

        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     *  Used from Quake Report App
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

}
