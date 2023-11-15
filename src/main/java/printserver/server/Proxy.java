package printserver.server;

import java.rmi.RemoteException;
import java.util.List;

public class Proxy extends PrintServant{
    private final List<String> permissions;
    private final String username;

    public Proxy(String username, List<String> permissions) throws RemoteException {
        super();
        this.permissions = permissions;
        this.username = username;
    }

    @Override
    public boolean print(String filename, String printer) throws RemoteException {
        if (permissions.contains("print")) return super.print(filename, printer);
        System.out.println(getPermissionDenied("print"));
        return false;
    }

    @Override
    public boolean queue(String printer) throws RemoteException {
        if (permissions.contains("queue")) return super.queue(printer);
        System.out.println(getPermissionDenied("queue"));
        return false;
    }

    @Override
    public boolean topQueue(String printer, int job) throws RemoteException {
        if (permissions.contains("topQueue")) return super.topQueue(printer, job);
        System.out.println(getPermissionDenied("topQueue"));
        return false;
    }

    @Override
    public boolean start() throws RemoteException {
        if (permissions.contains("start")) return super.start();
        System.out.println(getPermissionDenied("start"));
        return false;
    }

    @Override
    public boolean stop() throws RemoteException {
        if (permissions.contains("stop")) return super.stop();
        System.out.println(getPermissionDenied("stop"));
        return false;
    }

    @Override
    public boolean restart() throws RemoteException {
        if (permissions.contains("restart")) return super.restart();
        System.out.println(getPermissionDenied("restart"));
        return false;
    }

    @Override
    public boolean status(String printer) throws RemoteException {
        if (permissions.contains("status")) return super.status(printer);
        System.out.println(getPermissionDenied("status"));
        return false;
    }

    @Override
    public boolean readConfig(String printer) throws RemoteException {
        if (permissions.contains("readConfig")) return super.readConfig(printer);
        System.out.println(getPermissionDenied("readConfig"));
        return false;
    }

    @Override
    public boolean setConfig(String printer, String value) throws RemoteException {
        if (permissions.contains("setConfig")) super.setConfig(printer, value);
        System.out.println(getPermissionDenied("setConfig"));
        return false;
    }

    private String getPermissionDenied(String methodName){
        return String.format("Permission for %s is denied for user %s", methodName, username);
    }
}
