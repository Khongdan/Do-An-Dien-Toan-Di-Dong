package server;
import common.FileInfo;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class ServerCore {
    private ServerSocket serverSocket; // Cổng kết nối của Server
    private boolean isRunning = false; // Trạng thái hoạt động của Server
    private Consumer<String> onFileReceived; // Hàm gọi ngược để cập nhật tên file lên giao diện

    // Thiết lập hành động sẽ làm khi nhận xong file (hiển thị lên danh sách)
    public void setOnFileReceived(Consumer<String> callback) {
        this.onFileReceived = callback;
    }

    // Khởi động Server lắng nghe tại cổng Port
    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port); // Mở cổng
            isRunning = true;
            while (isRunning) {
                Socket socket = serverSocket.accept(); // Chờ và chấp nhận kết nối từ Client
                // Mỗi khi có máy khách kết nối, tạo một luồng (Thread) mới để xử lý riêng
                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException e) {
            if (isRunning) System.out.println("Server đã dừng.");
        }
    }

    // Logic xử lý nhận dữ liệu từ máy khách
    private void handleClient(Socket socket) {
        try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            // Bước 1: Nhận đối tượng chứa thông tin file trước
            FileInfo fileInfo = (FileInfo) ois.readObject();
            
            // Bước 2: Tạo thư mục "ReceivedFiles" nếu chưa tồn tại
            File folder = new File("ReceivedFiles");
            if (!folder.exists()) folder.mkdirs();

            // Bước 3: Tạo file mới bên trong thư mục ReceivedFiles
            File saveFile = new File(folder, "received_" + fileInfo.getFileName());
            try (FileOutputStream fos = new FileOutputStream(saveFile)) {
                byte[] buffer = new byte[8192]; // Bộ đệm 8KB để nhận dữ liệu
                long totalRead = 0;
                int read;
                // Nhận từng khối dữ liệu cho đến khi đủ dung lượng file
                while (totalRead < fileInfo.getFileSize() && (read = ois.read(buffer)) != -1) {
                    fos.write(buffer, 0, read);
                    totalRead += read;
                }
            }
            // Bước 4: Báo cho giao diện biết đã nhận xong để hiện lên danh sách
            if (onFileReceived != null) onFileReceived.accept(fileInfo.getFileName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException e) {} // Đóng kết nối máy khách
        }
    }

    // Dừng Server và đóng cổng kết nối
    public void stop() {
        isRunning = false;
        try { if (serverSocket != null) serverSocket.close(); } catch (Exception e) {}
    }
}