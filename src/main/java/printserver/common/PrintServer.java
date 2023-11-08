package printserver.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrintServer extends Remote {
        void print(String username, String filename, String printer) throws RemoteException;

        void queue(String username, String printer) throws RemoteException;

        void topQueue(String username, String printer, int job) throws RemoteException;

        void start(String username) throws RemoteException;

        void stop(String username) throws RemoteException;

        void restart(String username) throws RemoteException;

        void status(String username, String printer) throws RemoteException;

        void readConfig(String username, String printer) throws RemoteException;

        void setConfig(String username, String printer, String value) throws RemoteException;
}
