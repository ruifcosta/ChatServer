import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatThread implements Runnable{

    private Socket clientSocket;
    private BroadCast broadCast;
    DataOutputStream out = null;
    private int number;

    public ChatThread(Socket clientSocket, BroadCast broadCast, int number) {
        this.clientSocket=clientSocket;
        this.broadCast=broadCast;
        try {
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.number= number;
    }

    public void message(String toOut){

        try {

            out.writeBytes("Cadet-"+number+": " + toOut);
            out.flush();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {

            System.out.println("Connection accepted from "+clientSocket.getInetAddress().getHostAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            broadCast.add(this);
            String fromOut;
            while (true){

                fromOut= in.readLine();
                if (fromOut==null){break;}

                broadCast.sendMessage(fromOut,this);
            }

// STEP5: Close the streams
            in.close();
            out.close();
// STEP6: Close the sockets
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
