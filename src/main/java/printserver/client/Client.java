package printserver.client;

import printserver.common.PrintServer;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.net.MalformedURLException;

import java.util.Scanner;

public class Client {
    public static void main(String[] args)
        throws RemoteException, NotBoundException, MalformedURLException {

        System.out.println("Establishing connection to print server...");
        PrintServer ps = (PrintServer) Naming.lookup("rmi://localhost:5099/printserver");
        System.out.println("Connection established!");

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        System.out.println("Welcome to the Print Server client!");
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
        scanner.close();
    }
}
