//package org.androidtown.gympalai.backmethod;
//
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.Observer;
//
//import org.androidtown.gympalai.R;
//import org.androidtown.gympalai.database.GymPalDB;
//import org.androidtown.gympalai.entity.HealthInfo;
//import org.androidtown.gympalai.entity.User;
//import org.androidtown.gympalai.model.LoginId;
//
//import java.util.List;
//
//public class DietCalculate extends AppCompatActivity {
//
//    GymPalDB db;
//
//    LoginFunction loginFunction = new LoginFunction();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.for_database_test);
//
//        //DB 생성
//        db = GymPalDB.getInstance(this);
//
//        db.healthInfoDao().getUserInfo(loginId.getLoginId()).observe(this, new Observer<HealthInfo>() {
//            @Override
//            public void onChanged(HealthInfo userInfo) {
//                System.out.println("userInfo.toString() = " + userInfo.toString());
//                double TDEE = getTDEE(userInfo);
//                // tdee             5: 2: 3
//                    // 1g 당 칼로리 탄 4 단 4 지 9
//
//
//
//            }
//
//            private double getTDEE(HealthInfo userInfo) {
//                double TDEE;
//                if (userInfo.isGender() == true) TDEE = 88.362 + (13.397 * userInfo.getWeight()) + (4.799 * userInfo.getHeight()) - (5.677 * userInfo.getAge());
//                else {
//                    TDEE = 447.593 + (9.247 * userInfo.getWeight()) + (3.098 * userInfo.getHeight()) - (4.330 * userInfo.getAge());
//                }
//                switch (userInfo.getActivity()) {
//                    case 0:
//                        TDEE *= 1.2;
//                        break;
//                    case 1:
//                        TDEE *= 1.375;
//                        break;
//                    case 2:
//                        TDEE *= 1.55;
//                        break;
//                    case 3:
//                        TDEE *= 1.725;
//                        break;
//                    default:
//                        TDEE *= 1.9;
//                        break;
//                }
//
//                if (userInfo.getPurpose() == 0) TDEE -= 500;
//                else if(userInfo.getPurpose()==2) TDEE += 500;
//
//                return TDEE;
//            }
//
//            private int getDietScore(double tdee, double ratio, double actualIntake){
//                int gperKcal = (ratio == 0.5 || ratio == 0.2) ? 4 : 9;
//                double recommendedIntake = (tdee / ratio) / gperKcal;
//                int score = (int) (-(700 * ratio) / Math.pow(recommendedIntake, 2) * Math.pow(actualIntake - recommendedIntake, 2) + (700 * ratio));
//
//                return score;
//
//            }
//        });
//
//
//
//        // tdee 계산
////        HealthInfo userInfo = db.healthInfoDao().getHealthInfo(loginId.getLoginId());
//  //      System.out.println("userInfo = " + userInfo.toString());
//
//        // 남자
//
//        // 여자
//
//    }
//
//
//}
