package printserver.server;

import printserver.common.PrintServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrintServant extends UnicastRemoteObject implements PrintServer {
        private EncryptedAuthenticator auth;
        public PrintServant() throws RemoteException {
                super();
                auth = new EncryptedAuthenticator();
        }

        public void print(String username, String filename, String printer) throws RemoteException {
                System.out.println("Invoked print() with filename: " + filename + ", printer: " + printer);
        }

        public void queue(String username, String printer) throws RemoteException {
                System.out.println("Invoked queue() with printer: " + printer);
        }

        public void topQueue(String username, String printer, int job) throws RemoteException {
                System.out.println("Invoked topQueue() with printer: " + printer + ", job: " + job);
        }

        public void start(String username) throws RemoteException {
                System.out.println("Invoked start()");
        }

        public void stop(String username) throws RemoteException {
                System.out.println("Invoked stop()");
        }

        public void restart(String username) throws RemoteException {
                System.out.println("Invoked restart()");
        }

        public void status(String username, String printer) throws RemoteException {
                System.out.println("Invoked status() with printer: " + printer);
        }

        public void readConfig(String username, String printer) throws RemoteException {
                System.out.println("Invoked readConfig() with printer: " + printer);
        }

        public void setConfig(String username, String printer, String value) throws RemoteException {
                System.out.println("Invoked setConfig() with printer: " + printer + ", value: " + value);
        }
}
