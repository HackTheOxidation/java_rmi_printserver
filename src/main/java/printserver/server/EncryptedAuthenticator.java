package printserver.server;

import printserver.common.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.security.MessageDigest;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class EncryptedAuthenticator extends UnicastRemoteObject implements Authenticator {
        private KeyPair keyPair;
        private KeyPairGenerator generator;
        private HashMap<String, byte[]> credentials;
        private HashSet<String> sessions;

        private static final int PORT = 5099;

        public EncryptedAuthenticator() throws Exception, RemoteException, NoSuchAlgorithmException {
                super(PORT, new RMISSLClientSocketFactory(), new RMISSLServerSocketFactory());
                this.generator = KeyPairGenerator.getInstance("RSA");
                this.keyPair = null;
                this.credentials = new HashMap<String, byte[]>();
                this.sessions = new HashSet<String>();
        }

        protected void addUser(String username, String password) throws NoSuchAlgorithmException, BadPaddingException {
                this.credentials.put(username, getPasswordHash(password));
        }

        public PublicKey generatePublicKey() throws RemoteException {
                if (this.keyPair == null) {
                        this.keyPair = this.generator.generateKeyPair();
                }

                return this.keyPair.getPublic();
        }

        public boolean authenticate(byte[] username, byte[] password) throws RemoteException {
                try {
                        String decryptedUsername = this.decrypt(username);
                        String decryptedPassword = this.decrypt(password);

                        System.out.println("DEBUG - Username: " + decryptedUsername);
                        System.out.println("DEBUG - Password: " + decryptedPassword);

                        if (this.credentials.containsKey(decryptedUsername)) {
                                byte[] passwordHash = this.getPasswordHash(decryptedPassword);

                                if (Arrays.equals(passwordHash, this.credentials.get(decryptedUsername))) {
                                        this.sessions.add(decryptedUsername);
                                        return true;
                                }
                        }
                } catch (Exception e) {
                        System.out.println("Failed authentication attempt - username: " + Arrays.toString(username));
                }

                return false;
        }

        public void logOut(byte[] username) throws RemoteException {
                try {
                        String decryptedUsername = this.decrypt(username);

                        if (this.sessions.contains(decryptedUsername)) {
                                this.sessions.remove(decryptedUsername);
                        }
                } catch (Exception e) {
                    System.out.println("EncryptedAuthenticator - Logout failed: " + e.getMessage());
                    e.printStackTrace();
                }
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

        private byte[] getPasswordHash(String password) throws NoSuchAlgorithmException, BadPaddingException {
                MessageDigest md = MessageDigest.getInstance("SHA3-256");
                md.update(password.getBytes());
                return md.digest();
        }
}
