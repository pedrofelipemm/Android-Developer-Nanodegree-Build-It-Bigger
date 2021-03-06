package study.pmoreira.jokeactivity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class JokeActivityNoIntentTest {

    @Rule
    @SuppressWarnings("unchecked")
    public ActivityTestRule<JokeActivity> mActivityRule = new ActivityTestRule(JokeActivity.class);

    @Test
    public void noExtraEmptyViewDisplays() {
        onView(withId(R.id.empty_view))
                .check(matches(isDisplayed()));
    }

}