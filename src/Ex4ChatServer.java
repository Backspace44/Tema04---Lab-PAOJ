import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

class ChatServer {
    private final static CopyOnWriteArrayList<Socket> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("Server started...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            clients.add(clientSocket);
            new Thread(new ClientHandler(clientSocket)).start();
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket socket;
        private final ThreadLocal<Socket> localSocket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            this.localSocket = ThreadLocal.withInitial(() -> this.socket);
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                String msg;
                while ((msg = in.readLine()) != null) {
                    if ("/quit".equals(msg)) break;
                    if ("/shutdown".equals(msg)) {
                        for (Socket s : clients) s.close();
                        System.exit(0);
                    }
                    for (Socket s : clients) {
                        if (!s.isClosed()) {
                            new PrintWriter(s.getOutputStream(), true).println(msg);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clients.remove(socket);
            }
        }
    }
}
