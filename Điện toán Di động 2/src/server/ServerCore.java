package server;
import common.FileInfo;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class ServerCore {
    private ServerSocket serverSocket;
    private boolean isRunning = false;
    private Consumer<String> onFileReceived;

    public void setOnFileReceived(Consumer<String> callback) {
        this.onFileReceived = callback;
    }

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            isRunning = true;
            while (isRunning) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException e) {
            if (isRunning) e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) {
        try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            FileInfo fileInfo = (FileInfo) ois.readObject();
            
            // Tạo thư mục riêng
            File folder = new File("ReceivedFiles");
            if (!folder.exists()) folder.mkdirs();

            File saveFile = new File(folder, "received_" + fileInfo.getFileName());
            try (FileOutputStream fos = new FileOutputStream(saveFile)) {
                byte[] buffer = new byte[8192];
                long totalRead = 0;
                int read;
                while (totalRead < fileInfo.getFileSize() && (read = ois.read(buffer)) != -1) {
                    fos.write(buffer, 0, read);
                    totalRead += read;
                }
            }
            if (onFileReceived != null) onFileReceived.accept(fileInfo.getFileName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        isRunning = false;
        try { if (serverSocket != null) serverSocket.close(); } catch (Exception e) {}
    }
}