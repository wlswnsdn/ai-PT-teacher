package org.androidtown.gympalai;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatGPTActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.for_chatgpt_test);

        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String question = editText.getText().toString();
                ChatGPTAsyncTask task = new ChatGPTAsyncTask();
                task.execute(question);
                editText.setText("");
            }
        });
    }




    public class ChatGPTAsyncTask extends AsyncTask<String, Void, String> {

        public ChatGPTAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            String userPrompt = params[0];
            return chatGPT(userPrompt);
        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }
    }

    private String chatGPT(String prompt) {
// Replace "YOUR_API_KEY_HERE" with your actual OpenAI API key
        String apiKey = "sk-proj-5h1uVBeVPFcBqzoibAlUT3BlbkFJoWYnw4fzTEHfeDs9RuFv";
        String model = "gpt-4-turbo";
        String url = "https://api.openai.com/v1/chat/completions";

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

//            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "Show me an emoji from my memory. Never engage in conversation and only show one emoji."+ "\"}]}";
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"assistant\", \"content\": \"" + prompt + "Your response should not over 50 letters." +"\"}]}";
            connection.setDoOutput(true);
            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                writer.write(body);
                writer.flush();
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                return extractMessageFromJSONResponse(response.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "Error communicating with ChatGPT" + e.getMessage();
        }
    }

    private String
    extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content") + 11;
        int end = response.indexOf("\"", start);
        return response.substring(start, end);
    }
}

