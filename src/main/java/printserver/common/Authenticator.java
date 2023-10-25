package printserver.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.security.PublicKey;

public interface Authenticator extends Remote {
        PrintServer authenticate(String username, String password) throws RemoteException;
}
