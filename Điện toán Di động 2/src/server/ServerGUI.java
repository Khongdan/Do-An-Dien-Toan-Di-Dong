package server;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ServerGUI extends JFrame {
    private ServerCore core = new ServerCore(); // Đối tượng xử lý logic Server
    private DefaultListModel<String> listModel = new DefaultListModel<>(); // Danh sách hiển thị file

    public ServerGUI() {
        setTitle("Máy Nhận File (Server)");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Căn giữa màn hình
        setLayout(new BorderLayout()); // Sử dụng bố cục biên

        // Khu vực các nút điều khiển phía trên
        JPanel pnlTop = new JPanel();
        JButton btnStart = new JButton("Bắt đầu");
        JButton btnStop = new JButton("Dừng");
        JButton btnOpen = new JButton("Mở Thư Mục");
        btnStop.setEnabled(false);
        pnlTop.add(btnStart); pnlTop.add(btnStop); pnlTop.add(btnOpen);
        add(pnlTop, BorderLayout.NORTH);

        // Khu vực danh sách file nhận được ở giữa
        add(new JScrollPane(new JList<>(listModel)), BorderLayout.CENTER);

        // Thiết lập: Khi nhận xong file thì thêm tên file vào danh sách hiển thị
        core.setOnFileReceived(name -> SwingUtilities.invokeLater(() -> 
            listModel.insertElementAt("Đã nhận: " + name, 0)));

        // Sự kiện khi nhấn nút Bắt đầu
        btnStart.addActionListener(e -> {
            new Thread(() -> core.startServer(8888)).start(); // Chạy Server ở luồng riêng
            btnStart.setEnabled(false); btnStop.setEnabled(true);
        });

        // Sự kiện khi nhấn nút Dừng
        btnStop.addActionListener(e -> {
            core.stop();
            btnStart.setEnabled(true); btnStop.setEnabled(false);
        });

        // Sự kiện mở thư mục chứa file đã nhận
        btnOpen.addActionListener(e -> {
            try { Desktop.getDesktop().open(new File("ReceivedFiles")); } 
            catch (Exception ex) { JOptionPane.showMessageDialog(this, "Chưa có folder hoặc chưa nhận file!"); }
        });
    }
}