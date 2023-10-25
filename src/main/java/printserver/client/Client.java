package printserver.client;

import printserver.common.PrintServer;
import printserver.common.Authenticator;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.PublicKey;
import java.io.Console;
import java.util.Scanner;
import javax.rmi.ssl.SslRMIClientSocketFactory;

import javax.crypto.Cipher;

public class Client {
        private static final int PORT = 5099;

        public static void main(String[] args) {
                Scanner scanner = new Scanner(System.in);
                int choice = -1;
                Console console = System.console();

                // SSL/TLS for Java RMI:
                // https://colinchjava.github.io/2023-09-14/11-51-09-553866-securing-java-rmi-applications-with-ssltls/
                // Specify the truststore and its password.
                System.setProperty("javax.net.ssl.trustStore", "path/to/truststore.jks");
                System.setProperty("javax.net.ssl.trustStorePassword", "truststore_password");

                SslRMIClientSocketFactory ssl = new SslRMIClientSocketFactory();
                Registry registry = LocateRegistry.getRegistry(InetAddress.getLocalHost().getHostName(), PORT, ssl);

                try {
                        System.out.println("Welcome to the Print Server client!");

                        System.out.println("Establishing connection to authenticator...");
                        Authenticator auth = (Authenticator) Naming.lookup("rmi://localhost:5099/login");
                        System.out.println("Connection established!");

                        PublicKey publicKey = auth.generatePublicKey();
                        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

                        System.out.print("login: ");
                        String username = scanner.next();
                        System.out.print("password (will not echo): ");
                        String password = new String(console.readPassword());
                        boolean is_authenticated = auth.authenticate(cipher.doFinal(username.getBytes()),
                                        cipher.doFinal(password.getBytes()));

                        if (!is_authenticated) {
                                System.out.println(
                                                "Authentication attempt failed. Restart the client and try again...");
                                System.exit(0);
                        }

                        System.out.println("Establishing connection to print server...");
                        PrintServer ps = (PrintServer) Naming.lookup("rmi://localhost:5099/printserver");
                        System.out.println("Connection established!");

                        while (choice != 0) {
                                System.out.println("Select a command: ");
                                System.out.println("\t1: Print a file.");
                                System.out.println("\t2: List the task queue for a printer.");
                                System.out.println("\t3: Move a job to the top of the task queue.");
                                System.out.println("\t4: Start the printer.");
                                System.out.println("\t5: Stop the printer.");
                                System.out.println("\t6: Restart the printer.");
                                System.out.println("\t7: Show the status of a printer.");
                                System.out.println("\t8: Read the configuration of a printer.");
                                System.out.println("\t9: Set the configuration of a printer.");
                                System.out.println("\t0: Exit the client.");

                                System.out.print(":> ");
                                choice = scanner.nextInt();
                                String printer = null;

                                switch (choice) {
                                        case 1:
                                                System.out.print("Enter the name of the file to print: ");
                                                String filename = scanner.next();

                                                System.out.print("Enter the name of the printer: ");
                                                printer = scanner.next();

                                                ps.print(filename, printer);
                                                break;
                                        case 2:
                                                System.out.print("Enter the name of the printer: ");
                                                printer = scanner.next();

                                                ps.queue(printer);
                                                break;
                                        case 3:
                                                System.out.print("Enter the name of the printer: ");
                                                printer = scanner.next();

                                                System.out.print("Enter the number of the job to prioritize: ");
                                                int job = scanner.nextInt();

                                                ps.topQueue(printer, job);
                                                break;
                                        case 4:
                                                ps.start();
                                                break;
                                        case 5:
                                                ps.stop();
                                                break;
                                        case 6:
                                                ps.restart();
                                                break;
                                        case 7:
                                                System.out.print("Enter the name of the printer: ");
                                                printer = scanner.next();

                                                ps.status(printer);
                                                break;
                                        case 8:
                                                System.out.print("Enter the name of the printer: ");
                                                printer = scanner.next();

                                                ps.readConfig(printer);
                                                break;
                                        case 9:
                                                System.out.print("Enter the name of the printer: ");
                                                printer = scanner.next();

                                                System.out.print("Enter the configuration value to set: ");
                                                String value = scanner.next();

                                                ps.setConfig(printer, value);
                                                break;
                                        case 0:
                                                System.out.println("Shutting down...");
                                                break;
                                        default:
                                                System.out.println("Invalid choice: " + choice);
                                }
                        }

                        auth.logOut(username.getBytes());
                } catch (Exception e) {
                } finally {
                        scanner.close();
                }
        }
}
