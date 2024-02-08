package aor.paj.service;

import aor.paj.dto.User;

public class UserSession {

    String currentUser = null;
    public UserSession() {
    }
    public String getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
    public void logout() {
        currentUser = null;
    }
}
