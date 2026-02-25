package server;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ServerGUI extends JFrame {
    private ServerCore core = new ServerCore();
    private DefaultListModel<String> listModel = new DefaultListModel<>();

    public ServerGUI() {
        setTitle("Server - Basic");
        setSize(400, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel pnlControl = new JPanel();
        JButton btnStart = new JButton("Bắt đầu");
        JButton btnStop = new JButton("Dừng");
        JButton btnOpen = new JButton("Mở thư mục");
        btnStop.setEnabled(false);
        pnlControl.add(btnStart); pnlControl.add(btnStop); pnlControl.add(btnOpen);
        add(pnlControl, BorderLayout.NORTH);

        add(new JScrollPane(new JList<>(listModel)), BorderLayout.CENTER);

        core.setOnFileReceived(name -> SwingUtilities.invokeLater(() -> listModel.addElement("Đã nhận: " + name)));

        btnStart.addActionListener(e -> {
            new Thread(() -> core.startServer(8888)).start();
            btnStart.setEnabled(false); btnStop.setEnabled(true);
        });

        btnStop.addActionListener(e -> {
            core.stop();
            btnStart.setEnabled(true); btnStop.setEnabled(false);
        });

        btnOpen.addActionListener(e -> {
            try { Desktop.getDesktop().open(new File("ReceivedFiles")); } 
            catch (Exception ex) { JOptionPane.showMessageDialog(this, "Thư mục chưa có file nào!"); }
        });
    }
}