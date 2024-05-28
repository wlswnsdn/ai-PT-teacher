package org.androidtown.gympalai.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.backmethod.LoginFunction;
import org.androidtown.gympalai.dao.AvatarDao;
import org.androidtown.gympalai.dao.RankingDao;
import org.androidtown.gympalai.dao.ScoreDao;
import org.androidtown.gympalai.dao.UserDao;
import org.androidtown.gympalai.database.GymPalDB;
import org.androidtown.gympalai.entity.Avatar;

import org.androidtown.gympalai.entity.Ranking;
import org.androidtown.gympalai.entity.User;
import org.androidtown.gympalai.model.CircularProgressView;
import org.androidtown.gympalai.model.UserTotalScore;
import org.androidtown.gympalai.worker.SeasonUpdateWorker;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


public class home extends Fragment {

    ImageView silver, bronze, learderboardImage;
    ImageView firstImage, secondImage, thirdImage; //1, 2, 3등 사용자 이미지뷰
    CircularProgressView personalScore;  // 개인 점수 표시 뷰
    RecyclerView recyclerView;  // 리더보드 리사이클러뷰
    TextView firstPlace, secondPlace, thirdPlace;  // 1, 2, 3등 사용자 이름 텍스트뷰
    TextView firstPlaceScore, secondPlaceScore, thirdPlaceScore;  // 1, 2, 3등 사용자 점수 텍스트뷰
    TextView enough;
    LoginFunction loginFunction = new LoginFunction();

    private static String currentUser; // 현재 사용자 아이디를 user6으로 설정, 이부분 가져와야함.

    GymPalDB db;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 뷰 초기화
        silver = view.findViewById(R.id.silvercrown);
        bronze = view.findViewById(R.id.bronzecrown);
        learderboardImage = view.findViewById(R.id.leaderboard_image); //리더보드 내 사진

        firstImage = view.findViewById(R.id.firstImage);  //1등 사진
        secondImage = view.findViewById(R.id.secondImage); //2등 사진
        thirdImage = view.findViewById(R.id.thirdImage); //3등 사진

        personalScore = view.findViewById(R.id.score);
        recyclerView = view.findViewById(R.id.leaderboard);
        firstPlace = view.findViewById(R.id.userfirst);
        secondPlace = view.findViewById(R.id.usersecond);
        thirdPlace = view.findViewById(R.id.userthird);
        firstPlaceScore = view.findViewById(R.id.scorefirst);
        secondPlaceScore = view.findViewById(R.id.scoresecond);
        thirdPlaceScore = view.findViewById(R.id.scorethird);
        enough = view.findViewById(R.id.enough);

        silver.setColorFilter(Color.parseColor("#A5A9B4"));
        bronze.setColorFilter(Color.parseColor("#B08D57"));
        // 2주마다 ranking 초기화
        seasonUpdate();

        currentUser = loginFunction.getMyId();

        //DB 생성
        db = GymPalDB.getInstance(getActivity());

        // avatar 저장

        // 이미지 불러옴
        Bitmap bitmap1 = loadImageFromDrawable(getActivity(), R.drawable.avatar1);
        byte[] imageData1 = bitmapToByteArray(bitmap1);
        Bitmap bitmap2 = loadImageFromDrawable(getActivity(), R.drawable.avatar2);
        byte[] imageData2 = bitmapToByteArray(bitmap2);
        Bitmap bitmap3 = loadImageFromDrawable(getActivity(), R.drawable.avatar3);
        byte[] imageData3 = bitmapToByteArray(bitmap3);
        Bitmap bitmap4 = loadImageFromDrawable(getActivity(), R.drawable.avatar4);
        byte[] imageData4 = bitmapToByteArray(bitmap4);
        Bitmap bitmap5 = loadImageFromDrawable(getActivity(), R.drawable.avatar5);
        byte[] imageData5 = bitmapToByteArray(bitmap5);

