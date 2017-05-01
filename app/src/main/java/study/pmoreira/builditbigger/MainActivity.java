package study.pmoreira.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import study.pmoreira.backend.jokeApi.JokeApi;
import study.pmoreira.jokeactivity.JokeActivity;
import study.pmoreira.jokerepository.Joke;

public class MainActivity extends AppCompatActivity {

    private static final String STATE_JOKES = "STATE_JOKES";
    private static final String STATE_JOKE_INDEX = "STATE_JOKE_INDEX";

    ProgressBar mProgressBar;
    Button mTellJokeButton;

    private int mJokeIndex;

    ArrayList<Joke> mJokes;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mTellJokeButton = (Button) findViewById(R.id.tell_joke_button);

        if (savedInstanceState != null) {
            mJokes = (ArrayList<Joke>) savedInstanceState.getSerializable(STATE_JOKES);
            mJokeIndex = savedInstanceState.getInt(STATE_JOKE_INDEX);
        }

        if (mJokes == null || mJokes.isEmpty()) {
            new EndpointsAsyncTask(this, false).execute();
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
            new EndpointsAsyncTask(this, true).execute();
        }

        return isValid;
    }

    private class EndpointsAsyncTask extends AsyncTask<Void, Void, List<Joke>> {

        private final String TAG = EndpointsAsyncTask.class.getName();

        private JokeApi mJokeApi = null;
        private Context mContext;
        private boolean mTellJoke;

        EndpointsAsyncTask(Context context, boolean tellJoke) {
            mContext = context;
            mTellJoke = tellJoke;
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mTellJokeButton.setEnabled(false);
        }

        @Override
        protected List<Joke> doInBackground(Void... params) {
            if (mJokeApi == null) {
                mJokeApi = newJokeApi();
            }

            try {
                return parseJokes(mJokeApi.jokes().execute().getItems());
            } catch (IOException e) {
                Log.d(TAG, "doInBackground: ", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Joke> jokes) {
            if (!NetworkUtils.isNetworkAvailable(mContext)) {
                Toast.makeText(mContext, R.string.error_no_internet, Toast.LENGTH_SHORT).show();
            } else if (jokes == null || jokes.isEmpty()) {
                Toast.makeText(mContext, R.string.error_server_error, Toast.LENGTH_SHORT).show();
            }

            if (jokes != null) mJokes = new ArrayList<>(jokes);

            if (mTellJoke) tellJoke(null);

            mProgressBar.setVisibility(View.GONE);
            mTellJokeButton.setEnabled(true);
        }

        private List<Joke> parseJokes(List<study.pmoreira.backend.jokeApi.model.Joke> items) {
            List<Joke> jokes = new ArrayList<>(items.size());

            for (study.pmoreira.backend.jokeApi.model.Joke item : items) {
                jokes.add(new Joke(item.getJoke(), item.getAnswer()));
            }

            Collections.shuffle(jokes);
            return jokes;
        }

        private JokeApi newJokeApi() {
            return new JokeApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl(BuildConfig.JOKE_API_ROOT_URL)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                                throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    }).build();
        }
    }

}
