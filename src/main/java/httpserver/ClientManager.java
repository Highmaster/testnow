package httpserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientManager implements Runnable{
 private Socket clientSocket; //the client socket accepted by the server
 private  String UserName;
 private static ArrayList<ClientManager> clients = new ArrayList<>();//no of clients that connects to the server will be added to the arraylist
 private BufferedReader bufferedReader;
 private PrintWriter printWriter;

 public ClientManager(Socket clientSocket){
    try {
        this.clientSocket = clientSocket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        this.UserName = bufferedReader.readLine();
        clients.add(this);//instantiating the clientsManager and add it to the clients ArrayList
        broadcastMessage(UserName + " Just joined Famechat");
    }catch(IOException e){
        closeStreams(clientSocket, bufferedReader, printWriter);
    }
 }

    @Override
    public void run() {
        String clientsMessage;
     while (clientSocket.isConnected()) {
         try {
              clientsMessage = bufferedReader.readLine();
             if (clientsMessage == null) {
                removeClients();
                 break;
             }
             broadcastMessage(UserName+": "+clientsMessage);// broadcast the message to every person in the groupchat except the sender
         }catch(IOException e) {
            closeStreams(clientSocket, bufferedReader, printWriter);
            break;
        }
     }

    }

    public void broadcastMessage(String message){
     for(ClientManager clientManager: clients) { //to loop through clientManagers in the client ArrayList
         if(!clientManager.equals(this)){ //if a message is sent, it broadcasts to everybody except the clientManager
            clientManager.printWriter.println(message);
         }
     }
    }

    public void removeClients(){
     clients.remove(this);
     broadcastMessage(UserName +" has been removed");
    }

    public void closeStreams(Socket socket, BufferedReader bufferedReader, PrintWriter printWriter){
     removeClients();//method to pass in the fields and close them if it is empty with try and catch
     try{
         if(socket!= null){
             socket.close();
         }
         if (bufferedReader != null){
             bufferedReader.close();
         }
         if (printWriter != null) {
             printWriter.close();
         }
     }catch(IOException Ex){
         Ex.printStackTrace();
     }

    }
}
