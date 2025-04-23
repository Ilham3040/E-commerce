package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.User;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserModelHelper implements ModelHelper<User> {

    @Override
    public User createModel(Integer num){
        if (num == 1) {
            return User.builder()
                .userId(1L)
                .username("JohnDoe")
                .email("johndoe@mymail.com")
                .phoneNumber("0888888888888")
                .build();
        } else {
            return User.builder()
                .userId(2L)
                .username("Jonathan")
                .email("jonathan@mymail.com")
                .phoneNumber("0888888")
                .build();
        }
    }
}
