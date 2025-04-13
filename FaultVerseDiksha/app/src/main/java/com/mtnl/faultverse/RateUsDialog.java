package com.mtnl.faultverse;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;

public class RateUsDialog extends Dialog {
    private float userate = 0;
    public RateUsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_us_dialog);

        final AppCompatRatingBar ratingBar = findViewById(R.id.ratingbar);
        final   AppCompatButton rate = findViewById(R.id.rate);
        final   AppCompatButton later = findViewById(R.id.review);
        final ImageView ratingimage = findViewById(R.id.ratingimage);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 // code when we click Rate now button
                ratingBar.setVisibility(View.VISIBLE);
            }
        });

        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                dismiss();
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(v <= 1){
                      ratingimage.setImageResource(R.drawable.first);
                }
                else if(v <= 2)
                {
                    ratingimage.setImageResource(R.drawable.second);
                }
                else if(v <= 3)
                {
                    ratingimage.setImageResource(R.drawable.four);
                }
                else if(v <= 4)
                {
                    ratingimage.setImageResource(R.drawable.five);
                }
                else if(v <= 5)
                {
                    ratingimage.setImageResource(R.drawable.third);
                }
                animateImage(ratingimage);
                userate = v;
            }
        });


    }
    private void animateImage(ImageView ratingimage){
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f,0,  1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        ratingimage.startAnimation(scaleAnimation);
    }
}
