package com.digir.geoquiz_mvc;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.digir.geoquiz_mvc.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.digir.geoquiz_mvc.answer_shown";
    private static final String GLOBAL_KEY = "globalKey";
    private static final String KEY1 = "key1";
    private static final String KEY2 = "key2";
    private TextView mAnswerTextView;
    private TextView mApiTextView;
    private TextView cheatPointsTextView;
    private Button mShowAnswerButton;
    private String TAG = "CheatActivity";
    private boolean mAnswerIsTrue;
    private int cheatPoints;
    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        checkApi();
        SharedPreferences appPrefs = getSharedPreferences(GLOBAL_KEY, MODE_PRIVATE);
        cheatPoints = appPrefs.getInt(KEY2, 0);

        cheatPointsTextView = (TextView) findViewById(R.id.cheat_checks_score);
        cheatPointsTextView.setText(String.valueOf(cheatPoints));

        //defaultValue - false when key not found
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        if(savedInstanceState != null) {
            mAnswerIsTrue = savedInstanceState.getBoolean(KEY1, false);
            useOnClick();
        }

        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        if(cheatPoints <= 0) {
            mShowAnswerButton.setEnabled(false);
        }
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useOnClick();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {    //If API user is lower than API 21 then only make button invisible without animation
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
                cheatPoints--;
                sharePreferences(cheatPoints);
                cheatPointsTextView.setText(String.valueOf(cheatPoints));

            }
        });

    }
    private void sharePreferences(int cP){
        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_KEY, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(KEY2, cP);
        prefsEditor.commit();
    }
    private void checkApi() {
        mApiTextView = (TextView) findViewById(R.id.which_api);
        int version = Build.VERSION.SDK_INT;
        mApiTextView.setText("API Level " + version);
    }
    private void useOnClick() {
        if(mAnswerIsTrue){
            mAnswerTextView.setText(R.string.true_button);
        }

        else{
            mAnswerTextView.setText(R.string.false_button);
        }
        setAnswerShownResult(true);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.i(TAG, "onSaveInstanceState");
        outState.putBoolean(KEY1, mAnswerIsTrue);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}