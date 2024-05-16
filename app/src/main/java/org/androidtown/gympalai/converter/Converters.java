package org.androidtown.gympalai.converter;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Converters {

//    @TypeConverter
//    public static LocalDateTime fromString(String value) {
//        return value == null ? null : LocalDateTime.parse(value);
//    }

    private static final DateTimeFormatter formatterWithMillis = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final DateTimeFormatter formatterWithoutMillis = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static LocalDateTime fromString(String value) {
        if (value == null) {
            return null;
        }
        try {
            // 첫 번째 형식으로 파싱 시도
            return LocalDateTime.parse(value, formatterWithMillis);
        } catch (DateTimeParseException e) {
            try {
                // 두 번째 형식으로 파싱 시도
                return LocalDate.parse(value, formatterWithoutMillis).atStartOfDay();
            } catch (DateTimeParseException ex) {
                // 어떤 형식에도 맞지 않을 경우 null 반환
                return null;
            }
        }
    }

    @TypeConverter
    public static String dateToString(LocalDateTime date) {
        // 원하는 형식의 문자열로 변환
        return date == null ? null : date.format(formatterWithMillis);
    }
}
