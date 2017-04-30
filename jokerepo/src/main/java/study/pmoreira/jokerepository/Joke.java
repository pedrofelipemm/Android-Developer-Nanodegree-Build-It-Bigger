package study.pmoreira.jokerepository;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Joke implements Serializable {

    private String joke;
    private String answer;

    Joke(String joke, String answer) {
        this.joke = joke;
        this.answer = answer;
    }

    public String getJoke() {
        return joke;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Joke joke1 = (Joke) o;

        return joke != null ? joke.equals(joke1.joke) : joke1.joke == null
                && answer != null ? answer.equals(joke1.answer) : joke1.answer == null;

    }

    @Override
    public int hashCode() {
        int result = joke != null ? joke.hashCode() : 0;
        result = 31 * result + (answer != null ? answer.hashCode() : 0);
        return result;
    }
}
