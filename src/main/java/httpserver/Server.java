package httpserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {
    private ServerSocket serverSocket;

    //constructor
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket; //calling the server socket that is in the main method
    }

    //method for starting the server
    public void start() {
        try {
            while(!serverSocket.isClosed()) {
                System.out.println("waiting for client's connection...");
                Socket clientSocket = serverSocket.accept();//serverSocket accepts the connection
                System.out.println("Client has established connection");
                ClientManager clientManager = new ClientManager(clientSocket); //calling the clientManager class
                Thread thread = new Thread(clientManager);//creating  a
                thread.start();
            }
        }catch(IOException e){
            if (serverSocket!= null){ // when the socket is not empty, close serversocket
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

    }
//main method to run the server
    public static void main(String[] args) throws IOException {
       ServerSocket serverSocket1 = new ServerSocket( 6055);// server socket to connect to each client
        Server server = new Server(serverSocket1); //pass the serverSocket into the server class
        server.start(); //to run the server
    }
}
