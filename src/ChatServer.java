import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private ServerSocket serverSocket = null;

    public ChatServer(int portNumber) {
        try {
            this.serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void start() {
        // Allocate a Pool of 4 fixed threads
        ExecutorService fixedPool = Executors.newFixedThreadPool(2);
        BroadCast broadCast= new BroadCast();
        int number=-1;
// submit 10 tasks to be executed
        while (serverSocket.isBound()){

            try {
                Socket clientSocket = serverSocket.accept();
                number++;
                fixedPool.submit(new ChatThread(clientSocket,broadCast,number));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

// shut down the executor after all submitted tasks are finished
        fixedPool.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}