package com.shivam.portfolio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class AboutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String PREFS_NAME = "portfolio_game";
    private static final String PREF_BEST_SCORE = "best_tap_score";
    private static final String PREF_VISITED_ABOUT = "visited_about";
    private static final int BASE_TIMER_SECONDS = 30;

    private DrawerLayout drawerLayout;
    private TextView gameScoreText;
    private TextView gameBestText;
    private TextView gameTimerText;
    private TextView gameStatusText;
    private TapDotGameView tapDotGameView;
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private int remainingSeconds = BASE_TIMER_SECONDS;
    private int score = 0;
    private int bestScore = 0;
    private int lastMilestone = 0;
    private boolean timerRunning = false;
    private int gameOverCountdownSeconds = 0;

    private final Runnable timerRunnable =
            new Runnable() {
                @Override
                public void run() {
                    if (!timerRunning) {
                        return;
                    }
                    remainingSeconds--;
                    updateTimerLabel();

                    if (remainingSeconds <= 0) {
                        timerRunning = false;
                        tapDotGameView.setGameActive(false);
                        gameStatusText.setVisibility(View.VISIBLE);
                        gameOverCountdownSeconds = 3;
                        updateTimerLabel();
                        timerHandler.post(gameOverRunnable);
                        return;
                    }
                    timerHandler.postDelayed(this, 1000);
                }
            };

    private final Runnable gameOverRunnable =
            new Runnable() {
                @Override
                public void run() {
                    if (gameOverCountdownSeconds <= 0) {
                        gameStatusText.setVisibility(View.GONE);
                        startNewRound();
                        return;
                    }
                    updateTimerLabel();
                    gameOverCountdownSeconds--;
                    timerHandler.postDelayed(this, 1000);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        drawerLayout = findViewById(R.id.drawerLayoutAbout);
        findViewById(R.id.btnMenuAbout).setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        NavigationView navigationView = findViewById(R.id.navViewAbout);
        navigationView.setNavigationItemSelectedListener(this);

        findViewById(R.id.btnAboutHome).setOnClickListener(view -> openHome());
        findViewById(R.id.btnAboutNext).setOnClickListener(view -> openExperience());

        gameScoreText = findViewById(R.id.gameScoreText);
        gameBestText = findViewById(R.id.gameBestText);
        gameTimerText = findViewById(R.id.gameTimerText);
        gameStatusText = findViewById(R.id.gameStatusText);
        tapDotGameView = findViewById(R.id.tapDotGameView);

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        preferences.edit().putBoolean(PREF_VISITED_ABOUT, true).apply();
        bestScore = preferences.getInt(PREF_BEST_SCORE, 0);
        gameBestText.setText(getString(R.string.game_best_score, bestScore));
        gameScoreText.setText(getString(R.string.game_score, 0));
        gameTimerText.setText(getString(R.string.game_timer, BASE_TIMER_SECONDS));

        tapDotGameView.setOnScoreChangeListener(
                newScore -> {
                    score = newScore;
                    gameScoreText.setText(getString(R.string.game_score, score));

                    if (score > bestScore) {
                        bestScore = score;
                        preferences.edit().putInt(PREF_BEST_SCORE, bestScore).apply();
                        gameBestText.setText(getString(R.string.game_best_score, bestScore));
                    }

                    int milestone = score / 5;
                    if (milestone > lastMilestone) {
                        int gainedMilestones = milestone - lastMilestone;
                        remainingSeconds =
                                Math.min(BASE_TIMER_SECONDS, remainingSeconds + (10 * gainedMilestones));
                        lastMilestone = milestone;
                        tapDotGameView.increaseDifficultyStep();
                        updateTimerLabel();
                    }
                });

        startNewRound();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            openHome();
            return true;
        }
        if (itemId == R.id.nav_about) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (itemId == R.id.nav_experience) {
            openExperience();
            return true;
        }
        if (itemId == R.id.nav_projects) {
            startActivity(new Intent(this, ProjectActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (itemId == R.id.nav_research) {
            startActivity(new Intent(this, ResearchActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (itemId == R.id.nav_resume) {
            startActivity(new Intent(this, ResumeActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (itemId == R.id.nav_contact) {
            startActivity(new Intent(this, ContactActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    private void openHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void openExperience() {
        startActivity(new Intent(this, ExperienceActivity.class));
    }

    private void startNewRound() {
        timerHandler.removeCallbacks(timerRunnable);
        timerHandler.removeCallbacks(gameOverRunnable);
        score = 0;
        lastMilestone = 0;
        gameOverCountdownSeconds = 0;
        remainingSeconds = BASE_TIMER_SECONDS;
        gameStatusText.setVisibility(View.GONE);
        gameScoreText.setText(getString(R.string.game_score, 0));
        updateTimerLabel();
        tapDotGameView.resetGameState();
        tapDotGameView.setGameActive(true);
        timerRunning = true;
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    private void updateTimerLabel() {
        int displaySeconds =
                gameOverCountdownSeconds > 0 ? gameOverCountdownSeconds : Math.max(0, remainingSeconds);
        gameTimerText.setText(getString(R.string.game_timer, displaySeconds));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerRunning = false;
        timerHandler.removeCallbacksAndMessages(null);
    }
}
