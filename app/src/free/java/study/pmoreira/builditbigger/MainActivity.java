package study.pmoreira.builditbigger;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import study.pmoreira.builditbigger.MainActivityFragment.OnTellJoke;
import study.pmoreira.jokeactivity.JokeActivity;
import study.pmoreira.jokerepository.Joke;

public class MainActivity extends AppCompatActivity implements OnTellJoke {

    private MainActivityFragment mFragment = new MainActivityFragment();

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();
        initAd();
    }

    private void initFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mFragment)
                .commit();
    }

    private void initAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.test_banner_ad_unit_id));

        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .addTestDevice(BuildConfig.TEST_DEVICE_ID)
                .build());
    }

    @Override
    public void onTellJoke(Joke joke) {
        // I don't want the user waiting for the ad, if it's not loaded, he's going to see it in the next iteration
        if (mInterstitialAd.isLoaded() && joke != null) {
            mInterstitialAd.setAdListener(new CustomAdListener(this, mFragment, joke));
            mInterstitialAd.show();
        } else if (joke != null) {
            JokeActivity.startActivity(this, joke);
        } else {
            mFragment.tryTellJoke();
        }
    }

    private class CustomAdListener extends AdListener {

        private Context mContext;
        private MainActivityFragment mFragment;
        private Joke mJoke;

        CustomAdListener(Context context, MainActivityFragment fragment, Joke joke) {
            mContext = context;
            mFragment = fragment;
            mJoke = joke;
        }

        @Override
        public void onAdClosed() {
            requestNewInterstitial();
            JokeActivity.startActivity(mContext, mJoke);
        }
    }
}
