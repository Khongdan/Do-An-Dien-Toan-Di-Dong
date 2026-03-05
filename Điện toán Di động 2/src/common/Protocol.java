package common;

import java.io.*;
import java.nio.file.Files;

public class Protocol {

    public static final int PORT = 5500;
    private static final int BUFFER = 8192;

    // CLIENT → SERVER
    public static void sendFile(File file, DataOutputStream out) throws IOException {
        out.writeUTF(file.getName());
        out.writeLong(file.length());

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buf = new byte[BUFFER];
            int read;
            while ((read = fis.read(buf)) != -1) {
                out.write(buf, 0, read);
            }
        }
        out.flush();
    }

    // SERVER ← CLIENT
    public static void receiveFile(DataInputStream in, File saveDir) throws IOException {
        String fileName = in.readUTF();
        long size = in.readLong();

        File outFile = new File(saveDir, fileName);

        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            byte[] buf = new byte[BUFFER];
            long remaining = size;

            while (remaining > 0) {
                int read = in.read(buf, 0, (int)Math.min(buf.length, remaining));
                if (read == -1) break;
                fos.write(buf, 0, read);
                remaining -= read;
            }
        }

        System.out.println("Received: " + outFile.getAbsolutePath());
    }
}
