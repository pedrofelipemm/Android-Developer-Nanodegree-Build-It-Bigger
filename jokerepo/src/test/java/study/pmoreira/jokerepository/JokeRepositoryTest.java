package study.pmoreira.jokerepository;

import org.junit.Assert;
import org.junit.Test;

public class JokeRepositoryTest {

    @Test
    public void findAllReturn10Jokes() {
        Assert.assertEquals(10, JokeRepository.findAll().size());
    }

}