package study.pmoreira.jokerepository;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class JokeRepository {

    public static List<Joke> findAll() {
        return mock();
    }

    private static List<Joke> mock() {
        List<Joke> jokes = new ArrayList<>();

        jokes.add(new Joke("What time does Sean Connery get to Wimbledon?", "Tennish"));
        jokes.add(new Joke(" I went to the zoo the other day. It was empty, except for a single dog...", "It was a Shih Tzu"));
        jokes.add(new Joke("What kind of bagel can fly?", "A plain bagel"));
        jokes.add(new Joke("Where do animals go when their tails fall off?", "The retail store"));
        jokes.add(new Joke(" Why can't you hear a pterodactyl going to the bathroom?", "Because the \"P\" is silent"));
        jokes.add(new Joke("How does a train eat?", "It goes chew chew"));
        jokes.add(new Joke("What's Forrest Gump's password?", "1Forrest1"));
        jokes.add(new Joke("What do you call a cow with no legs?", "Ground beef"));
        jokes.add(new Joke("How is imitation like a plateau?", "They're both the highest form of flattery"));
        jokes.add(new Joke("What's the best thing about living in Switzerland?", "I don't know. But the flag is a big plus"));

        return jokes;
    }

}
