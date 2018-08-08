package com.appodeal.support.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appodeal.ads.NativeAd;
import com.appodeal.ads.native_ad.views.NativeAdViewNewsFeed;

import java.util.List;


public class NewsAdapter extends BaseAdapter {
    private static final int NEWS_TYPE = 0;
    private static final int NATIVE_TYPE = 1;

    private static final int NATIVE_INDEX = 2;

    private List<News> newsFeed;
    private Context context;
    private List<NativeAd> nativeAd;

    public NewsAdapter(Context context, List<NativeAd> nativeAd, List<News> newsFeed) {
        this.context = context;
        this.nativeAd = nativeAd;
        this.newsFeed = newsFeed;
    }


    @Override
    public int getCount() {
        return newsFeed.size() + nativeAd.size();
    }

    @Override
    public Object getItem(int i) {
        if (i != 0 && i % NATIVE_INDEX == 0) {
            if (i - (i / NATIVE_INDEX + 1) < nativeAd.size()) {
                return nativeAd.get(i - (i / NATIVE_INDEX + 1));
            } else return newsFeed.get(i - nativeAd.size());
        } else if (i < NATIVE_INDEX) {
            return newsFeed.get(i);
        }
        return newsFeed.get(i - i / NATIVE_INDEX);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        switch (getItemViewType(i)) {
            case NEWS_TYPE: {
                ViewHolder viewHolder;
                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.news_item, viewGroup, false);
                    viewHolder = new ViewHolder();
                    viewHolder.title = view.findViewById(R.id.newsTitleTextView);
                    viewHolder.description = view.findViewById(R.id.newsDescriptionTextView);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                viewHolder.title.setText(((News) getItem(i)).getTitle());
                viewHolder.description.setText(((News) getItem(i)).getDescription());
                break;
            }
            case NATIVE_TYPE: {
                if (view == null) {
                    view = LayoutInflater.from(context)
                            .inflate(R.layout.native_item, viewGroup, false);
                }
                ((NativeAdViewNewsFeed) view).setNativeAd((NativeAd) getItem(i));
            }
        }

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        if (position != 0 && position % NATIVE_INDEX == 0 &&
                position - (position / NATIVE_INDEX + 1) < nativeAd.size()) {
            return NATIVE_TYPE;
        }
        return NEWS_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public static final class ViewHolder {
        TextView title;
        TextView description;
    }
}
