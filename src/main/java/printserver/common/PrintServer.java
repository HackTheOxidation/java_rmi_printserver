package printserver.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrintServer extends Remote {
        boolean print(String filename, String printer) throws RemoteException;

        boolean queue(String printer) throws RemoteException;

        boolean topQueue(String printer, int job) throws RemoteException;

        boolean start() throws RemoteException;

        boolean stop() throws RemoteException;

        boolean restart() throws RemoteException;

        boolean status(String printer) throws RemoteException;

        boolean readConfig(String printer) throws RemoteException;

        boolean setConfig(String printer, String value) throws RemoteException;
}
