package com.shivam.portfolio;

import android.content.ActivityNotFoundException;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class ResearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);

        drawerLayout = findViewById(R.id.drawerLayoutResearch);
        findViewById(R.id.btnMenuResearch).setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        NavigationView navigationView = findViewById(R.id.navViewResearch);
        navigationView.setNavigationItemSelectedListener(this);

        findViewById(R.id.btnResearchView).setOnClickListener(view -> openUrl(getString(R.string.url_research_view)));
        findViewById(R.id.btnResearchDownload)
                .setOnClickListener(view -> downloadPdf(getString(R.string.url_research_pdf)));
        findViewById(R.id.btnResearchHome).setOnClickListener(view -> openHome());
        findViewById(R.id.btnResearchNext).setOnClickListener(view -> openResume());

        setupPdfPreview();
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
            startActivity(new Intent(this, ExperienceActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (itemId == R.id.nav_projects) {
            startActivity(new Intent(this, ProjectActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (itemId == R.id.nav_research) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (itemId == R.id.nav_resume) {
            openResume();
            return true;
        }
        if (itemId == R.id.nav_contact) {
            openContact();
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

    @Override
    protected void onDestroy() {
        WebView webView = findViewById(R.id.researchWebView);
        webView.stopLoading();
        webView.destroy();
        super.onDestroy();
    }

    private void setupPdfPreview() {
        WebView webView = findViewById(R.id.researchWebView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient());

        String pdfUrl = getString(R.string.url_research_pdf);
        String embeddedViewerUrl = "https://drive.google.com/viewerng/viewer?embedded=1&url=" + Uri.encode(pdfUrl);
        webView.loadUrl(embeddedViewerUrl);
    }

    private void openHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void openContact() {
        startActivity(new Intent(this, ContactActivity.class));
    }

    private void openResume() {
        startActivity(new Intent(this, ResumeActivity.class));
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "No supported app found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadPdf(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("EV_Research_Paper.pdf");
        request.setDescription("Downloading research paper");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "EV_Research_Paper.pdf");

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
            Toast.makeText(this, "Download started.", Toast.LENGTH_SHORT).show();
        } else {
            openUrl(url);
        }
    }
}
