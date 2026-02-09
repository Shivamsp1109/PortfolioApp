package com.shivam.portfolio;

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

public class ResumeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        drawerLayout = findViewById(R.id.drawerLayoutResume);
        findViewById(R.id.btnMenuResume).setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        NavigationView navigationView = findViewById(R.id.navViewResume);
        navigationView.setNavigationItemSelectedListener(this);

        findViewById(R.id.btnResumeDownload)
                .setOnClickListener(view -> downloadPdf(getString(R.string.url_resume_pdf), "ShivamKumarPandey.pdf"));
        findViewById(R.id.btnResumeHome).setOnClickListener(view -> openHome());
        findViewById(R.id.btnResumeNext).setOnClickListener(view -> openContact());

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
            startActivity(new Intent(this, ResearchActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (itemId == R.id.nav_resume) {
            drawerLayout.closeDrawer(GravityCompat.START);
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
        WebView webView = findViewById(R.id.resumeWebView);
        webView.stopLoading();
        webView.destroy();
        super.onDestroy();
    }

    private void setupPdfPreview() {
        WebView webView = findViewById(R.id.resumeWebView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient());

        String pdfUrl = getString(R.string.url_resume_pdf);
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

    private void downloadPdf(String url, String fileName) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(fileName);
        request.setDescription("Downloading resume");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
            Toast.makeText(this, "Download started.", Toast.LENGTH_SHORT).show();
        }
    }
}
