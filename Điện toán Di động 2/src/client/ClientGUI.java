package client;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ClientGUI extends JFrame {
    private JProgressBar bar = new JProgressBar(0, 100);
    private JTextField txtIP = new JTextField("127.0.0.1", 15);
    private File selectedFile;
    private ClientCore core = new ClientCore();

    public ClientGUI() {
        setTitle("Client - Basic");
        setSize(350, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 5, 5));

        JPanel p1 = new JPanel(); p1.add(new JLabel("IP:")); p1.add(txtIP);
        JButton btnChoose = new JButton("Chọn File");
        JButton btnSend = new JButton("Gửi File");
        bar.setStringPainted(true);

        add(p1); add(btnChoose); add(new JLabel("File:", JLabel.CENTER)); add(bar); add(btnSend);

        btnChoose.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser();
            if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) selectedFile = jfc.getSelectedFile();
        });

        btnSend.addActionListener(e -> {
            new Thread(() -> {
                btnSend.setEnabled(false);
                boolean ok = core.sendFile(txtIP.getText(), 8888, selectedFile, bar);
                JOptionPane.showMessageDialog(this, ok ? "Thành công!" : "Lỗi!");
                btnSend.setEnabled(true);
            }).start();
        });
    }
}