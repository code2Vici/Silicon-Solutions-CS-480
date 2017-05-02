package Encryption;

import java.io.Serializable;

public class Credential implements Serializable {
    private String username;
    private String password;
    private String website;

    public Credential(String username, String password, String website){
        this.username = username;
        this.password = password;
        this.website = website;
    }

    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getWebsite(){
        return website;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setWebsite(String website){
        this.website = website;
    }



}

