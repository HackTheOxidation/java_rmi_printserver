package printserver.server;

import printserver.common.Authenticator;
import printserver.common.PrintServer;

import java.rmi.RemoteException;

import java.rmi.server.UnicastRemoteObject;
import java.security.*;

import javax.crypto.BadPaddingException;

import java.util.*;

public class EncryptedAuthenticator extends UnicastRemoteObject implements Authenticator {
        private final Database db;
        public EncryptedAuthenticator() throws RemoteException {
                this.db = new Database();
        }

        protected void addUser(String username, String password) throws NoSuchAlgorithmException, BadPaddingException {
                String salt = this.getSalt();
                String passwordWithSalt = password + salt;
                Login login = new Login(username, this.getPasswordHash(passwordWithSalt), salt);
                try {
                        this.db.createUser(login);
                } catch (Exception e) {
                        System.out.println("DEBUG - addUser:" + e.getMessage());
                }
        }

        public PrintServer authenticate(String username, String password) throws RemoteException {
                try {
                        Login login = this.authenticateLogin(username, password);
                        if(login.isAuthenticated()) return new Proxy(db.getPriviligesForUser(username));
                } catch (Exception e) {
                        System.out.println("DEBUG - authenticate: " + e.getMessage());
                }
                System.out.println("Failed authentication attempt - username: " + username);
                return null;
        }

        private Login authenticateLogin(String username, String password) throws NoSuchAlgorithmException, ClassNotFoundException {
                Login login = this.db.getUser(new Login(username));

                if(login.getPasswordHash() != null){
                        String decryptedPasswordWithSalt = password + login.getSalt();
                        byte[] inputPasswordHashWithSalt = this.getPasswordHash(decryptedPasswordWithSalt);
                        if (Arrays.equals(inputPasswordHashWithSalt, login.getPasswordHash())) {
                                return new Login(login.getUsername(), login.getPasswordHash(), login.getSalt(), true);
                        }
                }
                return login;
        }

        private byte[] getPasswordHash(String password) throws NoSuchAlgorithmException {
                MessageDigest md = MessageDigest.getInstance("SHA3-256");
                md.update(password.getBytes());
                return md.digest();
        }

        private String getSalt() {
                Random random = new SecureRandom();
                byte[] salt = new byte[16];
                random.nextBytes(salt);
                return Base64.getEncoder().encodeToString(salt);
        }
}
