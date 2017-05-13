package com.example.rc.samples;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.rc.samples.contentProvider.PersonContentProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import models.Person;

/**
 * Created by RENT on 2017-05-10.
 */

public class PersonAdapter extends CursorAdapter {

    @BindView(R.id.name)
    protected TextView name;

    @BindView(R.id.ratingBar)
    protected RatingBar ratingBar;

    public PersonAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.stars, parent, false);
        view.setLongClickable(true);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor c) {
        ButterKnife.bind(this, view);
        name.setText(c.getString(c.getColumnIndex(Person.NAME)));
        ratingBar.setRating((float) c.getDouble(c.getColumnIndex(Person.RATING)));
        int id = (int) c.getInt(c.getColumnIndex(Person.ID));
        ratingBar.setTag(id);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean ifNotInit) {
                if (ifNotInit) {
                    ContentValues values = new ContentValues();
                    values.put(Person.RATING, String.valueOf(rating));
                    context.getContentResolver().update(
                            Uri.parse(PersonContentProvider.CONTENT_URI+"/"+ ratingBar.getTag()),
                            values,
                            null,
                            null
                    );
                }
            }
        });
    }
}