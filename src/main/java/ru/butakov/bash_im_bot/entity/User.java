package ru.butakov.bash_im_bot.entity;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    long chatId;
}
