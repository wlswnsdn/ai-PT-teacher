package org.androidtown.gympalai.mypagefragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.backmethod.LoginFunction;
import org.androidtown.gympalai.dao.AvatarDao;
import org.androidtown.gympalai.dao.UserDao;
import org.androidtown.gympalai.database.GymPalDB;
import org.androidtown.gympalai.entity.Avatar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AvatarSetting extends Fragment {
    CircleImageView current_trainner;
    TextView current_trainner_name;
    GymPalDB db;

    LoginFunction loginFunction = new LoginFunction();
    ImageButton blocked_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.my_page_avatar, container, false);
        boolean paid_status = false; // 유료가입이 되었는지 안되었는지 boolean으로 체크
        // DB 생성
        db = GymPalDB.getInstance(getActivity());
        // ImageButton 생성
        blocked_btn = rootView.findViewById(R.id.block_button);

        // 유료 상태에 따라 blocked_btn 가시성 설정
        if (paid_status) {
            blocked_btn.setVisibility(View.GONE); // 유료 가입 상태이면 버튼을 숨김
        } else {
            blocked_btn.setVisibility(View.VISIBLE); // 유료 가입 상태가 아니면 버튼을 보임
        }

        // ImageView들 선언
        current_trainner = rootView.findViewById(R.id.trainner_circle_1);

        ImageView[] trainnerOptions = new ImageView[4];

        trainnerOptions[0] = rootView.findViewById(R.id.trainner_option_circle_1);
        trainnerOptions[1] = rootView.findViewById(R.id.trainner_option_circle_2);
        trainnerOptions[2] = rootView.findViewById(R.id.trainner_option_circle_3);
        trainnerOptions[3] = rootView.findViewById(R.id.trainner_option_circle_4);

        // TextView 선언
        current_trainner_name = rootView.findViewById(R.id.trainer_name_textview);

        TextView[] trainnerDescriptions = new TextView[4];

        trainnerDescriptions[0] = rootView.findViewById(R.id.trainner_description_1);
        trainnerDescriptions[1] = rootView.findViewById(R.id.trainner_description_2);
        trainnerDescriptions[2] = rootView.findViewById(R.id.trainner_description_3);
        trainnerDescriptions[3] = rootView.findViewById(R.id.trainner_description_4);

        // Button들 선언
        Button[] trainners = new Button[4];

        trainners[0] = rootView.findViewById(R.id.trainner_btn_1);
        trainners[1] = rootView.findViewById(R.id.trainner_btn_2);
        trainners[2] = rootView.findViewById(R.id.trainner_btn_3);
        trainners[3] = rootView.findViewById(R.id.trainner_btn_4);

        try {
            // 내 트레이너 설정
            String avatarName = new userAsyncTask(0, "", db.userDao()).execute(loginFunction.getMyId()).get();
            List<Avatar> avatar = new avatarAsyncTask(0, db.avatarDao()).execute(avatarName).get();
            byte[] current_image = avatar.get(0).getImage();
            Bitmap current_bitmap = BitmapFactory.decodeByteArray(current_image, 0, current_image.length);
            current_trainner.setImageBitmap(current_bitmap);
            current_trainner_name.setText(avatar.get(0).getAvatarName());

            // 다른 트레이너 설정
            List<Avatar> avatars = new avatarAsyncTask(1, db.avatarDao()).execute("").get();
            for (int i = 0; i < 4; i++) {
                byte[] image = avatars.get(i).getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                trainnerOptions[i].setImageBitmap(bitmap);
                trainners[i].setText(avatars.get(i).getAvatarName());
                trainnerDescriptions[i].setText(avatars.get(i).getDescription());
            }

        } catch (ExecutionException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }

        // blocked 버튼 클릭 시 toast 메시지 생성
        blocked_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paid_status) {

                } else {
                    Toast.makeText(getContext(), "유료 결제상품입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 버튼 클릭 시 current_trainner의 src, current_trainner_name의 텍스트를 해당 아바타의 값들로 바꿉니다.
        trainners[0].setOnClickListener(new View.OnClickListener() { // trainer1 버튼을 누르면 avatar가 바뀐다.
            @Override
            public void onClick(View v) { // 여기에 avatar1 구현해주시면 됩니다.
                try {
                    List<Avatar> avatars = new avatarAsyncTask(1, db.avatarDao()).execute("").get();
                    new userAsyncTask(1, avatars.get(0).getAvatarName(), db.userDao()).execute(loginFunction.getMyId());
                    String sourceText = trainners[0].getText().toString();
                    current_trainner_name.setText(sourceText);
                    current_trainner.setImageDrawable(trainnerOptions[0].getDrawable());
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        trainners[1].setOnClickListener(new View.OnClickListener() { // trainer2 버튼을 누르면 avatar가 바뀐다.
            @Override
            public void onClick(View v) {
                try {
                    List<Avatar> avatars = new avatarAsyncTask(1, db.avatarDao()).execute("").get();
                    new userAsyncTask(1, avatars.get(1).getAvatarName(), db.userDao()).execute(loginFunction.getMyId());
                    String sourceText = trainners[1].getText().toString();
                    current_trainner_name.setText(sourceText);
                    current_trainner.setImageDrawable(trainnerOptions[1].getDrawable());
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        trainners[2].setOnClickListener(new View.OnClickListener() { // trainer3 버튼을 누르면 avatar가 바뀐다.
            @Override
            public void onClick(View v) {
                try {
                    List<Avatar> avatars = new avatarAsyncTask(1, db.avatarDao()).execute("").get();
                    new userAsyncTask(1, avatars.get(2).getAvatarName(), db.userDao()).execute(loginFunction.getMyId());
                    String sourceText = trainners[2].getText().toString();
                    current_trainner_name.setText(sourceText);
                    current_trainner.setImageDrawable(trainnerOptions[2].getDrawable());
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        trainners[3].setOnClickListener(new View.OnClickListener() { // trainer4 버튼을 누르면 avatar가 바뀐다.
            @Override
            public void onClick(View v) { // 여기에 avatar1 구현해주시면 됩니다.
                try {
                    List<Avatar> avatars = new avatarAsyncTask(1, db.avatarDao()).execute("").get();
                    new userAsyncTask(1, avatars.get(3).getAvatarName(), db.userDao()).execute(loginFunction.getMyId());
                    String sourceText = trainners[3].getText().toString();
                    current_trainner_name.setText(sourceText);
                    current_trainner.setImageDrawable(trainnerOptions[3].getDrawable());
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        return rootView;
    }

    public static class userAsyncTask extends AsyncTask<String, Void, String> {

        private UserDao userDao;
        private int identifier;
        private String avatarName;

        public userAsyncTask(int identifier, String avatarName, UserDao userDao) {
            this.identifier = identifier;
            this.avatarName = avatarName;
            this.userDao = userDao;
        }

        @Override // 백그라운드 작업(메인스레드 X)
        protected String doInBackground(String... userIds) {
            if (identifier == 0 && userIds[0] != null) return userDao.getAvatarName(userIds[0]);
            else if (identifier == 1 && userIds[0] != null) {
                userDao.updateAvatar(userIds[0], avatarName);
                return null;
            } else return null;
        }
    }

    public static class avatarAsyncTask extends AsyncTask<String, Void, List<Avatar>> {
        private AvatarDao avatarDao;
        private int identifier;

        public avatarAsyncTask(int identifier, AvatarDao avatarDao) {
            this.avatarDao = avatarDao;
            this.identifier = identifier;
        }

        @Override
        protected List<Avatar> doInBackground(String... avatarNames) {
            if (identifier == 0 && avatarNames[0] != null) {
                List<Avatar> avatars = new ArrayList<>();
                avatars.add(avatarDao.getAvatar(avatarNames[0]));
                return avatars;
            } else if (identifier == 1 && avatarNames[0] != null) {
                return avatarDao.getAvatarList();
            } else return null;
        }
    }
}
