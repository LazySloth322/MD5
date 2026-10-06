import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter file path or text:");
        String input = scanner.nextLine();

        MD5 md5 = new MD5();

        try{
            //try to open file
            //if throws "no such file"-ish error, hash text
            //if throws some other error - abort execution

            input = Files.readString(
                    Path.of(input),
                    StandardCharsets.UTF_8
            );

        }catch (NoSuchFileException e){
            System.out.println("File not found. Hashing input string...");
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        System.out.print(input+"\n");
        md5.run(input);
    }
}