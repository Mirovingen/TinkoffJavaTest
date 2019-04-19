import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;
import java.util.StringJoiner;

public class Engine {
    private static File file;
    private static FileWriter fw;
    private static StringJoiner strJoiner;
    private static final int GB = (int) Math.pow(1024, 3);
    private static final int MB = (int) Math.pow(1024, 2);

    private static long heapSize = Runtime.getRuntime().totalMemory();
    private static long heapMaxSize = Runtime.getRuntime().maxMemory();
    private static long heapFreeSize = Runtime.getRuntime().freeMemory();

    public static void main(String[] args) throws IOException {

        System.out.println("heapSize: " + heapSize/MB + "\n" +
                            "heapMaxSize: " + heapMaxSize/MB + "\n" +
                            "heapFreeSize: " + heapFreeSize/MB + "\n");

       // strJoiner = new StringJoiner(",");
       // int strJoinerCache = 0;
//        while (strJoiner.length() + strJoinerCache < GB) {
//            //rnd.ints().limit(1).forEach((x) -> {System.out.println(strJoiner.length() +  " : " + x); strJoiner.add(Integer.toString(x));});
//            strJoiner.add(Integer.toString(rnd.nextInt()));
//            if (Runtime.getRuntime().totalMemory() > heapMaxSize - GB ) {
//                fw.write(strJoiner.toString());
//                fw.flush();
//                strJoinerCache += strJoiner.length();
//                strJoiner = null;
//                strJoiner = new StringJoiner(",");
//            }
//        }
        generateFiles();
        //FileChannelWrite(file, strJoiner);
    }

    public static void generateFiles() throws IOException {
        file = new File("1.txt");

            if (!file.exists()) {
                System.out.println("Created at " + file.getPath());
                file.createNewFile();
            }


        fw = new FileWriter(file, true);


        Random rnd = new Random();
        strJoiner = new StringJoiner(",");
        int strJoinerCache = 0;
        long time = System.currentTimeMillis();

        try{
            while (strJoiner.length() + strJoinerCache < GB) {
                strJoiner.add(Integer.toString(rnd.nextInt()));
                if (strJoiner.length() > MB){
                    System.out.println("heapSize: " + Runtime.getRuntime().totalMemory()/MB);
                    System.out.println("D " + strJoiner.length() + " : C " + strJoinerCache);
                    strJoinerCache += strJoiner.length();
                    fw.write(strJoiner.toString());

                    strJoiner = null;
                    strJoiner = new StringJoiner(",");
                    System.out.println("heapSize: " + Runtime.getRuntime().totalMemory()/MB);
                    System.out.println("D " + strJoiner.length() + " : C " + strJoinerCache);
                }
            }
            time = System.currentTimeMillis() - time;
        }catch(OutOfMemoryError e)
        {
            e.getStackTrace();
            System.out.println(strJoiner.length()/MB + " ? " + file.getUsableSpace()/MB);
            System.out.println("heapSize: " + Runtime.getRuntime().totalMemory()/MB + "\n" + "heapMaxSize: " + heapMaxSize/MB);
        }
        //BufferedWriter bufferedWriter = new BufferedWriter(fw, strJoiner.length());
        fw.write(strJoiner.toString());

        fw.close();


        System.out.println(time/1000f + " seconds");
        System.out.println( "length: " + file.length() + "\nSlength: " + strJoiner.length());

        }

        public static void FileChannelWrite(File file, StringJoiner strJoiner) throws IOException {
            FileChannel rwChannel = new RandomAccessFile(file, "rw").getChannel();
            byte[] strbyte = strJoiner.toString().getBytes();
            ByteBuffer bf = ByteBuffer.allocate(strbyte.length);
            System.out.println(strbyte.length + " bytes allocated");

            rwChannel.close();
            bf = null;
        }
}
