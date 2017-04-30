package study.pmoreira.jokeactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import study.pmoreira.jokerepository.Joke;

/**
 * An activity to display jokes.
 * It expects the {@link Joke} in the intent, with the extra: {@link JokeActivity#EXTRA_JOKE}
 */
public class JokeActivity extends AppCompatActivity {

    private static final String TAG = JokeActivity.class.getName();

    public static final String EXTRA_JOKE = "EXTRA_JOKE";

    private TextView mJokeTextView;
    private TextView mAnswerTextView;
    private TextView mEmptyViewTextView;

    private Button mAnswerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        ActionBar actionBar = null;
        if ((actionBar = getSupportActionBar()) != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        loadJoke();
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

    private void initViews() {
        mJokeTextView = (TextView) findViewById(R.id.joke_textview);
        mAnswerTextView = (TextView) findViewById(R.id.answer_textview);
        mEmptyViewTextView = (TextView) findViewById(R.id.empty_view);

        mAnswerButton = (Button) findViewById(R.id.answer_button);
        mAnswerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnswerTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadJoke() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_JOKE)) {
            displayJoke((Joke) intent.getSerializableExtra(EXTRA_JOKE));
        } else {
            displayError();
            Log.e(TAG, getString(R.string.error_joke_expected));
        }
    }

    private void displayJoke(Joke joke) {
        mJokeTextView.setText(joke.getJoke());
        mAnswerTextView.setText(joke.getAnswer());
    }

    private void displayError() {
        mJokeTextView.setVisibility(View.GONE);
        mAnswerTextView.setVisibility(View.GONE);
        mAnswerButton.setVisibility(View.GONE);
        mEmptyViewTextView.setVisibility(View.VISIBLE);
    }
}
