package org.androidtown.gympalai.backmethod;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.dao.AvatarDao;
import org.androidtown.gympalai.database.GymPalDB;
import org.androidtown.gympalai.entity.Avatar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AvatarSettings extends AppCompatActivity {


    //DB 생성
    GymPalDB db = GymPalDB.getInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 이미지 불러옴
        Bitmap bitmap1 = loadImageFromDrawable(this, R.id.analysis);
        byte[] imageData1 = bitmapToByteArray(bitmap1);
        Bitmap bitmap2 = loadImageFromDrawable(this, R.id.analysis);
        byte[] imageData2 = bitmapToByteArray(bitmap1);
        Bitmap bitmap3 = loadImageFromDrawable(this, R.id.analysis);
        byte[] imageData3 = bitmapToByteArray(bitmap1);
        Bitmap bitmap4 = loadImageFromDrawable(this, R.id.analysis);
        byte[] imageData4 = bitmapToByteArray(bitmap1);
        Bitmap bitmap5 = loadImageFromDrawable(this, R.id.analysis);
        byte[] imageData5 = bitmapToByteArray(bitmap1);

        List<byte[]> bitmapList = new ArrayList<>(Arrays.asList(imageData1,imageData2,imageData3,imageData4,imageData5));

        // 아바타 생성
        Avatar avatar1 = new Avatar("성민수", bitmapList.get(0), false, "나이: 20대 초반\n" +
                "가르치는 스타일: 정석적인 접근, 자세 교정에 집중\n" +
                "성격: 엄격하고 규칙적인\n" +
                "말투: 말끝마다 \"~회원님\"을 붙이는 자비없는 스타일\n" +
                "예시:\n" +
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
            if(!isEmpty)
                for (int i = 1; i <= 5; i++) {
                    new avatarInsertAsyncTask(1,db.avatarDao()).execute(avatarList.get(i));
                }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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

    private static class avatarInsertAsyncTask extends AsyncTask<Avatar, Void, Boolean> {
        private AvatarDao avatarDao;
        private int identifier;
        public avatarInsertAsyncTask(int identifier, AvatarDao avatarDao){
            this.avatarDao = avatarDao;
            this.identifier = identifier;
        }

        @Override
        protected Boolean doInBackground(Avatar ... avatars) {

            if(identifier==0) return avatarDao.isTableEmpty();
            else if(identifier==1 && avatars[0]!=null)  avatarDao.insert(avatars[0]);
            return false;
        }
    }

}
