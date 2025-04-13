package com.mtnl.faultverse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_ANIMATION_DURATION = 100; // Duration between each frame (in milliseconds)
    private static final int FRAME_COUNT = 11; // Number of frames in the animation

    private ImageView splashImageView;
    private int currentFrame = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashImageView = findViewById(R.id.imageViewSplash);

        // Start the animation loop
        startSplashAnimation();
    }

    private void startSplashAnimation() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Update the ImageView with the next frame
                splashImageView.setImageResource(getNextAnimationFrame());

                // Increment the current frame counter
                currentFrame++;

                // If we haven't reached the last frame, continue the animation
                if (currentFrame < FRAME_COUNT) {
                    handler.postDelayed(this, SPLASH_ANIMATION_DURATION);
                } else {
                    // The animation is finished, navigate to the main activity
                    navigateToMainActivity();
                }
            }
        }, SPLASH_ANIMATION_DURATION);
    }

    private int getNextAnimationFrame() {

        return getResources().getIdentifier("frame_" + currentFrame, "drawable", getPackageName());
    }

    private void navigateToMainActivity() {

        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }
}