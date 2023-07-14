package printserver.client;

import printserver.common.PrintServer;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.net.MalformedURLException;

public class Client {
    public static void main(String[] args)
        throws RemoteException, NotBoundException, MalformedURLException {
        PrintServer ps = (PrintServer) Naming.lookup("rmi://localhost:5099/printserver");
        ps.print("text.pdf", "printer1");
        ps.queue("printer1");
        ps.topQueue("printer1", 0);
        ps.start();
        ps.stop();
        ps.restart();
        ps.status("printer1");
        ps.readConfig("printer1");
        ps.setConfig("printer1", "size:A4");
    }
}
