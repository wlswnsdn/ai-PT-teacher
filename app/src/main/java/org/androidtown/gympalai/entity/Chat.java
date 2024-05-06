package org.androidtown.gympalai.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "chat", foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "userId", childColumns = "userId", onDelete = ForeignKey.CASCADE))
public class Chat {

    public Chat(@NonNull String userId, LocalDateTime dateTime, Boolean isQuestion, String message) {
        this.userId = userId;
        this.dateTime = dateTime;
        this.isQuestion = isQuestion;
        this.message = message;
    }

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Long chatId;

    @NonNull
    private String userId;

    @ColumnInfo(name = "date")
    private LocalDateTime dateTime;

    @ColumnInfo(name = "isQuestion")    // true-> 질문 false->답변
    private Boolean isQuestion;

    @ColumnInfo(name = "message")
    private String message;

    @NonNull
    public Long getChatId() {
        return chatId;
    }

    public void setChatId(@NonNull Long chatId) {
        this.chatId = chatId;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Boolean getQuestion() {
        return isQuestion;
    }

    public void setQuestion(Boolean question) {
        isQuestion = question;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "chatId=" + chatId +
                ", userId='" + userId + '\'' +
                ", dateTime=" + dateTime +
                ", isQuestion=" + isQuestion +
                ", message='" + message + '\'' +
                '}';
    }
}
