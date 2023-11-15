package printserver.server;

import java.rmi.RemoteException;
import java.util.List;

public class Proxy extends PrintServant{
    private final List<String> acl;

    public Proxy(List<String> access) throws RemoteException {
        super();
        acl = access;
    }

    @Override
    public void print(String filename, String printer) throws RemoteException {
        if (acl.contains("print")) {
            super.print(filename, printer);
        } else {
            System.out.println("Permission for print is denied");
        }
    }

    @Override
    public void queue(String printer) throws RemoteException {
        if (acl.contains("queue")) {
            super.queue(printer);
        } else {
            System.out.println("Permission for queue is denied");
        } 
    }

    @Override
    public void topQueue(String printer, int job) throws RemoteException {
        if (acl.contains("topQueue")) {
            super.topQueue(printer, job);
        } else {
            System.out.println("Permission for topQueue is denied");
        } 
    }
    
    @Override
    public void start() throws RemoteException {
        if (acl.contains("start")) {
            super.start();
        } else {
            System.out.println("Permission for start is denied");
        } 
    }

    @Override
    public void stop() throws RemoteException {
        if (acl.contains("stop")) {
            super.stop();
        } else {
            System.out.println("Permission for stop is denied");
        } 
    }

    @Override
    public void restart() throws RemoteException {
        if (acl.contains("restart")) {
            super.restart();
        } else {
            System.out.println("Permission for restart is denied");
        } 
    }

    @Override
    public void status(String printer) throws RemoteException {
        if (acl.contains("status")) {
            super.status(printer);
        } else {
            System.out.println("Permission for status is denied");
        } 
    }

    @Override
    public void readConfig(String printer) throws RemoteException {
        if (acl.contains("readConfig")) {
            super.readConfig(printer);
        } else {
            System.out.println("Permission for readConfig is denied");
        } 
    }

    @Override
    public void setConfig(String printer, String value) throws RemoteException {
        if (acl.contains("setConfig")) {
            super.setConfig(printer, value);
        } else {
            System.out.println("Permission for setConfig is denied");
        } 
    }
}
