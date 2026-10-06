import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter file path or text:");
        String input = scanner.nextLine();

        try{
            //try to open file and hash it
            //if throws "no such file"-ish error, hash text
            //if throws some other error - abort execution

            File file = new File(input);

            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                String data = reader.nextLine();
                System.out.println(data);
            }

            MD5 md5 = new MD5(input);
            md5.run();

        }catch (FileNotFoundException e){
            System.out.println("File not found...");
            MD5 md5 = new MD5(input);
            md5.run();
        }
    }
}