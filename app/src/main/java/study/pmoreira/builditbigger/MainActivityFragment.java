package study.pmoreira.builditbigger;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import study.pmoreira.builditbigger.EndpointsAsyncTask.OnReceiveJokes;
import study.pmoreira.jokerepository.Joke;

public class MainActivityFragment extends Fragment implements OnReceiveJokes {

    private static final String TAG = MainActivityFragment.class.getName();

    private static final String STATE_JOKES = "STATE_JOKES";
    private static final String STATE_JOKE_INDEX = "STATE_JOKE_INDEX";

    ProgressBar mProgressBar;
    Button mTellJokeButton;

    private OnTellJoke mCallback;
    private boolean mTellJoke;
    private int mJokeIndex;

    ArrayList<Joke> mJokes;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnTellJoke) {
            mCallback = (OnTellJoke) context;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mTellJoke = false;
        initViews(root);

        if (savedInstanceState != null) {
            mJokes = (ArrayList<Joke>) savedInstanceState.getSerializable(STATE_JOKES);
            mJokeIndex = savedInstanceState.getInt(STATE_JOKE_INDEX);
        }

        if (mJokes == null || mJokes.isEmpty()) {
            new EndpointsAsyncTask(this).execute();
        }

        return root;
    }

    private void initViews(View root) {
        mProgressBar = (ProgressBar) root.findViewById(R.id.progressbar);
        mTellJokeButton = (Button) root.findViewById(R.id.tell_joke_button);
        mTellJokeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tellJoke();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_JOKES, mJokes);
        outState.putInt(STATE_JOKE_INDEX, mJokeIndex);
    }

    void tellJoke() {
        if (!isValid()) {
            return;
        }

        if (mJokeIndex >= mJokes.size()) {
            mJokeIndex = 0;
        }

        Joke joke = mJokes.get(mJokeIndex++);

        if (mCallback != null) {
            mCallback.onTellJoke(joke);
        }
    }

    boolean isValid() {
        boolean isValid = true;

        if (!NetworkUtils.isNetworkAvailable(getContext()) && (mJokes == null || mJokes.isEmpty())) {
            isValid = false;
            Toast.makeText(getContext(), R.string.error_no_internet, Toast.LENGTH_SHORT).show();
        } else if (mJokes == null || mJokes.isEmpty()) {
            isValid = false;
            tryTellJoke();
        }

        return isValid;
    }

    /**
     * Tries to sync jokes with server then tell the joke.<br/>
     * For optimal performance, should be called only if first attempt fails.
     */
    void tryTellJoke() {
        mTellJoke = true;
        new EndpointsAsyncTask(this).execute();
    }

    @Override
    public void onPreExecute() {
        showProgressBar();
    }

    @Override
    public void onPostExecute(List<Joke> jokes) {
        hideProgressBar();

        if (!NetworkUtils.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), R.string.error_no_internet, Toast.LENGTH_SHORT).show();
        } else if (jokes == null || jokes.isEmpty()) {
            Toast.makeText(getContext(), R.string.error_server_error, Toast.LENGTH_SHORT).show();
        }

        if (jokes != null) mJokes = new ArrayList<>(jokes);

        if (mTellJoke) tellJoke();
    }

    void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTellJokeButton.setEnabled(false);
    }

    void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        mTellJokeButton.setEnabled(true);
    }

    interface OnTellJoke {
        void onTellJoke(Joke joke);
    }
}
