package client;
import common.FileInfo;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import java.io.*;
import java.net.Socket;

public class ClientCore {
    public boolean sendFile(String ip, int port, File file, JProgressBar bar) {
        try (Socket socket = new Socket(ip, port);
             FileInputStream fis = new FileInputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

            FileInfo info = new FileInfo();
            info.setFileName(file.getName());
            info.setFileSize(file.length());
            oos.writeObject(info);
            oos.flush();

            byte[] buf = new byte[8192];
            long sent = 0;
            int read;
            while ((read = fis.read(buf)) != -1) {
                oos.write(buf, 0, read);
                sent += read;
                int percent = (int) ((sent * 100) / file.length());
                SwingUtilities.invokeLater(() -> bar.setValue(percent));
            }
            return true;
        } catch (Exception e) { return false; }
    }
}