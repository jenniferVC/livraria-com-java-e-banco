package Models;

public class Account {

    private String emailAddress;
    private long ID;
    private String password;

    //==== MÉTODOS ACESSORES E MODIFICADORES             
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //==== Métodos de operação
    public boolean verifyPassword(String psw){
        return password.equals(psw);
    }
    
    public boolean validateLogin(long ID, String psw) {
        if (verifyPassword(psw) && this.ID == ID) {
            return true;
        }
        return false;
    }

}
