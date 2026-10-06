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



            MD5 md5 = new MD5(input);
            md5.run();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}