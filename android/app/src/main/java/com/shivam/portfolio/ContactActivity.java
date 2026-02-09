package com.shivam.portfolio;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class ContactActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private EditText inputName;
    private EditText inputEmail;
    private EditText inputSubject;
    private EditText inputMessage;
    private MaterialButton btnSendMessage;
    private final ExecutorService networkExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        drawerLayout = findViewById(R.id.drawerLayoutContact);
        findViewById(R.id.btnMenuContact).setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        findViewById(R.id.btnContactHome).setOnClickListener(view -> openHome());

        NavigationView navigationView = findViewById(R.id.navViewContact);
        navigationView.setNavigationItemSelectedListener(this);

        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputSubject = findViewById(R.id.inputSubject);
        inputMessage = findViewById(R.id.inputMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);

        btnSendMessage.setOnClickListener(view -> onSendClicked());
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
            startActivity(new Intent(this, ResumeActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if (itemId == R.id.nav_contact) {
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

    @Override
    protected void onDestroy() {
        networkExecutor.shutdownNow();
        super.onDestroy();
    }

    private void onSendClicked() {
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String subject = inputSubject.getText().toString().trim();
        String message = inputMessage.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, getString(R.string.contact_fill_all), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, getString(R.string.contact_invalid_email), Toast.LENGTH_SHORT).show();
            return;
        }

        setSendingState(true);
        networkExecutor.execute(() -> sendContactRequest(name, email, subject, message));
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void setSendingState(boolean sending) {
        btnSendMessage.setEnabled(!sending);
        btnSendMessage.setText(sending ? getString(R.string.contact_sending) : getString(R.string.contact_send));
    }

    private void sendContactRequest(String name, String email, String subject, String message) {
        HttpURLConnection connection = null;
        try {
            JSONObject payload = new JSONObject();
            payload.put("name", name);
            payload.put("email", email);
            payload.put("subject", subject);
            payload.put("message", message);

            URL url = new URL(getString(R.string.contact_endpoint));
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] body = payload.toString().getBytes(StandardCharsets.UTF_8);
                outputStream.write(body, 0, body.length);
            }

            int responseCode = connection.getResponseCode();
            boolean success = responseCode >= 200 && responseCode < 300;
            String responseMessage = readResponseBody(connection, success);
            final String finalResponseMessage = responseMessage;

            runOnUiThread(() -> {
                setSendingState(false);
                if (success) {
                    inputName.setText("");
                    inputEmail.setText("");
                    inputSubject.setText("");
                    inputMessage.setText("");
                    Toast.makeText(this, getString(R.string.contact_success), Toast.LENGTH_SHORT).show();
                } else {
                    String errorText = finalResponseMessage;
                    if (errorText == null || errorText.isEmpty()) {
                        errorText = getString(R.string.contact_failed);
                    }
                    Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ignored) {
            runOnUiThread(() -> {
                setSendingState(false);
                Toast.makeText(this, getString(R.string.contact_failed), Toast.LENGTH_SHORT).show();
            });
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String readResponseBody(HttpURLConnection connection, boolean success) {
        try {
            InputStream stream = success ? connection.getInputStream() : connection.getErrorStream();
            if (stream == null) {
                return null;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                String raw = builder.toString();
                if (raw.isEmpty()) {
                    return null;
                }
                JSONObject json = new JSONObject(raw);
                return json.optString("message", null);
            }
        } catch (Exception ignored) {
            return null;
        }
    }

    private void openHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