        List<byte[]> bitmapList = new ArrayList<>(Arrays.asList(imageData1, imageData2, imageData3, imageData4, imageData5));
        // 아바타 생성
        Avatar avatar1 = new Avatar("성민수", bitmapList.get(0), false, "나이: 20대 초반" +
                "가르치는 스타일: 정석적인 접근, 자세 교정에 집중" +
                "성격: 엄격하고 규칙적인" +
                "말투: 말끝마다 \"~회원님\"을 붙이는 자비없는 스타일" +
                "예시:" +
                "\"회원님, 스쿼트 자세가 틀렸습니다. 다시 한번 해보세요, 회원님.\"");
        Avatar avatar2 = new Avatar("이지현", bitmapList.get(1), false, "나이: 20대 초반\n" +
                "가르치는 스타일: 여성스럽고 엄격한 접근, 자세 교정에 집중\n" +
                "성격: 엄격하지만 친근한\n" +
                "말투: 친근하고 상냥한\n" +
                "예시:\n" +
                "\"저희 함께 열심히 노력해봐요! 자세를 조금만 더 바로 세우면 될 거에요.\"");
        Avatar avatar3 = new Avatar("다솜", bitmapList.get(2), false, "나이: 20대 후반\n" +
                "가르치는 스타일: 여성스럽고 격려하는 접근, 동기부여에 집중\n" +
                "성격: 부드럽고 따뜻한\n" +
                "말투: 부드럽고 격려하는\n" +
                "예시:\n" +
                "\"당신은 정말 멋져요! 계속해서 노력하면 반드시 성과가 있을 거에요.\"");
        Avatar avatar4 = new Avatar("김성현", bitmapList.get(3), false, "나이: 30대 중반\n" +
                "가르치는 스타일: 친절하고 격려하는 접근, 동기부여에 집중\n" +
                "성격: 차분하고 친절한\n" +
                "말투: 부드럽고 차분한\n" +
                "예시:\n" +
                "\"좋아요, 조금만 더 힘내세요! 잘하고 있어요, 계속해봅시다.\"");
        Avatar avatar5 = new Avatar("김갑식", bitmapList.get(4), false, "나이: 40대 초반\n" +
                "가르치는 스타일: 강도 높은 훈련, 체력 향상에 집중\n" +
                "성격: 직설적이고 강압적인\n" +
                "말투: 직설적이고 강한\n" +
                "예시:\n" +
                "\"지금 포기할 생각하지 마세요. 더 강하게, 더 빠르게!\"");

        List<Avatar> avatarList = new ArrayList<>(Arrays.asList(avatar1, avatar2, avatar3, avatar4, avatar5));

        // 아바타 저장
        try {
            Boolean isEmpty = new avatarInsertAsyncTask(0, db.avatarDao()).execute(avatarList.get(0)).get();
            if (isEmpty) {

                for (int i = 0; i < 5; i++) {
                    System.out.println("avatarList = " + avatarList.get(i));
                    new avatarInsertAsyncTask(1, db.avatarDao()).execute(avatarList.get(i));
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        // 리사이클러뷰 설정

        // 리사이클러뷰 레이아웃 매니저 설정

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // DB에서 데이터 가져오기
        List<Ranking> userList = null;
        List<byte[]> imageList = new ArrayList<>(); // 유저의 이미지를 저장하는 imageList 선언
        // 유저의 이미지를 저장하는 imageList 선언
        try {
            userList = new rankingAsyncTask(0, db.rankingDao()).execute("").get();
            for (Ranking ranking : userList) {
                if (ranking != null) {
                    String nickName = new userAsyncTask(db.userDao()).execute(ranking.getUserId()).get(); //닉네임 갖고오기
                    ranking.setUserId(nickName);
                    // imageList에 닉네임으로 유저 테이브에서 같은 닉네임을 가진 유저 사진 가져와서 리스트로 만들기
                    // GetProfilePictureAsyncTask 사용하기
                    byte[] profileImage = new GetProfilePictureAsyncTask(db.userDao()).execute(ranking.getUserId()).get();
                    imageList.add(profileImage);
                }
                System.out.println("ranking = " + ranking);

            }



            // 리더보드 어댑터 설정어
            LeaderboardAdapter adapter = new LeaderboardAdapter(userList, imageList); // 여기에 사진 list추가 해야함
            recyclerView.setAdapter(adapter);

            // 상위 3명 사용자 이름 및 점수 설정
            if (userList.size() >= 3) {
                enough.setAlpha(0.0f);
                firstPlace.setText(userList.get(0).getUserId());
                secondPlace.setText(userList.get(1).getUserId());
                thirdPlace.setText(userList.get(2).getUserId());

                firstPlaceScore.setText(String.valueOf(userList.get(0).getScore()));
                secondPlaceScore.setText(String.valueOf(userList.get(1).getScore()));
                thirdPlaceScore.setText(String.valueOf(userList.get(2).getScore()));

                // 백에서 해당하는 닉네임을 갖는 유저의 이미지를 가져오면 된다.
                new SetProfilePictureAsyncTask(db.userDao(), firstImage).execute(userList.get(0).getUserId());
                new SetProfilePictureAsyncTask(db.userDao(), secondImage).execute(userList.get(1).getUserId());
                new SetProfilePictureAsyncTask(db.userDao(), thirdImage).execute(userList.get(2).getUserId());
            }

            // 개인 점수 설정
            Integer myScore = new scoreAsyncTask(db.scoreDao()).execute(currentUser).get();
            if(myScore!=null)   personalScore.setScore(myScore);
            else personalScore.setScore(0);


            // 현재 사용자를 중앙에 위치시키는 메서드 호출
            scrollToCurrentUser(userList);

        } catch (ExecutionException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }


        return view;
    }

    // 현재 사용자를 중앙에 위치시키는 메서드
    private void scrollToCurrentUser(List<Ranking> userList) {
        int position = -1;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUserId().equals(currentUser)) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (layoutManager != null) {
                layoutManager.scrollToPositionWithOffset(position, recyclerView.getHeight() / 2);
            }
        }
    }


    // LeaderboardAdapter 클래스
    static class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

        private List<Ranking> userList;
        private List<byte[]> imageList;

