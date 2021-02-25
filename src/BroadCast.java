import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class BroadCast {
    ArrayList<ChatThread> clients =new ArrayList<>();




    public synchronized void sendMessage(String fromOut, ChatThread thread) {
        System.out.println(thread.getName()+": "+fromOut);
        for (ChatThread threads:clients) {

            if (threads.equals(thread)) {
                continue;
            }
            threads.message(thread.getName() + ": " + fromOut + "\n");
        }
    }

    public void add(ChatThread thread) {
        clients.add(thread);
    }


    public boolean privateMessage(String name, String message,ChatThread thread) {
        for (ChatThread threads:clients) {
            if (threads.getName().equals(name)) {
                System.out.println(message);
                threads.message("From "+thread.getName()+": "+message + "\n");
                return true;
            }
        }
        return false;
    }

    public boolean kick(String name, ChatThread thread) {
        for (ChatThread threads:clients) {
            if (threads.getName().equals(name)) {
                sendMessage("Kicked "+name+" from server!",thread);
                threads.close();
                return true;
            }
        }
        return false;
    }

    public synchronized void remove(ChatThread thread) {

        clients.remove(thread);
    }
}
