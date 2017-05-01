package study.pmoreira.builditbigger;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import study.pmoreira.backend.jokeApi.JokeApi;
import study.pmoreira.jokerepository.Joke;

class EndpointsAsyncTask extends AsyncTask<Void, Void, List<Joke>> {

    private final String TAG = EndpointsAsyncTask.class.getName();

    private JokeApi mJokeApi = null;
    private OnReceiveJokes mCallback;

    EndpointsAsyncTask(OnReceiveJokes callback) {
        mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        mCallback.onPreExecute();
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
        mCallback.onPostExecute(jokes);
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

    interface OnReceiveJokes {
        void onPreExecute();

        void onPostExecute(List<Joke> jokes);
    }
}
