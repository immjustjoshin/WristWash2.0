package com.teamwishwash.wristwash;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Joshua on 9/24/2018.
 */

public class HandWashListAdapter extends BaseAdapter {

    private Context mContext;
    private List<HandWashTechnique> mHandWashList;

    // Constructor
    public HandWashListAdapter(Context mContext, List<HandWashTechnique> mHandWashList) {
        this.mContext = mContext;
        this.mHandWashList = mHandWashList;
    }

    @Override
    public int getCount() {
        return mHandWashList.size();
    }

    @Override
    public Object getItem(int i) {
        return mHandWashList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.hand_wash_techniques_list, null);
        TextView techniqueItem = (TextView) v.findViewById(R.id.listItem);
        TextView scoreItem = (TextView) v.findViewById(R.id.scoreItem);
        double scoreValue = mHandWashList.get(i).getScore();

        // Set text for technique name and score & color
        techniqueItem.setText(mHandWashList.get(i).getHandWashTechnique());
        scoreItem.setText(String.valueOf(mHandWashList.get(i).getScore()));
        if (scoreValue >= 0 && scoreValue < 4) {
            scoreItem.setTextColor(Color.parseColor("#ff0000"));
        } else if (scoreValue >=4 && scoreValue < 8) {
            scoreItem.setTextColor(Color.parseColor("#edbb20"));
        } else if (scoreValue >= 8 && scoreValue <= 10) {
            scoreItem.setTextColor(Color.parseColor("#00cd00"));
        }

        return v;
    }
}
