package printserver.server;

import printserver.common.PrintServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrintServant extends UnicastRemoteObject implements PrintServer {
        public PrintServant() throws RemoteException {
                super();
        }

        public boolean print(String filename, String printer) throws RemoteException {
                System.out.println("Invoked print() with filename: " + filename + ", printer: " + printer);
                return true;
        }

        public boolean queue(String printer) throws RemoteException {
                System.out.println("Invoked queue() with printer: " + printer);
                return true;
        }

        public boolean topQueue(String printer, int job) throws RemoteException {
                System.out.println("Invoked topQueue() with printer: " + printer + ", job: " + job);
                return true;
        }

        public boolean start() throws RemoteException {
                System.out.println("Invoked start()");
                return true;
        }

        public boolean stop() throws RemoteException {
                System.out.println("Invoked stop()");
                return true;
        }

        public boolean restart() throws RemoteException {
                System.out.println("Invoked restart()");
                return true;
        }

        public boolean status(String printer) throws RemoteException {
                System.out.println("Invoked status() with printer: " + printer);
                return true;
        }

        public boolean readConfig(String printer) throws RemoteException {
                System.out.println("Invoked readConfig() with printer: " + printer);
                return true;
        }

        public boolean setConfig(String printer, String value) throws RemoteException {
                System.out.println("Invoked setConfig() with printer: " + printer + ", value: " + value);
                return true;
        }
}
