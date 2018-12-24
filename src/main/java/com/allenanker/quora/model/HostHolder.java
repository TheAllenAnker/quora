package com.allenanker.quora.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public static User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
