package printserver.server;

import printserver.common.Authenticator;
import printserver.common.PrintServer;

import java.rmi.RemoteException;

import java.rmi.server.UnicastRemoteObject;
import java.security.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.util.*;

public class EncryptedAuthenticator extends UnicastRemoteObject implements Authenticator {
        private KeyPair keyPair;
        private final KeyPairGenerator generator;
        private final HashMap<String, byte[]> credentials;
        private final HashSet<String> sessions;
        private final Database db;
        public EncryptedAuthenticator() throws RemoteException, NoSuchAlgorithmException {
                this.generator = KeyPairGenerator.getInstance("RSA");
                this.keyPair = null;
                this.credentials = new HashMap<String, byte[]>();
                this.sessions = new HashSet<String>();
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
                this.credentials.put(username, this.getPasswordHash(passwordWithSalt));
        }

        public PublicKey generatePublicKey() throws RemoteException {
                if (this.keyPair == null) {
                        this.keyPair = this.generator.generateKeyPair();
                }

                return this.keyPair.getPublic();
        }

        public PrintServer authenticate(byte[] username, byte[] password) throws RemoteException {
                try {
                        Login login = this.authenticateLogin(username, password);
                        if(login.isAuthenticated()){
                                this.sessions.add(login.getUsername());
                                return new PrintServant();
                        }
                } catch (Exception e) {
                        System.out.println("DEBUG - authenticate: " + e.getMessage());
                }
                System.out.println("Failed authentication attempt - username: " + Arrays.toString(username));
                return null;
        }

        public void logOut(byte[] username) throws RemoteException {
                try {
                        String decryptedUsername = this.decrypt(username);
                        this.sessions.remove(decryptedUsername);
                } catch (Exception ignored) {
                }
        }

        private Login authenticateLogin(byte[] username, byte[] password) throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, ClassNotFoundException {
                String usernameDecrypted = this.decrypt(username);
                String passwordDecrypted = this.decrypt(password);

                Login login = this.db.getUser(new Login(usernameDecrypted));

                if(login.getPasswordHash() != null){
                        String decryptedPasswordWithSalt = passwordDecrypted + login.getSalt();
                        byte[] inputPasswordHashWithSalt = this.getPasswordHash(decryptedPasswordWithSalt);
                        if (Arrays.equals(inputPasswordHashWithSalt, login.getPasswordHash())) {
                                return new Login(login.getUsername(), login.getPasswordHash(), login.getSalt(), true);
                        }
                }
                return new Login(usernameDecrypted);
        }

        private String decrypt(byte[] cipherText) throws NoSuchAlgorithmException, InvalidKeyException,
                        IllegalBlockSizeException, NoSuchPaddingException {
                if (this.keyPair == null) {
                        return null;
                }

                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.DECRYPT_MODE, this.keyPair.getPrivate());

                try {
                        return new String(cipher.doFinal(cipherText));
                } catch (BadPaddingException e) {
                        return null;
                }
        }

        private byte[] getPasswordHash(String password) throws NoSuchAlgorithmException {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
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
