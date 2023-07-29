package printserver.server;

import printserver.common.PrintServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrintServant extends UnicastRemoteObject implements PrintServer {
        public PrintServant() throws RemoteException {
                super();
        }

        public void print(String filename, String printer) throws RemoteException {
                System.out.println("Invoked print() with filename: " + filename + ", printer: " + printer);
        }

        public void queue(String printer) throws RemoteException {
                System.out.println("Invoked queue() with printer: " + printer);
        }

        public void topQueue(String printer, int job) throws RemoteException {
                System.out.println("Invoked topQueue() with printer: " + printer + ", job: " + job);
        }

        public void start() throws RemoteException {
                System.out.println("Invoked start()");
        }

        public void stop() throws RemoteException {
                System.out.println("Invoked stop()");
        }

        public void restart() throws RemoteException {
                System.out.println("Invoked restart()");
        }

        public void status(String printer) throws RemoteException {
                System.out.println("Invoked status() with printer: " + printer);
        }

        public void readConfig(String printer) throws RemoteException {
                System.out.println("Invoked readConfig() with printer: " + printer);
        }

        public void setConfig(String printer, String value) throws RemoteException {
                System.out.println("Invoked setConfig() with printer: " + printer + ", value: " + value);
        }
}
