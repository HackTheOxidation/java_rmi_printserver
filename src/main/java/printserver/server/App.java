package printserver.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class App {
        public static void main(String[] args) throws RemoteException {
                Registry registry = LocateRegistry.createRegistry(5099);
                try {
                        EncryptedAuthenticator auth = new EncryptedAuthenticator();

                        registry.rebind("login", auth);
                } catch (Exception e) {
                        System.out.println("Failed to create authenticator: " + e.getMessage());
                }
        }
}
