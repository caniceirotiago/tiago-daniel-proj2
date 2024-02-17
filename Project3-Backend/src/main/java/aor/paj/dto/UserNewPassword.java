package aor.paj.dto;

public class UserNewPassword {
    private String password;
    private String newPassword;

    public UserNewPassword() {
    }

    public UserNewPassword(String password, String newPassword) {
        this.password = password;
        this.newPassword = newPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPassword() {
        return password;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
