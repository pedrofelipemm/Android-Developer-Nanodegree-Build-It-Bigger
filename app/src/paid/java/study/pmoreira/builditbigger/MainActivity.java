package study.pmoreira.builditbigger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import study.pmoreira.builditbigger.MainActivityFragment.OnTellJoke;
import study.pmoreira.jokeactivity.JokeActivity;
import study.pmoreira.jokerepository.Joke;

public class MainActivity extends AppCompatActivity implements OnTellJoke {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new MainActivityFragment())
                .commit();
    }

    @Override
    public void onTellJoke(Joke joke) {
        JokeActivity.startActivity(this, joke);
    }
}
