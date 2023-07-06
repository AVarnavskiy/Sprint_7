package models.requests;

public class LoginCourierRequest {

    private String login;
    private String password;

    public LoginCourierRequest() {
    }

    public LoginCourierRequest(String login) {
        this.login = login;
    }

    public LoginCourierRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
