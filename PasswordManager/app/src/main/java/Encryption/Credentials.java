package Encryption;

import java.io.Serializable;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class Credentials implements Serializable{

    String username;
    String hash;
    LinkedList<Credential> credentials = new LinkedList<Credential>();

    public Credentials(String username, String hash){
        this.username = username;
        this.hash= hash;
    }
    public String getHash(){
        return hash;
    }
    public String getUsername(){
        return username;
    }
    public void setHash(String hash){
        this.hash = hash;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public boolean addCredential(String username, String password, String website) throws Exception{
        boolean added = true;

        if(added){
            credentials.add(new Credential(username,Encryptor.encrypt(password, hash),website));
        }
        return added;
    }
    
    public LinkedList<Credential> getCredentials(){
        return credentials;
    }




}
