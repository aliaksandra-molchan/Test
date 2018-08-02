package com.appodeal.support.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeCallbacks;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String APP_KEY = "8a57398146ede8bdd1c904b3bbb439d3b99eaa5e25fe7cb0";
    private static final long BANNER_TIME = 5000;
    private static final long INTERSTITIAL_TIME = 30000;
    private static final long COUNT_DOWN_INTERVAL = 1000;

    private boolean isFirstRange = true;

    private List<NativeAd> nativeAdList = new ArrayList<>();

    private Activity activity;

    private TextView timerTextView;
    private ProgressBar progressBar;

    private CountDownTimer interstitialTimer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button buttonStart = (Button) findViewById(R.id.buttonStart);

        initAppodeal();
        initBannerCallbacks();
        initInterstitialCallbacks();
        initNativeCallbacks();

        initInterstitialTimer();
        interstitialTimer.start();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFirstRange)
                    interstitialTimer.cancel();
                Appodeal.cache(activity, Appodeal.NATIVE, 1);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initAppodeal() {
        Appodeal.disableLocationPermissionCheck();
        Appodeal.setTesting(true);
        Appodeal.setAutoCache(Appodeal.NATIVE, false);
        Appodeal.initialize(this, APP_KEY, Appodeal.INTERSTITIAL | Appodeal.NATIVE | Appodeal.BANNER);
    }

    private void initInterstitialTimer() {
        interstitialTimer = new CountDownTimer(INTERSTITIAL_TIME, COUNT_DOWN_INTERVAL) {

            @Override
            public void onTick(long l) {
                timerTextView.setText(String.valueOf((l / COUNT_DOWN_INTERVAL)));
            }

            @Override
            public void onFinish() {
                timerTextView.setText("");
                Appodeal.show(activity, Appodeal.INTERSTITIAL);
                isFirstRange = false;
            }
        };
    }

    private void initBannerCallbacks() {
        Appodeal.setBannerCallbacks(new BannerCallbacks() {
            @Override
            public void onBannerLoaded(int i, boolean b) {
                Appodeal.show(activity, Appodeal.BANNER_TOP);
                new CountDownTimer(BANNER_TIME, COUNT_DOWN_INTERVAL) {

                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        Appodeal.hide(activity, Appodeal.BANNER);
                    }
                }.start();
            }

            @Override
            public void onBannerFailedToLoad() {

            }

            @Override
            public void onBannerShown() {

            }

            @Override
            public void onBannerClicked() {

            }
        });
    }

    private void initInterstitialCallbacks() {
        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            @Override
            public void onInterstitialLoaded(boolean isPrecache) {

            }

            @Override
            public void onInterstitialFailedToLoad() {

            }

            @Override
            public void onInterstitialShown() {

            }

            @Override
            public void onInterstitialClicked() {

            }

            @Override
            public void onInterstitialClosed() {
                interstitialTimer.start();
            }
        });
    }

    private void initNativeCallbacks() {
        Appodeal.setNativeCallbacks(new NativeCallbacks() {
            @Override
            public void onNativeLoaded() {
                List<News> newsFeed=new ArrayList<>();
                newsFeed.add(new News("Google 'plans censored China search engine","Google"));
                newsFeed.add(new News("McDonald's gives mum-to-be chemical latte","McDonald's"));
                newsFeed.add(new News("Almost all lemurs are under threat of extinction","Lemurs"));
                newsFeed.add(new News("Tokyo Medical University 'changed female exam scores'","Tokyo"));

                nativeAdList = Appodeal.getNativeAds(1);

                ListView listView = (ListView) findViewById(R.id.nativeList);

                NewsAdapter newsAdapter=new NewsAdapter(activity, nativeAdList.get(0),newsFeed);
                listView.setAdapter(newsAdapter);

                Appodeal.cache(activity, Appodeal.NATIVE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNativeFailedToLoad() {
            }

            @Override
            public void onNativeShown(NativeAd nativeAd) {

            }

            @Override
            public void onNativeClicked(NativeAd nativeAd) {
            }
        });
    }

}
