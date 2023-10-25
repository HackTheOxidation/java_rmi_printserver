package printserver.server;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class App {
        private static final int PORT = 5099;

        public static void main(String[] args) throws RemoteException {
                // SSL/TLS for Java RMI:
                // https://colinchjava.github.io/2023-09-14/11-51-09-553866-securing-java-rmi-applications-with-ssltls/
                // Specify the truststore and its password.
                System.setProperty("javax.net.ssl.keyStore", "path/to/keystore.jks");
                System.setProperty("javax.net.ssl.keyStorePassword", "keystore_password");

                SslRMIServerSocketFactory ssl = new SslRMIServerSocketFactory();

                Registry registry = LocateRegistry.createRegistry(InetAddress.getLocalHost().getHostName(), PORT, ssl);
                registry.rebind("printserver", new PrintServant());
                try {
                        EncryptedAuthenticator auth = new EncryptedAuthenticator();
                        auth.addUser("user", "123");
                        registry.rebind("login", auth);
                } catch (Exception e) {
                        System.out.println("Failed to create authenticator: " + e.getMessage());
                }
        }
}
