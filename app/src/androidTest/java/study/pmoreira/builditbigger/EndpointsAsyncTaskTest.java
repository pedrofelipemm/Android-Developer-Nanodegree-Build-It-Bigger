package study.pmoreira.builditbigger;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import study.pmoreira.builditbigger.EndpointsAsyncTask.OnReceiveJokes;
import study.pmoreira.jokerepository.Joke;

public class EndpointsAsyncTaskTest {

    private OnReceiveJokes emptyInterface = new OnReceiveJokes() {
        @Override public void onPreExecute() {}
        @Override public void onPostExecute(List<Joke> jokes) {}
    };

    @Test
    public void doInBackgroundReturnNonEmptyList() throws Exception {
        List<Joke> jokes = new EndpointsAsyncTask(emptyInterface).execute().get();
        Assert.assertEquals(10, jokes.size());
    }
}