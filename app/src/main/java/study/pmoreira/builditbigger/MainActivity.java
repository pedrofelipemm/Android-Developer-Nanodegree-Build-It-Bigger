package study.pmoreira.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

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

    private int mJokeIndex;

    List<Joke> mJokes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new EndpointsAsyncTask(this).execute();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //TODO
    }

    public void tellJoke(View view) {
        if (mJokeIndex >= mJokes.size()) {
            mJokeIndex = 0;
        }

        Joke joke = mJokes.get(mJokeIndex++);

        Intent intent = new Intent(this, JokeActivity.class);
        intent.putExtra(JokeActivity.EXTRA_JOKE, joke);
        startActivity(intent);
    }

    private class EndpointsAsyncTask extends AsyncTask<Void, Void, List<Joke>> {

        private final String TAG = EndpointsAsyncTask.class.getName();

        private JokeApi mJokeApi = null;
        private Context mContext;

        EndpointsAsyncTask(Context context) {
            mContext = context;
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

        private List<Joke> parseJokes(List<study.pmoreira.backend.jokeApi.model.Joke> items) {
            List<Joke> jokes = new ArrayList<>(items.size());

            for (study.pmoreira.backend.jokeApi.model.Joke item : items) {
                jokes.add(new Joke(item.getJoke(), item.getAnswer()));
            }

            Collections.shuffle(mJokes);
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

        @Override
        protected void onPostExecute(List<Joke> jokes) {
            //hide indicator
            mJokes = jokes;
        }
    }

}
