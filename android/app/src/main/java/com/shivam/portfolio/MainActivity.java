package com.shivam.portfolio;

import android.content.ActivityNotFoundException;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String PREFS_NAME = "portfolio_game";
    private static final String PREF_VISITED_ABOUT = "visited_about";
    private static final String PREF_SESSION_ACTIVE = "session_active";
    private static final String PREF_POPUP_SHOWN = "popup_shown_in_session";
    private static boolean processAlive = false;

    private DrawerLayout drawerLayout;
    private long lastBackPressMs = 0L;
    private AlertDialog gamePromptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!processAlive) {
            processAlive = true;
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .edit()
                    .putBoolean(PREF_SESSION_ACTIVE, false)
                    .apply();
        }
        startSessionIfNeeded();

        drawerLayout = findViewById(R.id.drawerLayout);
        findViewById(R.id.btnMenuHome).setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        setupExternalLink(R.id.btnLinkedin, getString(R.string.url_linkedin));
        setupExternalLink(R.id.btnGithub, getString(R.string.url_github));
        setupExternalLink(R.id.btnCodeforces, getString(R.string.url_codeforces));
        setupExternalLink(R.id.btnInstagram, getString(R.string.url_instagram));
        setupEmailLink(R.id.btnMailMe);
        setupCallLink(R.id.btnCallMe);
        findViewById(R.id.btnOpenMenu).setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        getOnBackPressedDispatcher()
                .addCallback(
                        this,
                        new OnBackPressedCallback(true) {
                            @Override
                            public void handleOnBackPressed() {
                                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                    return;
                                }

                                if (isGamePromptVisible()) {
                                    closePromptAndHandleBack();
                                    return;
                                }

                                if (!hasVisitedAbout() && !hasPopupShownInCurrentSession()) {
                                    showGamePrompt();
                                    return;
                                }

                                handleDoubleBackExit();
                            }
                        });
    }

    private void handleDoubleBackExit() {
        long now = System.currentTimeMillis();
        if (now - lastBackPressMs < 2000) {
            endSession();
            finish();
            return;
        }

        lastBackPressMs = now;
        Toast.makeText(
                        MainActivity.this,
                        getString(R.string.back_again_exit),
                        Toast.LENGTH_SHORT)
                .show();
    }

    private boolean hasVisitedAbout() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.getBoolean(PREF_VISITED_ABOUT, false);
    }

    private void startSessionIfNeeded() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (!preferences.getBoolean(PREF_SESSION_ACTIVE, false)) {
            preferences
                    .edit()
                    .putBoolean(PREF_SESSION_ACTIVE, true)
                    .putBoolean(PREF_POPUP_SHOWN, false)
                    .putBoolean(PREF_VISITED_ABOUT, false)
                    .apply();
        }
    }

    private void endSession() {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putBoolean(PREF_SESSION_ACTIVE, false)
                .putBoolean(PREF_POPUP_SHOWN, false)
                .apply();
    }

    private boolean hasPopupShownInCurrentSession() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.getBoolean(PREF_POPUP_SHOWN, false);
    }

    private boolean isGamePromptVisible() {
        return gamePromptDialog != null && gamePromptDialog.isShowing();
    }

    private void showGamePrompt() {
        if (isGamePromptVisible()) {
            return;
        }
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().putBoolean(PREF_POPUP_SHOWN, true).apply();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_game_prompt, null, false);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setOnKeyListener(
                (DialogInterface d, int keyCode, KeyEvent event) -> {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                        closePromptAndHandleBack();
                        return true;
                    }
                    return false;
                });

        dialogView.findViewById(R.id.btnClosePrompt).setOnClickListener(v -> dialog.dismiss());
        dialogView.findViewById(R.id.btnPromptPlay)
                .setOnClickListener(
                        v -> {
                            dialog.dismiss();
                            startActivity(new Intent(this, AboutActivity.class));
                        });
        dialogView.findViewById(R.id.btnPromptExit)
                .setOnClickListener(
                        v -> {
                            dialog.dismiss();
                            endSession();
                            finish();
                        });

        dialog.setOnDismissListener(d -> gamePromptDialog = null);
        gamePromptDialog = dialog;
        dialog.show();
    }

    private void closePromptAndHandleBack() {
        if (gamePromptDialog != null && gamePromptDialog.isShowing()) {
            gamePromptDialog.dismiss();
        }
        handleDoubleBackExit();
    }

    @Override
    protected void onDestroy() {
        if (gamePromptDialog != null && gamePromptDialog.isShowing()) {
            gamePromptDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (item.getItemId() == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (item.getItemId() == R.id.nav_experience) {
            startActivity(new Intent(this, ExperienceActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (item.getItemId() == R.id.nav_projects) {
            startActivity(new Intent(this, ProjectActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (item.getItemId() == R.id.nav_research) {
            startActivity(new Intent(this, ResearchActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (item.getItemId() == R.id.nav_resume) {
            startActivity(new Intent(this, ResumeActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (item.getItemId() == R.id.nav_contact) {
            startActivity(new Intent(this, ContactActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        Toast.makeText(
                        this,
                        getString(R.string.section_coming_soon, item.getTitle()),
                        Toast.LENGTH_SHORT)
                .show();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupExternalLink(int viewId, String url) {
        findViewById(viewId).setOnClickListener(view -> openUrl(url));
    }

    private void setupEmailLink(int viewId) {
        findViewById(viewId).setOnClickListener(view -> {
            String uri =
                    "mailto:"
                            + Uri.encode(getString(R.string.email_label))
                            + "?subject="
                            + Uri.encode(getString(R.string.email_subject))
                            + "&body="
                            + Uri.encode(getString(R.string.email_body));
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
            safeStartIntent(Intent.createChooser(emailIntent, getString(R.string.btn_mail_me)));
        });
    }

    private void setupCallLink(int viewId) {
        findViewById(viewId).setOnClickListener(view -> {
            String phone = getString(R.string.phone_label).replace(" ", "");
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            safeStartIntent(Intent.createChooser(dialIntent, getString(R.string.btn_call_me)));
        });
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        safeStartIntent(intent);
    }

    private void safeStartIntent(Intent intent) {
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "No supported app found.", Toast.LENGTH_SHORT).show();
        }
    }
}
