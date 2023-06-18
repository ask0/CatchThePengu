package com.ask.catchthepengu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView timeText;
    TextView scoreText;
    TextView highScoreText;
    int score;
    int highScore;
    ImageView[] imageViews;

    Handler handler;
    Runnable runnable;
    CountDownTimer countDownTimer;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeText = findViewById(R.id.timeText);
        scoreText = findViewById(R.id.scoreText);
        highScoreText = findViewById(R.id.highScoreText);

        sharedPreferences = this.getSharedPreferences("com.ask.catchthekenny", Context.MODE_PRIVATE);
        highScore = sharedPreferences.getInt("highScore", 0);
        highScoreText.setText("High Score: " + highScore);
        score = 0;

        imageViews = new ImageView[9];
        imageViews[0] = findViewById(R.id.imageView);
        imageViews[1] = findViewById(R.id.imageView1);
        imageViews[2] = findViewById(R.id.imageView2);
        imageViews[3] = findViewById(R.id.imageView3);
        imageViews[4] = findViewById(R.id.imageView4);
        imageViews[5] = findViewById(R.id.imageView5);
        imageViews[6] = findViewById(R.id.imageView6);
        imageViews[7] = findViewById(R.id.imageView7);
        imageViews[8] = findViewById(R.id.imageView8);

        game();
    }

    private void game() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                for (ImageView imageView : imageViews) {
                    imageView.setVisibility(View.INVISIBLE);
                }

                Random random = new Random();
                int i = random.nextInt(9);
                imageViews[i].setVisibility(View.VISIBLE);
                handler.postDelayed(this, 300);
            }
        };
        handler.post(runnable);

        countDownTimer = new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long l) {
                timeText.setText("Time: " + l / 1000);
            }

            @Override
            public void onFinish() {
                timeText.setText("Time Off");
                handler.removeCallbacks(runnable);
                for (ImageView imageView : imageViews) {
                    imageView.setVisibility(View.INVISIBLE);
                }
                replayGame();
            }
        }.start();
    }

    public void increaseScore(View view) {
        scoreText.setText("Score: " + (++score));
    }

    public void replayGame() {
        if(score > highScore){
            highScore = score;
            highScoreText.setText("Highest Score: " + highScore);
            sharedPreferences.edit().putInt("highScore", highScore).apply();
        }
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Your score is: " + score + "\nDo you want to play again?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        score = 0;
                        scoreText.setText("Score: 0");
                        game();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Game Over!", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }
}
