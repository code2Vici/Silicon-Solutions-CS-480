package Encryption;

import android.support.annotation.NonNull;

import java.io.ObjectInput;
import java.io.Serializable;

        import java.io.Serializable;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.c;

public class Credential<T> implements Serializable,Comparable {
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


    @Override
    public int compareTo(Object o) {
        Credential c = (Credential) o;
        String website = c.getWebsite().toLowerCase();
        String username =c.getUsername().toLowerCase();
        String website1 = this.website.toLowerCase();
        String username1 = this.username.toLowerCase();

        if(website1.compareTo(c.website)==0){
            return username1.compareTo(username);
        }


        return website1.compareTo(website);
    }
}

