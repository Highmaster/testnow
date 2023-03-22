package httpserver;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private String userName;

//constructor for passing in the socket and username and instantiating the different fields
    public Client(Socket socket, String userName) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
            this.userName = userName;
        }catch(IOException e){
            close(socket, bufferedReader,printWriter);
        }

    }
    //method for sending message
    public void sendMessage(){
           try {
               printWriter.println(userName);
               BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // reading input from the user
               while (socket.isConnected()) { // while loop for reading message while socket is connected
                   String clientMessage = br.readLine();// takes the input from the client(user).
                   printWriter.println(clientMessage);
               }
           }catch(IOException e){
               close(socket, bufferedReader, printWriter);
           }

    }

    public void receiveMessage(){
        //creating a runnable for the new thread
        new Thread(() -> {
            String chatMessage;
            while(socket.isConnected()){
                try {
                    chatMessage = bufferedReader.readLine();
                    if (chatMessage == null) {
                        System.out.println("Server down");
                        break;
                    }
                    System.out.println(chatMessage);
                }catch(IOException e) {
                   close(socket, bufferedReader, printWriter);
                    break;//closes the chatMessage
                }
            }
        }).start();// starts the thread
        }

        public void close(Socket socket, BufferedReader bufferedReader, PrintWriter printWriter) {
        //whenever a socket is disconnected || an exception is thrown, close the socket, buffered reader && printwriter
            try{
                if(socket != null){
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

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username:  ");
        String userName = scanner.nextLine();
        Socket clientSocket = new Socket("localhost", 6055);
        Client  client = new Client(clientSocket, userName);
        client.receiveMessage();
        client.sendMessage();

    }
}
