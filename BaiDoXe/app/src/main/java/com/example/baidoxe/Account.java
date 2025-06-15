package com.example.baidoxe;

public class Account {
    private String email;
    private boolean locked;

    public Account(String email, boolean locked) {
        this.email = email;
        this.locked = locked;
    }

    public String getEmail() {
        return email;
    }

    public boolean isLocked() {
        return locked;
    }
}