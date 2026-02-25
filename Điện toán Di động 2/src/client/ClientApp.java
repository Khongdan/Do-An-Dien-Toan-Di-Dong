package client;
import javax.swing.*;

public class ClientApp {
    public static void main(String[] args) {
        try {
            // Dòng này giúp giao diện mượt mà như ứng dụng Windows thật
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new ClientGUI().setVisible(true));
    }
}