package study.pmoreira.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Collections;
import java.util.List;

import study.pmoreira.jokeactivity.JokeActivity;
import study.pmoreira.jokerepository.Joke;
import study.pmoreira.jokerepository.JokeRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void tellJoke(View view) {
        List<Joke> jokes = JokeRepository.findAll();
        Collections.shuffle(jokes);

        Intent intent = new Intent(this, JokeActivity.class);
        intent.putExtra(JokeActivity.EXTRA_JOKE, jokes.get(0));
        startActivity(intent);
    }

}
