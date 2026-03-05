package client;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ClientGUI extends JFrame {
    private JProgressBar bar = new JProgressBar(0, 100); // Thanh tiến trình gửi
    private JTextField txtIP = new JTextField("127.0.0.1", 15); // Ô nhập IP máy nhận
    private JLabel lblFileName = new JLabel("Chưa chọn file", JLabel.CENTER); // Nhãn hiện tên file đã chọn
    private File selectedFile; // Biến lưu trữ file người dùng chọn
    private ClientCore core = new ClientCore(); // Đối tượng xử lý gửi file

    public ClientGUI() {
        setTitle("Máy Gửi File (Client)");
        setSize(350, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1, 5, 5)); // Bố cục lưới 6 hàng

        JPanel p1 = new JPanel(); p1.add(new JLabel("IP Server:")); p1.add(txtIP);
        JButton btnChoose = new JButton("Chọn File...");
        JButton btnSend = new JButton("GỬI FILE");
        bar.setStringPainted(true); // Hiện con số % trên thanh tiến trình

        // Thêm các thành phần vào giao diện theo thứ tự từ trên xuống
        add(p1); 
        add(btnChoose); 
        add(lblFileName); 
        add(bar); 
        add(btnSend);

        // Sự kiện khi nhấn nút chọn file
        btnChoose.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser();
            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = jfc.getSelectedFile();
                // Cập nhật tên file lên nhãn hiển thị ngay sau khi chọn
                lblFileName.setText("Đã chọn: " + selectedFile.getName());
                bar.setValue(0); // Reset thanh tiến trình về 0
            }
        });

        // Sự kiện khi nhấn nút gửi file
        btnSend.addActionListener(e -> {
            if (selectedFile == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn file trước khi gửi!");
                return;
            }
            // Chạy việc gửi trong luồng riêng để không làm đứng giao diện (Not Responding)
            new Thread(() -> {
                btnSend.setEnabled(false); // Khóa nút gửi khi đang làm việc
                boolean ok = core.sendFile(txtIP.getText(), 8888, selectedFile, bar);
                JOptionPane.showMessageDialog(this, ok ? "Gửi file thành công!" : "Lỗi kết nối Server!");
                btnSend.setEnabled(true); // Mở lại nút gửi sau khi xong
            }).start();
        });
    }
}