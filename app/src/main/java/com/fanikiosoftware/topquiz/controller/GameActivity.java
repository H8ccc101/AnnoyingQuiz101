package com.fanikiosoftware.topquiz.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fanikiosoftware.topquiz.R;
import com.fanikiosoftware.topquiz.model.Question;
import com.fanikiosoftware.topquiz.model.QuestionBank;

import java.util.Arrays;

import static java.lang.System.out;

public class GameActivity extends AppCompatActivity implements OnClickListener {

    public static final String BUNDLE_STATE_SCORE =
            GameActivity.class.getCanonicalName().concat("BUNDLE_STATE_SCORE");
    public static final String BUNDLE_STATE_QUESTIONS =
            GameActivity.class.getCanonicalName().concat("BUNDLE_STATE_QUESTIONS");
    public static final String PREF_KEY_SCORE = "PREFERENCE_KEY_SCORE";

    private TextView mQuestionTextView;
    private Button mAnswerBtn1;
    private Button mAnswerBtn2;
    private Button mAnswerBtn3;
    private Button mAnswerBtn4;

    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;
    private int mScore;
    private int mNumQuestions;
    private boolean mEnableTouchEvents;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        out.println("GameActivity::onCreate()");
        mQuestionBank = this.generateQuestions(); //returns QuestionBank obj -> List of questions
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //has data been persisted? if so, get data (score and questions)
        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mNumQuestions = savedInstanceState.getInt(BUNDLE_STATE_QUESTIONS);
        } else {
            //no saved data on record
            mScore = 0;
            mNumQuestions = 5;
        }
        mEnableTouchEvents = true;

        //set up the widgets
        mQuestionTextView = findViewById(R.id.activity_game_question_text);
        mAnswerBtn1 = findViewById(R.id.activity_game_answer1_btn);
        mAnswerBtn2 = findViewById(R.id.activity_game_answer2_btn);
        mAnswerBtn3 = findViewById(R.id.activity_game_answer3_btn);
        mAnswerBtn4 = findViewById(R.id.activity_game_answer4_btn);

        //set up tags for onclick method identification
        mAnswerBtn1.setTag(0);
        mAnswerBtn2.setTag(1);
        mAnswerBtn3.setTag(2);
        mAnswerBtn4.setTag(3);

        //set up onclick listeners for all btns
        mAnswerBtn1.setOnClickListener(this);
        mAnswerBtn2.setOnClickListener(this);
        mAnswerBtn3.setOnClickListener(this);
        mAnswerBtn4.setOnClickListener(this);

        //retrieve a question
        mCurrentQuestion = mQuestionBank.getQuestion();
        //send current question to method to display question and answer choices
        this.displayQuestion(mCurrentQuestion);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTIONS, mNumQuestions);
        super.onSaveInstanceState(outState);
    }

    //displays the question and it's choicelist
    private void displayQuestion(final Question question) {
        //set question into text view widget
        mQuestionTextView.setText(question.getQuestion());
        //set index 0-3 of choice list into each answer button
        mAnswerBtn1.setText(question.getChoiceList().get(0));
        mAnswerBtn2.setText(question.getChoiceList().get(1));
        mAnswerBtn3.setText(question.getChoiceList().get(2));
        mAnswerBtn4.setText(question.getChoiceList().get(3));
    }

    //onClickListener implementation for answer/choice list btns
    @Override
    public void onClick(View v) {
        //which button called onClick()
        int userAnswer = (int) v.getTag();
        if (userAnswer == mCurrentQuestion.getAnswerIndex()) {
            //user answer is correct
            Toast.makeText(GameActivity.this, "Good work",
                    Toast.LENGTH_SHORT).show();
            mScore++;
        } else {
            // incorrect answer
            Toast.makeText(GameActivity.this, "R.I.P.",
                    Toast.LENGTH_SHORT).show();
        }
        mEnableTouchEvents = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnableTouchEvents = true;
                // If this is the last question,game over
                // else, display the next question.
                if (--mNumQuestions == 0) {
                    // End the game
                    endGame();
                } else {
                    mCurrentQuestion = mQuestionBank.getQuestion();
                    displayQuestion(mCurrentQuestion);
                }
            }
        }, 2000); // LENGTH_SHORT is usually 2 second long so delay touch until completed
    }

    //provide the questions and choice list for each question
    public QuestionBank generateQuestions() {
        Question question1 = new Question(
                "What is the name of the man Carl helped",
                Arrays.asList(
                        "Siddiq",
                        "Zeriff",
                        "Jackie",
                        "Astrid"),
                0);

        Question question2 = new Question(
                "How did Daryl's brother, Merle Dixon, die?",
                Arrays.asList("He was killed by Negan",
                        "Death by walker",
                        "The governor killed him",
                        "He killed himself"), 2);

        Question question3 = new Question("What was the first community Rick and " +
                "his group stayed at?",
                Arrays.asList(
                        "Alexandria",
                        "The Hilltop",
                        "The Kingdom",
                        "The Prison"),
                0);

        Question question4 = new Question("How did Carl die?",
                Arrays .asList(
                        "Negan Smashed His Head",
                        "Simon Shot Him",
                        "He Was Bitten",
                        "Douglas Killed Him In Alexandria"),
                2);

        Question question5 = new Question("When did Carl learn to use a gun?" ,
                Arrays.asList(
                        "1 year old",
                        "5 years old",
                        "7 years old",
                        "13 years old"), 2);

        Question question6 = new Question("What is Carl's sister's name?",
                Arrays.asList(
                        "Judith",
                        "Jamie",
                        "Harley",
                        "Andrea"), 0);

        Question question7 = new Question("How did Rick overpower Negan?",
                Arrays.asList(
                        "By killing him",
                        "By slitting his throat",
                        "By throwing him to walkers",
                        "By shooting him"),
                                            1);

        Question question8 = new Question("How did Henry die?",
                Arrays.asList(
                        "He was eaten by Walkers",
                        "He was shot",
                        "His head was cut off",
                        "He was killed by the Saviours"),
                                            2);

        Question question9 = new Question("How did Glen meet Maggie?",
                Arrays.asList(
                        "When Rick and his group went to Hershel's",
                        "They were together since the Apocalypse started",
                        "They are siblings",
                        "Glen almost killed her"),
                                            0);

        Question question10 = new Question(
                "What is the name of the newest human threat to Rick and his group?",
                Arrays.asList(
                        "Crusaders Of The Night",
                        "The Vigilantes",
                        "The Whisperers",
                        "The Blaze"),
                                            2);

        Question question11 = new Question("What was the first place Rick ran into a " +
                "Walker horde? ",
                Arrays.asList(
                        "Atlanta",
                        "On the freeway",
                        "In Alexandria",
                        "With Morgan"),
                                            0);

        Question question12 = new Question("What was the name of Carol's daughter " +
                "in the first season?",
                Arrays.asList(
                        "Jackie",
                        "Sophie",
                        "Sophia",
                        "Lizzie"),
                                            2);

        Question question13 = new Question("What were Carl's last words?",
                Arrays.asList(
                        "I'm not crying, You're crying.",
                        "Now I can join Mom",
                        "Don't be mad",
                        "This is your fault"),
                                            0);

        Question question14 = new Question("How did Carl first get shot?",
                Arrays.asList(
                        "Shane shot him",
                        "He was watching a deer and Otis shot him",
                        "He shot himself learning to shoot a gun",
                        "He was only shot as a walker"),
                                            1);

        Question question15 = new Question("Why was Ezekiel discouraged aft" +
                "er he returned from his journey ",
                Arrays.asList(
                        "He was reminded of his past",
                        "Jerry died",
                        "Carol died",
                        "Because he was ambushed"),
                                            3);
        return new QuestionBank(Arrays.asList(question1, question2, question3, question4,
                question5, question6, question7, question8, question9, question10, question11,
                question12, question13, question14, question15));
    }

    //endGame implementation ,save score to shared pref and display final score to user
    private void endGame() {
        new AlertDialog.Builder(this)
                .setTitle("Keep it up, you survived another year")
                .setMessage("Your score: " + mScore)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //store user score in Shared Preferences
                        mPreferences.edit().putInt(GameActivity.PREF_KEY_SCORE, mScore).apply();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onStart() {
        super.onStart();
        out.println("GameActivity::onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        out.println("GameActivity::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        out.println("GameActivity::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        out.println("GameActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        out.println("GameActivity::onDestroy()");
    }
}