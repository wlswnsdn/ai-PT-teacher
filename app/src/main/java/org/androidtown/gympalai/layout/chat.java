package org.androidtown.gympalai.layout;
import android.annotation.SuppressLint;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.adapter.MessageAdapter;
import org.androidtown.gympalai.dao.ChatDao;
import org.androidtown.gympalai.dao.UserDao;
import org.androidtown.gympalai.database.GymPalDB;
import org.androidtown.gympalai.entity.Chat;
import org.androidtown.gympalai.entity.User;
import org.androidtown.gympalai.model.LoginId;
import org.androidtown.gympalai.model.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class chat extends Fragment {


    //TODO 아바타 프로필, 이름 생성 메서드 추가

    RecyclerView recyclerView;
    EditText etMsg;
    TextView tvWelcome;
    ImageButton btnSend;

    List<Message> messageList;
    MessageAdapter messageAdapter;


    //DB 생성
    GymPalDB db;

    LoginId loginId = new LoginId();


    private static final String api_key = "sk-proj-5h1uVBeVPFcBqzoibAlUT3BlbkFJoWYnw4fzTEHfeDs9RuFv";


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 레이아웃 파일을 inflate하여 View 생성
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        //DB 생성
        db = GymPalDB.getInstance(getActivity());


        // View에서 각 UI 컴포넌트 찾기
        recyclerView = view.findViewById(R.id.recycler_view);
        tvWelcome = view.findViewById(R.id.tv_welcome);
        etMsg = view.findViewById(R.id.et_msg);
        btnSend = view.findViewById(R.id.btn_send);

        // RecyclerView 설정
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);

        // 메시지 리스트 초기화 및 어댑터 설정
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);


        // db의 이전 메시지 list에 추가

        db.chatDao().getAll(loginId.getLoginId()).observe(getViewLifecycleOwner(), new Observer<List<Chat>>() {
            @Override
            public void onChanged(List<Chat> chatList) {
                updateRecyclerView(chatList);
            }
        });

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);



        // 버튼 클릭 이벤트 리스너 설정
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = etMsg.getText().toString().trim();
                addToChat(question, Message.SENT_BY_ME);
                etMsg.setText("");
                callAPI(question);
                tvWelcome.setVisibility(View.GONE);
            }
        });

        return view;
    }


    private void updateRecyclerView(List<Chat> chatList){
        messageList.clear(); // 기존 데이터를 지웁니다.
        if (chatList != null) {
            for (Chat chat : chatList) {
                String sentBy = (chat.getQuestion()) ? "me" : "bot";
                Message message = new Message(chat.getMessage(), sentBy);
                messageList.add(message);
            }
        }
        messageAdapter.notifyDataSetChanged(); // RecyclerView를 업데이트합니다.
    }


    //  채팅 목록에 메세지 추가
    @SuppressLint("NotifyDataSetChanged")
    void addToChat(String message, String sentBy) {
        getActivity().runOnUiThread(() -> {
            messageList.add(new Message(message, sentBy));
            char identifier = message.charAt(message.length() - 1);

            // db에도 message 저장
            // sentBy==SENT_BY_BOT -> isQuestion false로
            // sentBy==SENT_BY_ME -> isQuestion true로
            System.out.println("message = " + message);
            Boolean isQuestion = (sentBy.compareTo("me") == 0) ? true : false;
            System.out.println("isQuestion = " + isQuestion);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 답변이면 #숫자에 따라 식단/운동 칸에 저장
                if(identifier=='1') new InsertAsyncTask(db.chatDao()).execute(new Chat(loginId.getLoginId(), LocalDateTime.now(), isQuestion, message,null,getNamesFromResponse(message) ));
                else if(identifier=='2') new InsertAsyncTask(db.chatDao()).execute(new Chat(loginId.getLoginId(), LocalDateTime.now(), isQuestion, message,getNamesFromResponse(message), null ));
                // 질문, 피드백관련 답변은 추천 받은 거 list 칸 다 null로 넣고 저장
                else {
                    new InsertAsyncTask(db.chatDao()).execute(new Chat(loginId.getLoginId(), LocalDateTime.now(), isQuestion, message,null,null ));
                }
            }
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
        });
    }

    void addResponse(String response){
        messageList.remove(messageList.size()-1);
        addToChat(response, Message.SENT_BY_BOT);
    }

    // #뒤의 식별자에 따라 운동, 식단 이름 파싱
    private HashMap<Character, String> getInfoFromResponse(String response) {
        char identifier = response.charAt(response.length() - 1);
        String infoList = getNamesFromResponse(response);
        return new HashMap<Character, String>() {{
            put(identifier, infoList);
        }};
    }


    private String getNamesFromResponse(String response) {
        Pattern pattern = Pattern.compile("\\[([^\\[\\]]+)\\]");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return ""; // 대괄호로 둘러싸인 부분이 없는 경우 빈 문자열 반환
        }
    }



    void callAPI(String question) {
        //okhttp
        messageList.add(new Message("...", Message.SENT_BY_BOT));

        String script = "너는 운동과 식단 관련된 얘기만 해야해. 식단 추천과 관련된 답변 끝에는 []안에 추천한 음식의 이름을 ','로 구분해서 넣고 마지막에 #1을 붙여. 운동 추천과 관련된 답변 끝에는 []안에 추천한 운동의 이름을 ','로 구분해서 넣고 마지막에 #2를 붙여. 추천하는 음식이나 운동은 최소 3개여야해. " +
                "유저가 자신이 먹은 음식들을 말하면 너는 유저가 건강한 식사를 했는지 피드백을 해줘야해. 그리고 그 음식들에 들어있는 탄수화물, 단백질, 지방의 양을 각각 더해서 그램 단위로 답변 끝에 []안에 ','로 구분해서 숫자로만 넣고 마지막에 #3을 붙여." +
                "";

        //추가된 내용
        JSONArray arr = new JSONArray();
        JSONObject baseAi = new JSONObject();
        JSONObject userMsg = new JSONObject();
        try {
            //AI 속성설정
            baseAi.put("role", "user");
            baseAi.put("content", script);
            //유저 메세지
            userMsg.put("role", "user");
            userMsg.put("content", question);
            //array로 담아서 한번에 보낸다
            arr.put(baseAi);
            arr.put(userMsg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JSONObject object = new JSONObject();
        try {

            object.put("model", "gpt-4-turbo");
            object.put("messages", arr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(object.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")  //url 경로 수정됨
                .header("Authorization", "Bearer " + api_key)
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");

                        String result = jsonArray.getJSONObject(0).getJSONObject("message").getString("content");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    addResponse("Failed to load response due to " + response.body().string());
                }
            }


        });
    }


    //메인스레드에서 데이터베이스에 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public static class InsertAsyncTask extends AsyncTask<Chat, Void, Void> {
        private ChatDao chatDao;

        public  InsertAsyncTask(ChatDao chatDao){
            this.chatDao = chatDao;
        }

        @Override //백그라운드작업(메인스레드 X)
        protected Void doInBackground(Chat... chats) {
            //추가만하고 따로 SELECT문을 안해도 라이브데이터로 인해
            //getAll()이 반응해서 데이터를 갱신해서 보여줄 것이다,  메인액티비티에 옵저버에 쓴 코드가 실행된다. (라이브데이터는 스스로 백그라운드로 처리해준다.)
            try {
                if (chats != null && chats.length > 0) {
                    chatDao.insert(chats[0]);
                }
            } catch (Exception e) {
                System.out.println("AsyncTask Exception occurred: " + e.getMessage());
            }
            return null;

        }
    }



}