        public LeaderboardAdapter(List<Ranking> userList, List<byte[]> imageList) {
            this.userList = userList;
            this.imageList = imageList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Ranking ranking = userList.get(position);
            holder.rank.setText(String.valueOf(position + 1)); // 랭킹 설정
            holder.username.setText(ranking.getUserId());
            holder.score.setText(String.valueOf(ranking.getScore()));

            byte[] image = imageList.get(position);
            if(image != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                holder.profileImage.setImageBitmap(bitmap);
            }
            if(ranking.getUserId().equals(currentUser)) {
                holder.itemView.setBackgroundResource(R.drawable.leaderboard_square_mine);
            }
            else {
                holder.itemView.setBackgroundResource(R.drawable.leaderboard_square);
            }

        }

        @Override
        public int getItemCount() {
            return userList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView rank, username, score;
            ImageView profileImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                rank = itemView.findViewById(R.id.rank);
                username = itemView.findViewById(R.id.username);
                score = itemView.findViewById(R.id.score);
                profileImage = itemView.findViewById(R.id.leaderboard_image);
            }
        }
    }

    private Bitmap loadImageFromDrawable(Context context, int drawableId) {
        return BitmapFactory.decodeResource(context.getResources(), drawableId);
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private static class GetProfilePictureAsyncTask extends AsyncTask<String, Void, byte[]>{
        private UserDao userDao;

        public GetProfilePictureAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected byte[] doInBackground(String... strings) {
            byte[] picture = userDao.getProfilePictureByNickName(strings[0]);
            return picture;
        }
    }
    private static class SetProfilePictureAsyncTask extends AsyncTask<String, Void, byte[]> {
        private UserDao userDao;
        private ImageView profileImage;

        public SetProfilePictureAsyncTask(UserDao userDao, ImageView profileImage) {
            this.userDao = userDao;
            this.profileImage = profileImage;
        }

        @Override
        protected byte[] doInBackground(String... strings) {
            return userDao.getProfilePictureByNickName(strings[0]);
        }

        @Override
        protected void onPostExecute(byte[] profilePicture) {
            if (profilePicture != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(profilePicture, 0, profilePicture.length);
                profileImage.setImageBitmap(bitmap);
            }
        }
    }


    public static class avatarInsertAsyncTask extends AsyncTask<Avatar, Void, Boolean> {
        private AvatarDao avatarDao;
        private int identifier;

        public avatarInsertAsyncTask(int identifier, AvatarDao avatarDao) {
            this.avatarDao = avatarDao;
            this.identifier = identifier;
        }

        @Override
        protected Boolean doInBackground(Avatar... avatars) {

            try {
                if (identifier == 0) {
                    return avatarDao.isTableEmpty();
                } else if (identifier == 1 && avatars[0] != null) {
                    avatarDao.insert(avatars[0]);
                }
            } catch (Exception e) {
                e.printStackTrace();  // 오류 로그 기록
                return false;
            }
            return false;
        }
    }

    public static class rankingAsyncTask extends AsyncTask<String, Void, List<Ranking>> {

        private RankingDao rankingDao;
        private int identifier;

        public rankingAsyncTask(int identifier, RankingDao rankingDao) {
            this.identifier = identifier;
            this.rankingDao = rankingDao;
        }

        @Override //백그라운드작업(메인스레드 X)
        protected List<Ranking> doInBackground(String... userIds) {

            if (identifier == 0) {
                List<UserTotalScore> topscore=rankingDao.getTop10();
                List<Ranking> top10 = new ArrayList<>();
                for (UserTotalScore score : topscore) {
                    top10.add(new Ranking(null, score.getUserId(), score.getScore()));
                }
                return top10;
            } else {
                int userScore = rankingDao.getUserTotalScore(userIds[0]);

                List<Ranking> rankings = new ArrayList<>();
                rankings.add(new Ranking(null, userIds[0], userScore));
                return rankings;
            }
        }

    }

    public static class userAsyncTask extends AsyncTask<String, Void, String> {

        private UserDao userDao;

        public userAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override //백그라운드작업(메인스레드 X)
        protected String doInBackground(String... userIds) {

            if (userIds[0] != null) return userDao.getNickName(userIds[0]);
            else return null;
        }

    }

    public static class scoreAsyncTask extends AsyncTask<String, Void, Integer> {
        private ScoreDao scoreDao;

        public scoreAsyncTask(ScoreDao scoreDao) {
            this.scoreDao = scoreDao;

        }

        @Override //백그라운드작업(메인스레드 X)
        protected Integer doInBackground(String... userIds) {

            // select
            try {
                if (userIds[0] != null) {

                    return scoreDao.getScore(userIds[0]).getScore();
                }
                else return 0;
            } catch (Exception e) {
                System.out.println("e.getMessage() = " + e.getMessage());
            }
            return null;
        }

    }

    private void seasonUpdate() {
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(SeasonUpdateWorker.class, 14, TimeUnit.DAYS).build();

        WorkManager.getInstance(getActivity()).enqueueUniquePeriodicWork("SeasonUpdateWork", ExistingPeriodicWorkPolicy.KEEP, request);
    }

}