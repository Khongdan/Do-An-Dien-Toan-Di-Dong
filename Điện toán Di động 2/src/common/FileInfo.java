package common;
import java.io.Serializable;

// Lớp FileInfo phải thực thi interface Serializable để có thể gửi qua Socket
public class FileInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String fileName; // Tên file
    private long fileSize;   // Kích thước file (dùng để tính tiến trình %)

    // Các hàm Getter và Setter để truy xuất và cập nhật dữ liệu
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
}