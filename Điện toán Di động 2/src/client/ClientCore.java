package client;
import common.FileInfo;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import java.io.*;
import java.net.Socket;

public class ClientCore {
    // Hàm thực hiện kết nối và gửi file
    public boolean sendFile(String ip, int port, File file, JProgressBar bar) {
        if (file == null) return false;
        try (Socket socket = new Socket(ip, port); // Kết nối đến Server qua IP và Cổng
             FileInputStream fis = new FileInputStream(file); // Đọc file từ ổ cứng
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

            // Gửi thông tin định danh file trước
            FileInfo info = new FileInfo();
            info.setFileName(file.getName());
            info.setFileSize(file.length());
            oos.writeObject(info); // Gửi đối tượng FileInfo
            oos.flush();

            byte[] buf = new byte[8192]; // Bộ đệm gửi 8KB
            long sent = 0;
            int read;
            // Đọc file từng phần và đẩy vào luồng mạng
            while ((read = fis.read(buf)) != -1) {
                oos.write(buf, 0, read);
                sent += read;
                // Tính phần trăm và cập nhật lên thanh tiến trình (Progress Bar)
                int percent = (int) ((sent * 100) / file.length());
                SwingUtilities.invokeLater(() -> bar.setValue(percent));
            }
            return true; // Trả về thành công
        } catch (Exception e) { return false; } // Trả về thất bại nếu có lỗi
    }
}