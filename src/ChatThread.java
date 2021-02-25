import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ChatThread implements Runnable{

    private Socket clientSocket;
    private BroadCast broadCast;
    DataOutputStream out = null;
    private String name;


    public ChatThread(Socket clientSocket, BroadCast broadCast, int number) {
        this.clientSocket=clientSocket;
        this.broadCast=broadCast;
        try {
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.name = "Cadet-"+number;

    }

    public void message(String toOut){

        try {

            out.writeBytes(toOut);
            out.flush();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        try {

            System.out.println("Connection accepted from "+clientSocket.getInetAddress().getHostAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            broadCast.add(this);
            out.writeBytes("You are "+name+"\n");
            out.flush();
            String fromOut;
            while (true){
                try {
                    fromOut = in.readLine();
                }catch (SocketException e){
                    return;
                }
                if (fromOut.equals("/exit")){break;}
                String firstLetter = fromOut.split("")[0];
                if (firstLetter.equals("/")){
                    doCommand(fromOut);
                    continue;
                }
                broadCast.sendMessage(fromOut,this);
            }

// STEP5: Close the streams
            in.close();
            out.close();
// STEP6: Close the sockets
            broadCast.remove(this);
            clientSocket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private void doCommand(String fromOut) {
        String[] array =fromOut.split(" ");
        String command = array[0];
        if(Commands.HELP.getValue().equals(command)){
            help(array);
        }else if (Commands.LIST.getValue().equals(command)){
            list(array);
        }else if (Commands.NAME.getValue().equals(command)){
            changeName(array);
        }else if (Commands.PRIVATE.getValue().equals(command)){
            sendPrivateMessage(array);
        }else if (Commands.KICK.getValue().equals(command)){
            kick(array);
        } else {
            message("Command doesn't exist"+"\n");
        }


    }

    private void kick(String[] array) {
        if (array.length >2){
            message("Wrong implementation! Only 1 user to kick at a time!"+"\n");
            return;
        }
        boolean isSent = broadCast.kick(array[1],this);
        if (!isSent){
            message("There is no user with that name!"+"\n");
        }
    }

    private void sendPrivateMessage(String[] array) {
        if (array.length <3){
        message("Wrong implementation! Its is '/private <name> <message>'!"+"\n");
        return;
    }
        StringBuilder builder = new StringBuilder();
        for (int i = 2; i < array.length; i++) {
            builder.append(array[i] +" ");
        }
        String str = builder.toString();
        boolean isSent = broadCast.privateMessage(array[1],str,this);
        if (!isSent){
            message("There is no user with that name!"+"\n");
        }

    }

    private void help(String[] array) {
        if (array.length >2){
            message("Wrong implementation! Only 1 command at a time!"+"\n");
            return;
        }
        for (Commands command:Commands.values()) {
            if (command.getValue().equals(array[1])) {
                message(command.getInstructions());
                return;
            }
        }
       message("No command with that name"+"\n");
    }


    public void changeName(String[] array) {
        if (array.length >2){
            message("Wrong implementation! No spaces in name!!"+"\n");
            return;
        }
        String previousName = this.name;
        this.name =array[1];
        message("You name changed to "+name+"!"+"\n");
        broadCast.sendMessage(previousName+ " name changed to "+name+"!",this);
    }

    public void list(String[] array){
        if (array.length >1){
            message("Wrong implementation! Only type '/list'"+"\n");
            return;
        }
        String toOut = "The commands are: "+"\n";
        for (Commands command:Commands.values()) {
            toOut += command.getValue()+"\n";
        }
        message(toOut);
    }


    public void close(){

        try {
            out.close();
            broadCast.remove(this);
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
// STEP6: Close the sockets

    }
}
