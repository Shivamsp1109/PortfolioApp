package com.shivam.portfolio;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class ExperienceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);

        drawerLayout = findViewById(R.id.drawerLayoutExperience);
        findViewById(R.id.btnMenuExperience).setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        NavigationView navigationView = findViewById(R.id.navViewExperience);
        navigationView.setNavigationItemSelectedListener(this);

        findViewById(R.id.btnExperienceHome).setOnClickListener(view -> openHome());
        findViewById(R.id.btnExperienceNext).setOnClickListener(view -> openProjects());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            openHome();
            return true;
        }
        if (itemId == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (itemId == R.id.nav_experience) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (itemId == R.id.nav_projects) {
            openProjects();
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

    private void openProjects() {
        startActivity(new Intent(this, ProjectActivity.class));
    }
}
