package com.example.android.popularmoviess1;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by anamanolache on 4/30/18.
 */

public class MovieDetailActivity extends AppCompatActivity{

    private Movie mMovie;
    private TextView mTitle;
    private ImageView mPoster;
    private TextView mReleaseDate;
    private TextView mVoteAverage;
    private TextView mSynopsis;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mPoster = findViewById(R.id.poster_detail_imageView);
        mReleaseDate = findViewById(R.id.release_date_textView);
        mVoteAverage = findViewById(R.id.vote_average_textView);
        mSynopsis = findViewById(R.id.synopsis_textView);
        mTitle = findViewById(R.id.original_title_textView);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mMovie = intentThatStartedThisActivity.getParcelableExtra(Intent.EXTRA_TEXT);

                Drawable placeholder = this.getResources().getDrawable(R.drawable.icon_movie);
                int targetWidth = getWindowManager().getDefaultDisplay().getWidth()/2
                        + getWindowManager().getDefaultDisplay().getWidth()/4;
                Picasso.with(this)
                        .load(mMovie.getPosterPathSmall().toString())
                        .placeholder(placeholder)
                        .resize(targetWidth, 0)
                        .into(mPoster);

                SimpleDateFormat simpleDate = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                Date date = null;
                String newDate = "-";
                try {
                    if (mMovie.getReleaseDate() != null && !mMovie.getReleaseDate().isEmpty()) {
                        date = sdf.parse(mMovie.getReleaseDate());
                        newDate = simpleDate.format(date);
                }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mTitle.setText(mMovie.getTitle());
                mReleaseDate.setText(newDate);
                mVoteAverage.setText(mMovie.getVoteAverage()+"");

                String synopsis = "-";
                if (mMovie.getSynopsis() != null && !mMovie.getSynopsis().isEmpty())
                    synopsis = mMovie.getSynopsis();
                mSynopsis.setText(synopsis);

                setTitle(mMovie.getTitle());
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
