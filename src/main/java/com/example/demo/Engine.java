import com.example.demo.DemoApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;
@SpringBootApplication
public class Engine {
    private static List<File> files = new ArrayList<>();

    private static StringJoiner strJoiner;
    private static final int GB = (int) Math.pow(1024, 3);
    private static final int MB = (int) Math.pow(1024, 2);
    private static final int KB = (int) Math.pow(1024, 1);

    private static String CODE = "01.Result.NotFound";
    private static List<String> filenames = new ArrayList<>();
    private static String error;

    private static long heapSize = Runtime.getRuntime().totalMemory();
    private static long heapMaxSize = Runtime.getRuntime().maxMemory();
    private static long heapFreeSize = Runtime.getRuntime().freeMemory();

    public static void main(String[] args) throws IOException {

        System.out.println("heapSize: " + heapSize / MB + "\n" +
                "heapMaxSize: " + heapMaxSize / MB + "\n" +
                "heapFreeSize: " + heapFreeSize / MB + "\n");

        while (files.size() < 5) {
            files.add(new File(files.size() + ".txt"));
        }

        for (File file : files) {
            generateFile(file);
        }

        for (File file : files) {
            CSVScan(file, 1902407800);
        }

        SpringApplication.run(DemoApplication.class, args);

        Insert(CODE,number,filenames.toString(),error);
    }

    public static void generateFile(File file) throws IOException {
        int count = 0;
        if (!file.exists()) {
            System.out.println("Created at " + file.getPath());
            file.createNewFile();
        } else {
            return;
        }

        try (FileWriter fw = new FileWriter(file, true)) {

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
                if (strJoiner.length() > KB) {
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
            System.out.println(time / 1000f + " seconds");
            System.out.println("length: " + file.length() + "\ncount: " + count);
        } catch (OutOfMemoryError e) {
            e.getStackTrace();
            error += e.getMessage();
            System.out.println(strJoiner.length() / MB + " ? " + file.getUsableSpace() / MB);
            System.out.println("heapSize: " + Runtime.getRuntime().totalMemory() / MB + "\n" + "heapMaxSize: " + heapMaxSize / MB);
        }

    }

    public static void CSVScan(File file, int number) {

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
//               in.lines().forEach(s -> {
//                   if (s.contains("964589")){
//                       filenames.add(file.getName());
//                   }
//               });
            boolean found = false;
            long time = System.currentTimeMillis();
            while (in.ready() && !found) {

                Scanner scan = new Scanner(in.readLine()).useDelimiter(",");
                while (scan.hasNext() && !found) {
                    //if (scan.nextInt() == number){
                    if (scan.next().equals(Integer.toString(number))) {
                        filenames.add(file.getName());
                        System.out.println("found ");
                        found = true;
                        CODE = "00.Result.OK";
                    }
                }
            }
            time = System.currentTimeMillis() - time;
            System.out.println(time / 1000f + " seconds");



            System.out.println(filenames.get(0));
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            error += e.getMessage();
        }




    }

    public static void Insert(String CODE, int number, String filenames, String error) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "120340")) {

            System.out.println("Connected to PostgreSQL database!");

            String sqlQuery = "INSERT INTO public.\"Smart\" (CODE, number, filenames, error) VALUES (?,?,?,?)";

            PreparedStatement preparedInsert = connection.prepareStatement(sqlQuery);
            preparedInsert.setString(1,CODE);
            preparedInsert.setInt(2,number);
            preparedInsert.setString(3,filenames);
            preparedInsert.setString(4,error);

        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }

}