package printserver.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class App {
        public static void main(String[] args) throws RemoteException {
                Registry registry = LocateRegistry.createRegistry(5099);
                try {
                        EncryptedAuthenticator auth = new EncryptedAuthenticator();
                        Database db = new Database();

                        //Add the roles
                        db.createRole("admin");
                        db.createRole("service");
                        db.createRole("power");
                        db.createRole("user");

                        //create admin user and assign role
                        auth.addUser("Alice", "Alice123");
                        db.assignRoleToUse("Alice","admin");

                        //create service user and assign role
                        auth.addUser("Bob", "Bob123");
                        db.assignRoleToUse("Bob","service");

                        //create power user and assign role
                        auth.addUser("Cecilia", "Cecilia123");
                        db.assignRoleToUse("Cecilia", "power");

                        //create user users and assign role
                        auth.addUser("David", "David123");
                        db.assignRoleToUse("David", "user");

                        auth.addUser("Erica", "Erica123");
                        db.assignRoleToUse("Erica", "user");

                        auth.addUser("Fred", "Fred123");
                        db.assignRoleToUse("Fred", "user");

                        auth.addUser("George", "George123");
                        db.assignRoleToUse("George", "user");

                        registry.rebind("login", auth);
                } catch (Exception e) {
                        System.out.println("Failed to create authenticator: " + e.getMessage());
                }
        }
}
