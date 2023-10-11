package printserver.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    static final String URL = "postgres://efiespng:xPT519NpOeC6Y14XZp6vYPoO_IeusQVR@ella.db.elephantsql.com/efiespng";
    static final String USER = "efiespng";
    static final String PASSWORD = "xPT519NpOeC6Y14XZp6vYPoO_IeusQVR";

    public void createUser(Login login){
        return;
    }

    public void changePassword(Login login){
        return;
    }

    public Login getUser(String username){
        return new Login();
    }
}
