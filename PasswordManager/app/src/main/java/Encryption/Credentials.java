package Encryption;

import java.io.Serializable;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class Credentials implements Serializable{


    String hash;
    String authType;
    LinkedList<Credential> credentials = new LinkedList<Credential>();

    public Credentials(String authType){
        this.authType = authType;
    }

    public Credentials(String authType, String hash){
        this.authType = authType;
        this.hash= hash;
    }
    public String getHash(){
        return hash;
    }
    public String getAuthType(){
        return authType;
    }
    public void setHash(String hash){
        this.hash = hash;
    }
    public void setAuthType(String authType){
        this.authType = authType;
    }
    public void setCredentials(LinkedList<Credential> credentials){
        this.credentials = credentials;
    }

    public String addCredential(String username, String password, String website, String unhashedPW) throws Exception{
        String returnVal ="";
        for(Credential c: credentials){
            if(Encryptor.decrypt(c.getPassword(),unhashedPW).equals(password)){
                returnVal = website;
            }
        }

            credentials.add(new Credential(username,Encryptor.encrypt(password, unhashedPW),website));

        return returnVal;
    }

    public LinkedList<Credential> getCredentials(){
        return credentials;
    }




}
