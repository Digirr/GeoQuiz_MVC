package com.digir.geoquiz_mvc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton, mPrevButton;
    private TextView mQuestionTextView;

    //Kontroler
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;
    private boolean[] questWasDone = new boolean[mQuestionBank.length];
    int score = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Wywołanie metody: onCreate(Bundle)");
        setContentView(R.layout.activity_quiz);
        for(int i=0;i<questWasDone.length;i++){
            questWasDone[i] = false;
        }
        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questWasDone[mCurrentIndex] = true;
                checkAnswer(true);
                lockOrUnlock(false);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questWasDone[mCurrentIndex] = true;
                checkAnswer(false);
                lockOrUnlock(false);
            }
        });
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentIndex != 0) {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                    updateQuestion();
                    if(questWasDone[mCurrentIndex])
                        lockOrUnlock(false);
                    else
                        lockOrUnlock(true);
                }
            }
        });
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentIndex == (mQuestionBank.length-1)){
                    double percent = Math.round((score*100)/mQuestionBank.length);
                    Toast.makeText(QuizActivity.this, "Score: " + percent + "%", Toast.LENGTH_SHORT).show();
                    score=0;
                } else {
                    mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                    updateQuestion();
                    if(questWasDone[mCurrentIndex])
                        lockOrUnlock(false);
                    else
                        lockOrUnlock(true);
                }

            }
        });

        updateQuestion();
    }
    private void lockOrUnlock(boolean or) {
        mTrueButton.setEnabled(or);
        mFalseButton.setEnabled(or);
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Wywołanie metody: onStart()");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Wywołanie metody: onResume()");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Wywołanie metody: onPause()");
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Wywołanie metody: onStop()");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Wywołanie metody: onDestroy()");
    }
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if(userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            score++;
        } else {
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
}