package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.User;

public class UserModelHelper implements ModelHelper<User> {

    private final Long userId1 = 1L;
    private final String username1 = "JohnDoe";
    private final String useremail1 = "johndoe@mymail.com";
    private final String usernumber1 = "0888888888888";

    private final Long userId2 = 2L;
    private final String username2 = "Jonathan";
    private final String useremail2 = "jonathan@mymail.com";
    private final String usernumber2 = "0888888";
    
    public UserModelHelper() {
    }
    

    @Override
    public User createModel(Integer num){
        if (num == 1) {
            User user1 = new User();
            user1.setUserId(userId1);
            user1.setUsername(username1);
            user1.setEmail(useremail1);
            user1.setPhoneNumber(usernumber1);
            return user1;
        } else {
            User user2 = new User();
            user2.setUserId(userId2);
            user2.setUsername(username2);
            user2.setEmail(useremail2);
            user2.setPhoneNumber(usernumber2);
            return user2;
        }
    }
}

