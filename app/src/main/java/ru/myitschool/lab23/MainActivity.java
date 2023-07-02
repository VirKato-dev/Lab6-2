package ru.myitschool.lab23;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ru.myitschool.lab23.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.container.sendButton.setOnClickListener(view -> sendMessage());
    }

    private void sendMessage() {
        String url = binding.container.urlText.getText().toString();
        String params = binding.container.queryParameter.getText().toString();

        // Асинхронно выполнить запрос (не зависать в ожидании ответа)
        AsyncTask.execute(() -> {
            URL serverAPI;
            try {
                serverAPI = new URL(url + "?" + "cta=" + params);
                HttpsURLConnection myConnection = (HttpsURLConnection) serverAPI.openConnection();
                //  myConnection.setRequestMethod("GET");
                StringBuilder resp = new StringBuilder();
                if (myConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // ответ получен успешно
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        resp.append(line);
                    }
                    // вывести ответ в TextView
                    binding.container.resultText.setText(resp.toString());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
