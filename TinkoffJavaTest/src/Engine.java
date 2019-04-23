import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class Engine {
    private static List<File> files = new ArrayList<>();
    private static StringJoiner strJoiner;
    private static final int GB = (int) Math.pow(1024, 3);
    private static final int MB = (int) Math.pow(1024, 2);
    private static final int KB = (int) Math.pow(1024, 1);

    private static long heapSize = Runtime.getRuntime().totalMemory();
    private static long heapMaxSize = Runtime.getRuntime().maxMemory();
    private static long heapFreeSize = Runtime.getRuntime().freeMemory();

    public static void main(String[] args) throws IOException {

        System.out.println("heapSize: " + heapSize/MB + "\n" +
                            "heapMaxSize: " + heapMaxSize/MB + "\n" +
                            "heapFreeSize: " + heapFreeSize/MB + "\n");

        while (files.size() < 5){
            files.add(new File(files.size() + ".txt"));
        }

        for (File file : files) {
            generateFile(file);
        }

        for (File file : files) {
            CSVScan(file,1902407800);
        }
    }

    public static void generateFile(File file) throws IOException {
        int count = 0;
            if (!file.exists()) {
                System.out.println("Created at " + file.getPath());
                file.createNewFile();
            }else{
                return;
            }

        try(FileWriter fw = new FileWriter(file, true)){

        Random rnd = new Random();
        strJoiner = new StringJoiner(",");
        int strJoinerCache = 0;
        long time = System.currentTimeMillis();
        // Генерацию чисел можно было бы сделать одной строчкой,если увеличить размер кучи (-Xmx),
        // но это плохой ход, правильнее в данном случае управлять буффером записи
        //rnd.ints().limit(?).forEach((x) -> strJoiner.add(Integer.toString(x)));

            while (strJoiner.length() + strJoinerCache < GB) {
                strJoiner.add(Integer.toString(rnd.nextInt()));
                count++;
                if (strJoiner.length() > KB){
                    //System.out.println("heapSize: " + Runtime.getRuntime().totalMemory()/MB);
                    //System.out.println("D " + strJoiner.length() + " : C " + strJoinerCache);
                    strJoinerCache += strJoiner.length();
                    strJoiner.add("\n");
                    fw.write(strJoiner.toString());

                    strJoiner = null;
                    strJoiner = new StringJoiner(",");
                    //System.out.println("heapSize: " + Runtime.getRuntime().totalMemory()/MB);
                    //System.out.println("D " + strJoiner.length() + " : C " + strJoinerCache);
                }
            }
            fw.write(strJoiner.toString());
            time = System.currentTimeMillis() - time;
            System.out.println(time/1000f + " seconds");
            System.out.println( "length: " + file.length() + "\ncount: " + count);
        }catch(OutOfMemoryError e)
        {
            e.getStackTrace();
            System.out.println(strJoiner.length()/MB + " ? " + file.getUsableSpace()/MB);
            System.out.println("heapSize: " + Runtime.getRuntime().totalMemory()/MB + "\n" + "heapMaxSize: " + heapMaxSize/MB);
        }

        }

        public static void CSVScan(File file, int number) {
        List<String> filenames = new ArrayList<>();
            try (BufferedReader in = new BufferedReader(new FileReader(file)))
            {
//               in.lines().forEach(s -> {
//                   if (s.contains("964589")){
//                       filenames.add(file.getName());
//                   }
//               });
                boolean found = false;
                long time = System.currentTimeMillis();
                while (in.ready() && !found){

                    Scanner scan = new Scanner(in.readLine()).useDelimiter(",");
                    while (scan.hasNext() && !found){
                      //if (scan.nextInt() == number){
                        if (scan.next().equals(Integer.toString(number))){
                            filenames.add(file.getName());
                            System.out.println("found ");
                            found = true;
                        }
                    }
                }
                time = System.currentTimeMillis() - time;
                System.out.println(time/1000f + " seconds");

                System.out.println(filenames.get(0));
            }
            catch (IOException | NumberFormatException e)
            {
                e.printStackTrace();
            }
        }
}
