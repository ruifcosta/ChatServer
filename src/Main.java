public class Main {

    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[0]);

        ChatServer chat = new ChatServer(portNumber);
        chat.start();

    }
}
