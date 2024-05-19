package org.androidtown.gympalai.layout;
import android.annotation.SuppressLint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.adapter.MessageAdapter;
import org.androidtown.gympalai.dao.AvatarDao;
import org.androidtown.gympalai.dao.ChatDao;
import org.androidtown.gympalai.dao.RankingDao;
import org.androidtown.gympalai.dao.ScoreDao;
import org.androidtown.gympalai.dao.UserDao;
import org.androidtown.gympalai.database.GymPalDB;
import org.androidtown.gympalai.entity.Avatar;
import org.androidtown.gympalai.entity.Chat;
import org.androidtown.gympalai.entity.HealthInfo;
import org.androidtown.gympalai.entity.Ranking;
import org.androidtown.gympalai.entity.Score;
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
import java.util.concurrent.ExecutionException;
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

    ImageView imageView;
    List<Message> messageList;
    MessageAdapter messageAdapter;

    int dietScore;
    int exerciseScore;
    //DB 생성
    GymPalDB db;

    LoginId loginId = new LoginId();

    String avatarDescription;

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
        imageView = view.findViewById(R.id.image_profile);

        // RecyclerView 설정
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);

        // 메시지 리스트 초기화 및 어댑터 설정
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);

        // 아바타 설정
        String avatarName = null;
        try {
            avatarName = new userAsyncTask(db.userDao()).execute(loginId.getLoginId()).get();
            if (avatarName != null) {
                Avatar avatar = new avatarAsyncTask(db.avatarDao()).execute(avatarName).get();
                avatarDescription=avatar.getDescription();
                byte[] image = avatar.getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageView.setImageBitmap(bitmap);
            }

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }




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
                // 식단 추천
                if(identifier=='1') new chatInsertAsyncTask(db.chatDao()).execute(new Chat(loginId.getLoginId(), LocalDateTime.now(), isQuestion, message,null,getFullNamesFromResponse(message) ));
                // 운동 추천
                else if(identifier=='2') new chatInsertAsyncTask(db.chatDao()).execute(new Chat(loginId.getLoginId(), LocalDateTime.now(), isQuestion, message,getFullNamesFromResponse(message), null ));
                // 식단 피드백
                else if(identifier=='3'){
                    new chatInsertAsyncTask(db.chatDao()).execute(new Chat(loginId.getLoginId(), LocalDateTime.now(), isQuestion, message,null,null ));
                    // score에다가 점수 계산해서 넣어야됨
                    List<String> nutrition = getSplitedNamesFromResponse(message);
                    db.healthInfoDao().getUserInfo(loginId.getLoginId()).observe(getActivity(), new Observer<HealthInfo>() {
                        @Override
                        public void onChanged(HealthInfo healthInfo) {
                            System.out.println("healthInfo = " + healthInfo);
                            double TDEE=getTDEE(healthInfo);
                            dietScore = 0;
                            // 탄수화물
                            dietScore += (int) (getDietScore(TDEE, 0.5, Double.parseDouble(nutrition.get(0))));
                            // 단백질
                            dietScore += (int) (getDietScore(TDEE, 0.2, Double.parseDouble(nutrition.get(1))));
                            // 지방
                            dietScore += (int) (getDietScore(TDEE, 0.3, Double.parseDouble(nutrition.get(2))));
                            Score score = null;
                            Ranking ranking = null;
                            try {
                                score = new scoreAsyncTask(2, 0, loginId.getLoginId(), db.scoreDao()).execute(new Score(LocalDateTime.now(), loginId.getLoginId(), 0)).get();
                                ranking = new rankingAsyncTask(2, 0, loginId.getLoginId(), db.rankingDao()).execute(new Ranking(LocalDateTime.now(), loginId.getLoginId(), 0)).get();
                                // 오늘 첫 기록이면
                                if(score==null) {
                                    new scoreAsyncTask(0, 0, loginId.getLoginId(), db.scoreDao()).execute(new Score(LocalDateTime.now(), loginId.getLoginId(), dietScore));
                                    new rankingAsyncTask(0, 0, loginId.getLoginId(), db.rankingDao()).execute(new Ranking(LocalDateTime.now(), loginId.getLoginId(), dietScore));
                                }
                                // 이미 오늘 기록한게 있으면
                                else if(score.getScore()+dietScore<1000) {
                                    new scoreAsyncTask(1, score.getScoreId(), loginId.getLoginId(), db.scoreDao()).execute(new Score(LocalDateTime.now(), loginId.getLoginId(), score.getScore()+dietScore));
                                    new rankingAsyncTask(1, ranking.getRankingId(), loginId.getLoginId(), db.rankingDao()).execute(new Ranking(LocalDateTime.now(), loginId.getLoginId(), ranking.getScore() + dietScore));
                                }

                                //System.out.println("score.toString() = " + score.toString());
                            } catch (ExecutionException e) {
                                throw new RuntimeException(e);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            // score에서 오늘의 점수를 가져옴


                            // if null -> 다 더한걸 insert

                            // else -> 가져오고 이거 더한걸로 update

                            // 계속 그 날의 스코어를 업데이트 하는 느낌으로다가
                            // 만약 오늘의 row를 가져왔는데 null이다? -> 0으로 채워 넣어
                            // 만약 0이 아니다? -> 식단 점수 가져오고 지금 계산한거 더해서 업데이트해놔
                            // 그러면 언제 가져와? -> 식단 점수 가져오기전에
                            // 없으면 만들고, 게산한거로 insert
                            // 있으면 가져오고 거따가 더해서 update
                        }
                    });

                }
                // 운동 피드백
                else if (identifier == '4') {
                    new chatInsertAsyncTask(db.chatDao()).execute(new Chat(loginId.getLoginId(), LocalDateTime.now(), isQuestion, message,null,null ));
                    // 운동 점수 계산
                    int sets = Integer.parseInt(getSplitedNamesFromResponse(message).get(0));
                    // 최대 세트 수는 30
                   if(sets >30) sets = 30;
                    exerciseScore = sets * 10;
                    Score score = null;
                    Ranking ranking = null;
                    try {
                        score = new scoreAsyncTask(2, 0, loginId.getLoginId(), db.scoreDao()).execute(new Score(LocalDateTime.now(), loginId.getLoginId(), 0)).get();
                        ranking = new rankingAsyncTask(2, 0, loginId.getLoginId(), db.rankingDao()).execute(new Ranking(LocalDateTime.now(), loginId.getLoginId(), 0)).get();
                        // 오늘 첫 기록이면
                        if(score==null) {
                            new scoreAsyncTask(0, 0, loginId.getLoginId(), db.scoreDao()).execute(new Score(LocalDateTime.now(), loginId.getLoginId(), exerciseScore));
                            new rankingAsyncTask(0, 0, loginId.getLoginId(), db.rankingDao()).execute(new Ranking(LocalDateTime.now(), loginId.getLoginId(), exerciseScore));
                        }
                        // 이미 오늘 기록한게 있으면
                        else if(score.getScore()+exerciseScore<1000) {
                            new scoreAsyncTask(1, score.getScoreId(), loginId.getLoginId(), db.scoreDao()).execute(new Score(LocalDateTime.now(), loginId.getLoginId(), score.getScore()+exerciseScore));
                            new rankingAsyncTask(1, ranking.getRankingId(), loginId.getLoginId(), db.rankingDao()).execute(new Ranking(LocalDateTime.now(), loginId.getLoginId(), ranking.getScore() + exerciseScore));
                        }

                        //System.out.println("score.toString() = " + score.toString());
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }


                }
                // 나머지
                else{
                    new chatInsertAsyncTask(db.chatDao()).execute(new Chat(loginId.getLoginId(), LocalDateTime.now(), isQuestion, message,null,null ));

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
    private String getFullNamesFromResponse(String response) {
        Pattern pattern = Pattern.compile("\\[([^\\[\\]]+)\\]");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return "";
        }
    }

    private List<String> getSplitedNamesFromResponse(String response) {
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(response);
        List<String> infoNames = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group(1);
            String[] names = group.split(",");
            for (String name : names) {
                infoNames.add(name.trim());
            }
        }
        return infoNames;
    }



    void callAPI(String question)  {
        //okhttp
        messageList.add(new Message("...", Message.SENT_BY_BOT));

        String script = "너는 운동과 식단 관련된 얘기만 해야해. 식단 추천과 관련된 답변 끝에는 []안에 추천한 음식의 이름을 ','로 구분해서 넣고 마지막에 #1을 붙여. 운동 추천과 관련된 답변 끝에는 []안에 추천한 운동의 이름을 ','로 구분해서 넣고 마지막에 #2를 붙여. 추천하는 음식은 3개, 운동은 5개여야해. " +
                "유저가 자신이 먹은 음식들을 말하면 너는 유저가 건강한 식사를 했는지 피드백을 해줘야해. 그리고 그 음식들에 들어있는 탄수화물, 단백질, 지방의 순서대로 각각의 양을 더해서 그램 단위로 답변 끝에 []안에 ','로 구분해서 숫자로만 넣고 마지막에 #3을 붙여." +
                "유저가 자신이 한 운동 이름들과 수행한 세트 수를 말하면 너는 유저가 운동을 잘 했는지 피드백을 해줘야해. 참고로 모든 운동은 6세트 수행해야 해. 그리고 유저가 수행한 운동들의 세트 수를 모두 더해 서 답변 끝에 []안에 숫자로만 넣고 마지막에 #4를 붙여.";

        //추가된 내용
        JSONArray arr = new JSONArray();
        JSONObject baseAi = new JSONObject();
        JSONObject userMsg = new JSONObject();
        try {
            //AI 속성설정
            baseAi.put("role", "user");
            baseAi.put("content", "너는 지금부터 내가 설명하는 트레이너가 돼서 유저들을 가르치는거야."+ avatarDescription + script);
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

    private double getTDEE(HealthInfo userInfo) {
        double TDEE;
        if (userInfo.isGender() == true) TDEE = 88.362 + (13.397 * userInfo.getWeight()) + (4.799 * userInfo.getHeight()) - (5.677 * userInfo.getAge());
        else {
            TDEE = 447.593 + (9.247 * userInfo.getWeight()) + (3.098 * userInfo.getHeight()) - (4.330 * userInfo.getAge());
        }
        switch (userInfo.getActivity()) {
            case 0:
                TDEE *= 1.2;
                break;
            case 1:
                TDEE *= 1.375;
                break;
            case 2:
                TDEE *= 1.55;
                break;
            case 3:
                TDEE *= 1.725;
                break;
            default:
                TDEE *= 1.9;
                break;
        }

        if (userInfo.getPurpose() == 0) TDEE -= 500;
        else if(userInfo.getPurpose()==2) TDEE += 500;

        return TDEE;
    }

    private int getDietScore(double tdee, double ratio, double actualIntake){
        int gperKcal = (ratio == 0.5 || ratio == 0.2) ? 4 : 9;
        double recommendedIntake = (tdee * ratio) / gperKcal;
        int score = (int) (-(700 * ratio) / Math.pow(recommendedIntake, 2) * Math.pow(actualIntake - recommendedIntake, 2) + (700 * ratio));

        return score;

    }




    //메인스레드에서 데이터베이스에 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public static class chatInsertAsyncTask extends AsyncTask<Chat, Void, Void> {
        private ChatDao chatDao;

        public  chatInsertAsyncTask(ChatDao chatDao){
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

    //메인스레드에서 데이터베이스에 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public static class scoreAsyncTask extends AsyncTask<Score, Void, Score> {
        private ScoreDao scoreDao;
        private int identifier;

        private long scoreId;

        private String userId;

        public  scoreAsyncTask(int identifier, long scoreId, String userId, ScoreDao scoreDao){
            this.scoreDao = scoreDao;
            this.identifier = identifier;
            this.scoreId = scoreId;
            this.userId = userId;
        }

        @Override //백그라운드작업(메인스레드 X)
        protected Score doInBackground(Score...scores) {
            // insert
            if(identifier==0 && scores!=null)   scoreDao.insert(scores[0]);
            // update
            else if(identifier==1 && scores!=null)   {
                scoreDao.updateScore(scoreId, scores[0].getScore());
            }
            // select
            else {
                Score score = scoreDao.getScore(scores[0].getUserId());
                return score;
            }

            return null;

        }
    }

    public static class rankingAsyncTask extends AsyncTask<Ranking, Void, Ranking> {

        private RankingDao rankingDao;

        private int identifier;

        private long rankingId;

        private String userId;

        public rankingAsyncTask(int identifier, long rankingId, String userId,RankingDao rankingDao) {
            this.rankingDao = rankingDao;
            this.identifier = identifier;
            this.rankingId = rankingId;
            this.userId = userId;
        }

        @Override //백그라운드작업(메인스레드 X)
        protected Ranking doInBackground(Ranking ... rankings) {
            System.out.println("identifier = " + identifier);
            // insert
            if(identifier==0 && rankings!=null)   rankingDao.insert(rankings[0]);
            // update
            else if(identifier==1 && rankings!=null)   {
                System.out.println("rankingId = " + rankingId);
                System.out.println("scores[0].getScore() = " + rankings[0].getScore());
                rankingDao.updateRanking(rankingId, rankings[0].getScore());
            }
            else {
                Ranking ranking = rankingDao.getRanking(rankings[0].getUserId());
                return ranking;
            }

            return null;

        }

    }

    //메인스레드에서 데이터베이스에 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public static class avatarAsyncTask extends AsyncTask<String, Void, Avatar> {

        private AvatarDao avatarDao;

        public avatarAsyncTask(AvatarDao avatarDao) {
            this.avatarDao = avatarDao;
        }

        @Override //백그라운드작업(메인스레드 X)
        protected Avatar doInBackground(String ...avatarNames) {
            try{
                if(avatarNames[0]!=null) {
                    Avatar avatar = avatarDao.getAvatar(avatarNames[0]);
                    return avatar;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;

        }
    }

    //메인스레드에서 데이터베이스에 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public static class userAsyncTask extends AsyncTask<String, Void, String> {

        private UserDao userDao;

        public userAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override //백그라운드작업(메인스레드 X)
        protected String doInBackground(String ...userIds) {
            try{
                if(userIds[0]!=null) {
                    String avatarName = userDao.getAvatarName(userIds[0]);
                    return avatarName;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;

        }
    }




}

