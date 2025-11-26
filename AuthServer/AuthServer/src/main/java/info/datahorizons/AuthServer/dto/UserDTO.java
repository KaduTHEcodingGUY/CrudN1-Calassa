package info.datahorizons.AuthServer.dto;

import java.util.List;
import java.util.Map;

public class UserDTO {
    private String login;
    private String password;
    private List<String> roles;
    private Map<String, Object> extra;

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
    public Map<String, Object> getExtra() { return extra; }
    public void setExtra(Map<String, Object> extra) { this.extra = extra; }
}