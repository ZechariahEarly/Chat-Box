//@author ZXE200000
//Project 2 4348.5U1

import java.net.*;
import java.io.*;
import java.util.*;

class ThreadServer extends Thread { //Thread for the Server class
    private Thread t;
    private String threadName;

    ThreadServer(String name) { //Contructor
        threadName = name;
        System.out.println("Creating " + threadName);
    }

    public void run() { //ThreadServer run method
        System.out.println("Running " + threadName); //Print name

        try {
            ServerSocket ss = new ServerSocket(6666); //Make serversocket on port 6666
            ss.setReuseAddress(true); //reuse this address for other clients
            while(true){ //create a new ClienHandler thread for each client that connects
                Socket client = ss.accept();// establishes connection
                System.out.println("New client connected " + client.getInetAddress().getHostAddress()); //print address of clients
                ClientHandler clientSock = new ClientHandler(client); //new ClientHandler
                new Thread(clientSock).start(); //New thread
            }
        } catch (Exception e) {
            System.out.println(e); //error handling
        }
    }
    private static class ClientHandler implements Runnable { //ClientHandler class
        private final Socket clientSocket;
  
        // Constructor
        public ClientHandler(Socket socket)
        {
            this.clientSocket = socket;
        }
  
        public void run() //run method for ClientHandler
        {
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                DataInputStream din = new DataInputStream(clientSocket.getInputStream()); //Read incoming data
                String str = "";
                while (true) {
                    str = din.readUTF();
                    System.out.println("client says: " + str); //print data that comes from clients
                    if(str=="stop"){
                        break;
                    }
                }
                din.close(); //close DataInputStream
                clientSocket.close(); //close socket
            }
            catch (IOException e) {
                e.printStackTrace(); //error handling
            }
        }
    }

}

class ThreadClient extends Thread { //ThreadClient class
    private Thread t;
    private String threadName;
    Scanner scanner = new Scanner(System.in); //new scanner for input

    ThreadClient(String name) {
        threadName = name;
        System.out.println("Creating " + threadName);
    }

    public void run() {
        System.out.println("Running " + threadName);

        try {
            System.out.println("Enter P1 IP Address "); //Ip address for each server to connect to
            String ipAddress1 = scanner.nextLine();
            Socket s1 = new Socket(ipAddress1, 6666); //create new socket to connect to
            DataOutputStream dout1 = new DataOutputStream(s1.getOutputStream()); //

            System.out.println("Enter P2 IP Address ");
            String ipAddress2 = scanner.nextLine();
            Socket s2 = new Socket(ipAddress2, 6666);
            DataOutputStream dout2 = new DataOutputStream(s2.getOutputStream());

            System.out.println("Enter P3 IP Address ");
            String ipAddress3 = scanner.nextLine();
            Socket s3 = new Socket(ipAddress3, 6666);
            DataOutputStream dout3 = new DataOutputStream(s3.getOutputStream());

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String str = "";
            System.out.println("Command Prompt: send *id* MESSAGE");
            while (true) {
                str = br.readLine();
                String[] arrOfStr = str.split(" ", 3);
                if(arrOfStr[0].equals("send")){
                    if(arrOfStr[1].equals("1")){
                        dout1.writeUTF(arrOfStr[2]);
                        dout1.flush();
                    }
                    if(arrOfStr[1].equals("2")){
                        dout2.writeUTF(arrOfStr[2]);
                        dout2.flush();
                    }
                    if(arrOfStr[1].equals("3")){
                        dout3.writeUTF(arrOfStr[2]);
                        dout3.flush();
                    }
                    if(arrOfStr[1].equals("0")){
                        dout1.writeUTF(arrOfStr[2]);
                        dout1.flush();
                        dout2.writeUTF(arrOfStr[2]);
                        dout2.flush();
                        dout3.writeUTF(arrOfStr[2]);
                        dout3.flush();
                    }
                } else {
                    if(arrOfStr[0].equals("stop")){
                        dout1.writeUTF("Stop");
                        dout1.flush();
                        dout2.writeUTF("Stop");
                        dout2.flush();
                        dout3.writeUTF("Stop");
                        dout3.flush();
                        dout1.close();
                        dout2.close();
                        dout3.close();
                        break;
                    }
                    System.out.println("Please try again and enter send *id* MESSAGE");
                }
            }
            s1.close();
            s2.close();
            s3.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}

public class Project2 {

    public static void main(String args[]) {

        Thread T1 = new ThreadServer("ThreadServer");
        T1.start();

        Thread T2 = new ThreadClient("ThreadClient");
        T2.start();
    }
}
