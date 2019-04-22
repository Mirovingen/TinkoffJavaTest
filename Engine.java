import java.io.*;
import java.util.*;

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

//        while (files.size() < 1){
//            files.add(new File(files.size() + ".txt"));
//        }
//
//        for (File file : files) {
//            generateFile(file);
//        }
//
//        for (File file : files) {
//            CSVScan(file,1928243054);
//        }

        String s = "-986180055,1928243054,298291083,-1584254099,1727598799,-367616019,1191124973,1839359732,-585480632,1320614272,-978965986,54943187,-1529652847,887874012,1854269298,-567180591,585079343,-2046859715,-1136744545,1322978827,-1242932108,109584469,1990896802,458521861,-332262912,-963559807,322461923,-1283000319,740681354,71195453,-1624290409,-397330284,1908008160,-107948070,1646618469,-1702574731,1399449644,102477422,1394655051,660248471,-2103190152,-1328681205,1278965187,-1864074118,1607866739,-1681171406,1298045473,864143806,-85539211,-405939575,1762291774,-894224903,832941760,-721311796,1550388826,823323337,1327097608,133795073,-1336773314,-1779227604,480320820,-1988657169,1370982049,1484189966,1223593830,-493262896,928742860,-1457657565,760475448,331413829,790220416,-386601993,-1616988855,1049157001,-1200583145,908803394,-1166497545,-779577111,-1293563160,32281216,1877123181,-1429478285,257979888,274064833,-1387479841,-1023840531,-101465427,442769853,-1220565228,-2106574584,-1876549299,1637163,-809594844,1382154453,";
        System.out.println(s.);

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
        // но это плохой ход,правильнее в данном случае управлять буффером записи
        //rnd.ints().limit(1).forEach((x) -> strJoiner.add(Integer.toString(x)));

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

                //while (in.ready()){
                  // if (in.readLine().contains("," + Integer.toString(number) + ",") ) {
                    System.out.println(in.readLine());
                System.out.println( in.readLine().matches("\\b1928243054\\b"));
//                    if (in.readLine().matches("\\b1902407800\\b")) {
//                       filenames.add(file.getName());
                       //break;
                 //  }
                //}

                //System.out.println(filenames.get(0));
            }
            catch (IOException | NumberFormatException e)
            {
                e.printStackTrace();
            }
        }
}
