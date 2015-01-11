import java.io.*;
import java.security.*;

public class ComputeSHA {
    byte mdBytes[];
    MessageDigest md = null;
    FileInputStream inputStream = null;

    public ComputeSHA() {
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.getStackTrace();
        }
    }

    public void parseFile(String file) throws FileNotFoundException, IOException {
        try {
            inputStream = new FileInputStream(file);
            int nread = 0;
            byte[] data = new byte[1024];

            while ((nread = inputStream.read(data)) != -1) {
                md.update(data, 0, nread);
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found!");
        }
    }

    public void printSHA() {
        mdBytes = md.digest();

        // Convert byte to hex format
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdBytes.length; i++) {
            sb.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        System.out.println(sb.toString());
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException();
        }

        String filePath = args[0];
        ComputeSHA c = new ComputeSHA();
        try {
            c.parseFile(filePath);
        } catch (FileNotFoundException e) {
            System.exit(1);
        }
        c.printSHA();
    }
}
