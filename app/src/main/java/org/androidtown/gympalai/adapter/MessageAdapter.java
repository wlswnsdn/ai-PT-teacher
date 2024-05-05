package org.androidtown.gympalai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    //ViewHolder 생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //view 연결
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if(message.getSentBy().equals(Message.SENT_BY_ME)){
            holder.left_chat_view.setVisibility(View.GONE);
            holder.right_chat_view.setVisibility(View.VISIBLE);
            holder.right_chat_tv.setText(message.getMessage());
        } else {
            holder.right_chat_view.setVisibility(View.GONE);
            holder.left_chat_view.setVisibility(View.VISIBLE);
            holder.left_chat_tv.setText(message.getMessage());
        }
    }
    //user 입력시 left_view 제거, right_view 텍스트 노출

    // system 입력시 right_view 제거, left_view 텍스트 노출
    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout left_chat_view, right_chat_view;
        TextView left_chat_tv, right_chat_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            left_chat_view = itemView.findViewById(R.id.left_chat_view);
            right_chat_view = itemView.findViewById(R.id.right_chat_view);
            left_chat_tv = itemView.findViewById(R.id.left_chat_tv);
            right_chat_tv = itemView.findViewById(R.id.right_chat_tv);
        }
    }
}