import java.util.LinkedList;
import java.util.List;

class Main
{
    static String read(String file) {
        try {
            java.io.FileInputStream in = new java.io.FileInputStream(file);
            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            while (true) {
                int read = in.read(buffer);
                if (read == -1) {
                    break;
                }
                out.write(buffer, 0, read);
            }
            return new String(out.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    static long lastMod(String file) {
        try {
            return new java.io.File(file).lastModified();
        } catch (Exception e) {
            return 0;
        }
    }

    public static void main(String[] args) {
        Scope runtime = new Scope(null);
        runtime.define("print", new Function() {
            Object call(Object[] arguments) {
                System.out.println(arguments[0]);
                return null;
            }
        });
        runtime.define("sleep", new Function() {
            Object call(Object[] arguments) {
                try {
                    Thread.sleep((int) (float) arguments[0]);
                } catch (InterruptedException e) {
                }
                return null;
            }
        });
        runtime.define("count", new Function() {
            Object call(Object[] arguments) {
                return (float) ((List) arguments[0]).size();
            }
        });
        Common.define(runtime);
        long lastMod = 0;
        while (true) {
            long newLastMod = lastMod("test.js");
            if (lastMod != newLastMod) {
                lastMod = newLastMod;
                Statement statement = Parsing.parse(read("test.js"));
                long start = System.currentTimeMillis();
                statement.execute(new Scope(runtime));
                System.out.println("Executed in " + (System.currentTimeMillis() - start) + "ms");
            }
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
        }
    }
}