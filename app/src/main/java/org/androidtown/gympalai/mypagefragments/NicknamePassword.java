    package org.androidtown.gympalai.mypagefragments;

    import android.app.Activity;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.net.Uri;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.provider.MediaStore;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.lifecycle.Observer;

    import org.androidtown.gympalai.R;
    import org.androidtown.gympalai.backmethod.LoginFunction;
    import org.androidtown.gympalai.dao.UserDao;
    import org.androidtown.gympalai.database.GymPalDB;
    import org.androidtown.gympalai.entity.User;

    import java.io.ByteArrayOutputStream;
    import java.io.IOException;
    import java.util.List;
    import java.util.concurrent.ExecutionException;

    import de.hdodenhof.circleimageview.CircleImageView;

    public class NicknamePassword extends Fragment {
        private static final int PICK_IMAGE_REQUEST = 1; //
        CircleImageView user_image;
        TextView old_usr_nickname;

        EditText new_usr_nickname,new_password,password_confirm;
        Button change_nickname_btn, change_password_btn;

        LoginFunction loginFunction;
        GymPalDB db;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.my_page_nickname, container, false);
            //유저의 이미지 뷰 생성
            user_image = rootView.findViewById(R.id.profile_circle_image);
            //유저 텍스트 뷰 생성
            old_usr_nickname = rootView.findViewById(R.id.old_nickname);
            //edittext 생성
            new_usr_nickname = rootView.findViewById(R.id.new_nickname);
            new_password = rootView.findViewById(R.id.new_password);
            password_confirm = rootView.findViewById(R.id.new_password_confirm);
            //버튼들 생성
            change_nickname_btn = rootView.findViewById(R.id.nickname_change_btn);
            change_password_btn = rootView.findViewById(R.id.password_change_btn);

            loginFunction = new LoginFunction();
            db = GymPalDB.getInstance(getActivity());
            db.userDao().getAll().observe(getActivity(), new Observer<List<User>>() {
                @Override
                public void onChanged(List<User> users) {
                }
            });

            String nickName; // 기존 닉네임을 가져온다
            try {
                nickName = String.valueOf(new GetUserNickTask(db.userDao()).execute(loginFunction.getMyId()).get());
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            old_usr_nickname.setText(nickName); //기존 닉네임을 보이게 한다

            // 프로필 사진 불러오기
            new GetProfilePictureAsyncTask(db.userDao(),user_image ).execute(loginFunction.getMyId());


            // 닉네임 변경 버튼 클릭 리스너
            change_nickname_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String oldNickName;
                    try {
                        oldNickName = String.valueOf(new GetUserNickTask(db.userDao()).execute(loginFunction.getMyId()).get());
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    String newNickname = new_usr_nickname.getText().toString();
                    if (newNickname.equals(oldNickName)) { //기존 닉네임과 동일하다면
                        Toast.makeText(getContext(), "기존 닉네임과 동일합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        //닉네임 중복검사
                        Boolean isSameNickName;
                        try { //새로 입력한 닉네임이 중복되는지 확인하기 위한 변수
                            isSameNickName = new CheckNickAsyncTask(db.userDao()).execute(newNickname).get();
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        if (isSameNickName) { //만약 중복된다면
                            Toast.makeText(getContext(), "중복되는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                        } else { //기존 닉네임도 아니고 중복되지 않는다면
                            //닉네임 업데이트
                            new UpdateNickAsyncTask(db.userDao()).execute(loginFunction.getMyId(), newNickname);
                            old_usr_nickname.setText(newNickname);
                            Toast.makeText(getContext(), "Nickname changed to: " + newNickname, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            // 비밀번호 변경 버튼 클릭 리스너
            change_password_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newPassword = new_password.getText().toString();
                    String confirmPassword = password_confirm.getText().toString();
                    String originPassword;
                    try {
                        originPassword = String.valueOf(new GetUserPw(db.userDao()).execute(loginFunction.getMyId()).get());
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    boolean noChangedPw = newPassword.equals(originPassword); //만약 새로 바꾼 비밀번호가 기존과 같다면 true

                    if (newPassword.equals(confirmPassword)) {
                        // 비밀번호가 일치하면 바뀐 비밀번호를 DB에 값을 반영해주시면 됩니다.
                        if (noChangedPw) {
                            Toast.makeText(getContext(), "Same as the existing password.", Toast.LENGTH_SHORT).show();
                        } else {
                            new UpdatePwAsyncTask(db.userDao()).execute(loginFunction.getMyId(), newPassword);
                            Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // 유저 이미지 뷰 클릭 리스너
            user_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openImageChooser();
                }
            });

            return rootView;
        }

        private void openImageChooser() {
            //여기가 이미지 생성 인텐트
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                Uri uri = data.getData(); // 선택한 이미지의 URI를 가져온다.
                try {
                    // 선택한 이미지의 비트맵을 가져온다.
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    // ImageView에 비트맵을 설정한다.
                    user_image.setImageBitmap(bitmap);

                    // 비트맵을 바이트 배열로 변환한다.
                    byte[] byteArray = bitmapToByteArray(bitmap);

                    // 데이터베이스에 저장하는 AsyncTask 실행
                    new UpdateProfilePictureAsyncTask(db.userDao()).execute(loginFunction.getMyId(), byteArray);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private byte[] bitmapToByteArray(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }

        //데이터베이스 관련 처리함수
        private static class CheckNickAsyncTask extends AsyncTask<String, Void, Boolean> { //중복 닉네임이 있는지 확인
            private UserDao userDao;

            public CheckNickAsyncTask(UserDao userDao) {
                this.userDao = userDao;
            }

            @Override
            protected Boolean doInBackground(String... strings) {
                return userDao.getUserNickList().contains(strings[0]);
            }
        }

        private static class GetUserNickTask extends AsyncTask<String, Void, String> { //기존 닉네임을 가져오기
            private UserDao userDao;

            public GetUserNickTask(UserDao userDao) {
                this.userDao = userDao;
            }

            @Override
            protected String doInBackground(String... strings) {
                return userDao.getUserNickById(strings[0]);
            }
        }

        private static class GetUserPw extends AsyncTask<String, Void, String> {
            private UserDao userDao;

            public GetUserPw(UserDao userDao) {
                this.userDao = userDao;
            }

            @Override
            protected String doInBackground(String... strings) {
                return userDao.getUserPwById(strings[0]);
            }
        }

        private static class UpdateNickAsyncTask extends AsyncTask<String, Void, Void> { // 닉네임 변경 메서드
            private UserDao userDao;

            public UpdateNickAsyncTask(UserDao userDao) {
                this.userDao = userDao;
            }

            @Override
            protected Void doInBackground(String... strings) {
                String userId = strings[0];
                String newNickName = strings[1];
                userDao.updateNickName(userId, newNickName);
                return null;
            }
        }

        private static class UpdatePwAsyncTask extends AsyncTask<String, Void, Void> {
            private UserDao userDao;

            public UpdatePwAsyncTask(UserDao userDao) {
                this.userDao = userDao;
            }

            @Override
            protected Void doInBackground(String... strings) {
                String userId = strings[0];
                String newPw = strings[1];
                userDao.updatePw(userId, newPw);
                return null;
            }
        }

        private static class UpdateProfilePictureAsyncTask extends AsyncTask<Object, Void, Void> {
            private UserDao userDao;

            public UpdateProfilePictureAsyncTask(UserDao userDao) {
                this.userDao = userDao;
            }

            @Override
            protected Void doInBackground(Object... objects) {
                String userId = (String) objects[0];
                byte[] profilePicture = (byte[]) objects[1];
                userDao.updateProfilePicture(userId, profilePicture);
                return null;
            }
        }

        private static class GetProfilePictureAsyncTask extends AsyncTask<String, Void, byte[]> {
            private UserDao userDao;
            private CircleImageView profileImage;

            public GetProfilePictureAsyncTask(UserDao userDao, CircleImageView profileImage) {
                this.userDao = userDao;
                this.profileImage = profileImage;
            }

            @Override
            protected byte[] doInBackground(String... strings) {
                return userDao.getProfilePictureById(strings[0]);
            }

            @Override
            protected void onPostExecute(byte[] profilePicture) {
                if (profilePicture != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(profilePicture, 0, profilePicture.length);
                    profileImage.setImageBitmap(bitmap);
                }
            }
        }



    }
