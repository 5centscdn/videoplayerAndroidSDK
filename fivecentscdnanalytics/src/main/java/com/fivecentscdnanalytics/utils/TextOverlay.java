package com.fivecentscdnanalytics.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fivecentscdnanalytics.R;

public class TextOverlay extends FrameLayout {
    private TextView textOverlay;
    Context context;
    public TextOverlay(Context context) {
        super(context);
        init(context);
        this.context = context;
    }
    public TextOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        this.context = context;
    }
    public TextOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        this.context = context;
    }
    private void init(Context context) {
        if (textOverlay == null){
            View customView = LayoutInflater.from(context).inflate(R.layout.text_overlay, this, true);
            textOverlay = customView.findViewById(R.id.text_overlay);
        }

    }

    public void setText(String text) {
        if (textOverlay != null) {
            textOverlay.setText(text);
        }else{
            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
        }
    }
}
