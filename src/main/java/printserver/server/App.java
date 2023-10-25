package printserver.server;

import printserver.common.*;

import java.net.InetAddress;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class App {
        private static final int PORT = 5099;

        public static void main(String[] args) throws RemoteException {
                if (System.getSecurityManager() == null) {
                        System.setSecurityManager(new RMISecurityManager());
                }

                try {
                        Registry registry = LocateRegistry.createRegistry(
                                        PORT,
                                        new RMISSLClientSocketFactory(), new RMISSLServerSocketFactory());

                        registry.rebind("printserver", new PrintServant());

                        EncryptedAuthenticator auth = new EncryptedAuthenticator();
                        auth.addUser("user", "123");
                        registry.rebind("login", auth);
                } catch (Exception e) {
                        System.out.println("Failed to create authenticator: " + e.getMessage());
                        e.printStackTrace();
                }
        }
}
