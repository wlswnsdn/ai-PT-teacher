package org.androidtown.gympalai.layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.net.MediaType;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.adapter.MessageAdapter;
import org.androidtown.gympalai.model.Message;

import java.util.ArrayList;
import java.util.List;

public class chat extends Fragment {


    RecyclerView recyclerView;
    EditText etMsg;
    TextView tvWelcome;
    ImageButton btnSend;

    List<Message> messageList;
    MessageAdapter messageAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        tvWelcome = view.findViewById(R.id.tv_welcome);
        etMsg = view.findViewById(R.id.et_msg);
        btnSend = view.findViewById(R.id.btn_send);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);

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

    //채팅 메세지 추가 메서드
    void addToChat(String message, String sentBy) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
            }
        });
    }

    //API 응답 추가 메서드
    void addResponse(String response) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.remove(messageList.size() - 1);
                addToChat(response, Message.SENT_BY_BOT);
            }
        });
    }

    // API 호출 메서드
    void callAPI(String question) {
        // 네트워크 통신 로직 구현

    }
}
