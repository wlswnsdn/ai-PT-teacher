package org.androidtown.gympalai.mypagefragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.backmethod.LoginFunction;
import org.androidtown.gympalai.dao.HealthInfoCloneDao;
import org.androidtown.gympalai.dao.HealthInfoDao;
import org.androidtown.gympalai.database.GymPalDB;
import org.androidtown.gympalai.entity.HealthInfo;
import org.androidtown.gympalai.entity.HealthInfoClone;
import org.androidtown.gympalai.entity.User;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyInformationChangeFragment extends Fragment {
    //weight, height를 위한 numberpicker정의
    private NumberPicker weightPicker;
    private NumberPicker heightPicker;
    String[] purposes={"다이어트","벌크업","유지어트"};
    Spinner spinner_purpose;

    public Button information_submit_btn;
    LoginFunction loginFunction;
    GymPalDB db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.my_page_information, container, false);
        //picker들 선언하고 minimum, maximum value들을 설정합니다.
        weightPicker=rootView.findViewById(R.id.weight_picker);
        heightPicker=rootView.findViewById(R.id.height_picker);
        information_submit_btn=rootView.findViewById(R.id.information_submit_btn);

        // 로그인 및 Db
        loginFunction = new LoginFunction();
        db = GymPalDB.getInstance(getActivity());
        db.userDao().getAll().observe(getActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
            }
        });


        setupNumberPicker(weightPicker, 30.0f, 200.0f, 0.1f);
        setupNumberPicker(heightPicker, 100.0f, 200.0f, 0.1f);
        //number picker들로 값을 설정하면 ChangeUserHealthInfo로 몸무게랑 키값 바꿔주시면 됩니다.

        //spinner 생성
        spinner_purpose = rootView.findViewById(R.id.purpose_spinner_change);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,purposes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_purpose.setAdapter(adapter);

        //바뀐 정보들을 저장하고 마지막으로 submit버튼을 누르면 유저의 건강정보가 갱신된다.
        information_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float selectedWeight = Float.parseFloat(weightPicker.getDisplayedValues()[weightPicker.getValue()]);
                float selectedHeight = Float.parseFloat(heightPicker.getDisplayedValues()[heightPicker.getValue()]);
                int selectedPurpose = spinner_purpose.getSelectedItemPosition();
                boolean isUpdate;
                boolean isInsert;
                //HealthInfo 값 Update
                try {
                    isUpdate = new UpdateHealthInfoAsyncTask(db.healthInfoDao()).execute(loginFunction.getMyId(),
                            String.valueOf(selectedHeight), String.valueOf(selectedWeight), String.valueOf(selectedPurpose)).get();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //HealthInfoClone에 값 Insert
                HealthInfoClone healthInfoClone;
                try {
                    HealthInfo healthInfo = new GetHealthInfoByUserIdAsynctask(db.healthInfoDao()).execute(loginFunction.getMyId()).get();
                    healthInfoClone = new HealthInfoClone(
                            loginFunction.getMyId(),
                            selectedHeight,
                            selectedWeight,
                            healthInfo.getAge(),
                            healthInfo.isGender(),
                            healthInfo.getActivity(),
                            selectedPurpose,
                            new Date()
                    );

                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                     isInsert = new InsertHealthInfoClone(db.healthInfoCloneDao()).execute(healthInfoClone).get();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if(isUpdate && isInsert){
                    Toast.makeText(getContext(), "HealthInfo Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void setupNumberPicker(NumberPicker numberPicker, float minValue, float maxValue, float step) {
        //numberpicker에 들어있는 30부터 200까지 0.1 step으로 움직였을 때 들어있는 값의 갯수를 정의합니다.
        int numValues = (int)((maxValue - minValue) / step) + 1;
        //값의 갯수 크기의 String array를 정의합니다.
        String[] values = new String[numValues];
        //for 반복문으로 String array에 모든 값을 넣어줍니다.
        for (int i = 0; i < numValues; i++) {
            values[i] = String.format("%.1f", minValue + (i * step));
        }
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(numValues - 1);
        numberPicker.setDisplayedValues(values);
        numberPicker.setWrapSelectorWheel(false);

        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            float selectedValue = minValue + (newVal * step);
            if (picker.getId() == R.id.weight_picker) {
                // weight_picker에서 값이 변경되었을때 selectedValue를 weight테이블에,
                Toast.makeText(getContext(), "Selected weight: "+selectedValue , Toast.LENGTH_SHORT).show();
            } else if (picker.getId() == R.id.height_picker) {
                // weight_picker에서 값이 변경되었을때 selectedValue를 height에 반영해주시면 됩니다..
                Toast.makeText(getContext(), "Selected height: "+selectedValue , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static class UpdateHealthInfoAsyncTask extends AsyncTask<String, Void, Boolean>{
        private HealthInfoDao healthInfoDao;
        private HealthInfoCloneDao healthInfoCloneDao;
        public UpdateHealthInfoAsyncTask(HealthInfoDao healthInfoDao){
            this.healthInfoDao = healthInfoDao;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String userId = strings[0];
            float height = Float.parseFloat(strings[1]);
            float weight = Float.parseFloat(strings[2]);
            int purpose = Integer.parseInt(strings[3]);

            healthInfoDao.updateHealthInfo(userId, height, weight, purpose);
            return true;
        }
    }

    private static class GetHealthInfoByUserIdAsynctask extends AsyncTask<String, Void, HealthInfo>{
        private HealthInfoDao healthInfoDao;

        public GetHealthInfoByUserIdAsynctask(HealthInfoDao healthInfoDao) {
            this.healthInfoDao = healthInfoDao;
        }
        @Override
        protected HealthInfo doInBackground(String... strings) {
            return healthInfoDao.getHealthInfoByUserId(strings[0]);
        }
    }

    private static class InsertHealthInfoClone extends AsyncTask<HealthInfoClone, Void, Boolean>{
        private HealthInfoCloneDao healthInfoCloneDao;
        public InsertHealthInfoClone(HealthInfoCloneDao healthInfoCloneDao){this.healthInfoCloneDao = healthInfoCloneDao;}

        @Override
        protected Boolean doInBackground(HealthInfoClone... healthInfoClones) {
            healthInfoCloneDao.insert(healthInfoClones[0]);
            return true;
        }
    }
}
