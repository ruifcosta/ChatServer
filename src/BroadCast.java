import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class BroadCast {
    ArrayList<ChatThread> clients =new ArrayList<>();




    public synchronized void sendMessage(String fromOut, ChatThread thread) {
        System.out.println(fromOut);
        for (ChatThread threads:clients) {
            if (threads.equals(thread)) {
                continue;
           }
            threads.message(fromOut + "\n");

        }
    }

    public void add(ChatThread thread) {
        clients.add(thread);
    }


}
