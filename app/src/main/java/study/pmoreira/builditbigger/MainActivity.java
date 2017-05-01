package study.pmoreira.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import study.pmoreira.builditbigger.EndpointsAsyncTask.OnReceiveJokes;
import study.pmoreira.jokeactivity.JokeActivity;
import study.pmoreira.jokerepository.Joke;

public class MainActivity extends AppCompatActivity implements OnReceiveJokes {

    private static final String STATE_JOKES = "STATE_JOKES";
    private static final String STATE_JOKE_INDEX = "STATE_JOKE_INDEX";

    ProgressBar mProgressBar;
    Button mTellJokeButton;

    private boolean mTellJoke;
    private int mJokeIndex;

    ArrayList<Joke> mJokes;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTellJoke = false;
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mTellJokeButton = (Button) findViewById(R.id.tell_joke_button);

        if (savedInstanceState != null) {
            mJokes = (ArrayList<Joke>) savedInstanceState.getSerializable(STATE_JOKES);
            mJokeIndex = savedInstanceState.getInt(STATE_JOKE_INDEX);
        }

        if (mJokes == null || mJokes.isEmpty()) {
            new EndpointsAsyncTask(this).execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_JOKES, mJokes);
        outState.putInt(STATE_JOKE_INDEX, mJokeIndex);
    }

    public void tellJoke(View view) {
        if (!isValid()) {
            return;
        }

        if (mJokeIndex >= mJokes.size()) {
            mJokeIndex = 0;
        }

        Joke joke = mJokes.get(mJokeIndex++);

        Intent intent = new Intent(this, JokeActivity.class);
        intent.putExtra(JokeActivity.EXTRA_JOKE, joke);
        startActivity(intent);
    }

    public boolean isValid() {
        boolean isValid = true;

        if (!NetworkUtils.isNetworkAvailable(this) && (mJokes == null || mJokes.isEmpty())) {
            isValid = false;
            Toast.makeText(this, R.string.error_no_internet, Toast.LENGTH_SHORT).show();
        } else if (mJokes == null || mJokes.isEmpty()) {
            isValid = false;
            mTellJoke = true;
            new EndpointsAsyncTask(this).execute();
        }

        return isValid;
    }

    @Override
    public void onPreExecute() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTellJokeButton.setEnabled(false);
    }

    @Override
    public void onPostExecute(List<Joke> jokes) {
        mProgressBar.setVisibility(View.GONE);
        mTellJokeButton.setEnabled(true);

        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.error_no_internet, Toast.LENGTH_SHORT).show();
        } else if (jokes == null || jokes.isEmpty()) {
            Toast.makeText(this, R.string.error_server_error, Toast.LENGTH_SHORT).show();
        }

        if (jokes != null) mJokes = new ArrayList<>(jokes);

        if (mTellJoke) tellJoke(null);
    }
}
