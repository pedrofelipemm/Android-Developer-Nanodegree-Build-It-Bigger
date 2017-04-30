package study.pmoreira.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.List;

import study.pmoreira.jokerepository.Joke;
import study.pmoreira.jokerepository.JokeRepository;

@Api(
        name = "jokeApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.pmoreira.study",
                ownerName = "backend.pmoreira.study",
                packagePath = ""
        )
)
public class JokeEndpoint {

    @ApiMethod(name = "jokes", httpMethod = HttpMethod.GET)
    public List<Joke> findAll() {
        return JokeRepository.findAll();
    }

}
