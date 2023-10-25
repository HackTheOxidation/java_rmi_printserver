package printserver.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.security.PublicKey;

public interface Authenticator extends Remote {
        public PublicKey generatePublicKey() throws RemoteException;

        public PrintServer authenticate(byte[] username, byte[] password) throws RemoteException;

        public void logOut(byte[] username) throws RemoteException;
}
