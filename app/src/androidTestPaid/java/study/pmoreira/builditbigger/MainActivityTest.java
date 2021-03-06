package study.pmoreira.builditbigger;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    @SuppressWarnings("unchecked")
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void JokeButtonClickDisplaysJokeAndAnswer() {
        onView(withId(R.id.tell_joke_button))
                .perform(click());

        onView(withId(R.id.joke_textview))
                .check(matches(isDisplayed()));

        onView(withId(R.id.answer_button))
                .perform(click());

        onView(withId(R.id.answer_textview))
                .check(matches(isDisplayed()));
    }

}
