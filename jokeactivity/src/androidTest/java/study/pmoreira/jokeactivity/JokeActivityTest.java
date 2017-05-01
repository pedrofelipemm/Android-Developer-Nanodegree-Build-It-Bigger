package study.pmoreira.jokeactivity;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import study.pmoreira.jokerepository.Joke;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class JokeActivityTest {

    @Rule
    @SuppressWarnings("unchecked")
    public ActivityTestRule<JokeActivity> mActivityRule = new ActivityTestRule(JokeActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent(InstrumentationRegistry.getContext(), JokeActivity.class);
            intent.putExtra(JokeActivity.EXTRA_JOKE, new Joke("Test Joke", "Test answer"));
            return intent;
        }
    };

    @Test
    public void withExtraJokeJokeAndAnswerDisplays() {
        onView(withId(R.id.answer_button))
                .perform(click());

        onView(withId(R.id.answer_textview))
                .check(matches(isDisplayed()));
    }

}