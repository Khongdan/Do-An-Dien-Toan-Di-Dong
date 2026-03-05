package server;
import javax.swing.SwingUtilities;
public class ServerApp {
    public static void main(String[] args) {
        // Khởi chạy giao diện Server trên luồng sự kiện Swing
        SwingUtilities.invokeLater(() -> new ServerGUI().setVisible(true));
    }
}