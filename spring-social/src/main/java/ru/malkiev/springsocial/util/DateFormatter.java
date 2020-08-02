package ru.malkiev.springsocial.util;

import lombok.AllArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;

@AllArgsConstructor
public class DateFormatter implements Supplier<String> {

    private final Date date;

    @Override
    public String get() {
        return date != null ? new SimpleDateFormat("dd-MM-yyyy HH:mm").format(date) : "";
    }
}
