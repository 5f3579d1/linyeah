import java.io.*;
import java.util.Random;

/**
 * Created by k on 5/27/15.
 */
public class CreateTestFile {

    private static final char[] symbols;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            tmp.append(ch);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            tmp.append(ch);
        symbols = tmp.toString().toCharArray();
    }

    private static final Random random = new Random();

    private static final char[] buf = new char[6];


    public static String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    public static void main(String[] args) {
        main();
    }

    public static void main() {
        try {
            File test = new File("test.pdf");
            for (int i = 0; i < 9; i++) {
                String user = nextString();
                String dir = "data/" + user;

                if (random.nextBoolean())
                    for (int j = 0; j < random.nextInt(5); j++) {
                        String str = dir + "/" + j;
                        creatFile(test, str);
                    }
                else {
                    creatFile(test, dir);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void creatFile(File test, String dir) throws IOException {
        new File(dir).mkdirs();
        OutputStream out = new BufferedOutputStream(new FileOutputStream(dir + "/" + nextString() + "封面.pdf"));
        int n;
        byte[] b = new byte[1024];
        InputStream input = new BufferedInputStream(new FileInputStream(test));

        while ((n = input.read(b)) != -1)
            out.write(b, 0, n);

        out.close();
        input.close();

        input = new BufferedInputStream(new FileInputStream(test));

        out = new BufferedOutputStream(new FileOutputStream(dir + "/" + nextString() + ".pdf"));
        while ((n = input.read(b)) != -1) {
            out.write(b, 0, n);
        }
        out.close();
        input.close();
    }

}
