package client;
import javax.swing.SwingUtilities;
public class ClientApp {
    public static void main(String[] args) {
        // Khởi chạy giao diện Client trên luồng sự kiện Swing
        SwingUtilities.invokeLater(() -> new ClientGUI().setVisible(true));
    }
}