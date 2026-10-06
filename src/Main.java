import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        try (Scanner scanner = new Scanner(System.in)) {
            MD5 md5 = new MD5();

            System.out.println("Choose mode: (1 - hash text; 2 - hash file)");
            String mode = scanner.nextLine().trim();

            byte[] data;

            switch (mode) {
                case "1" -> {
                    System.out.print("Enter text: ");
                    String input = scanner.nextLine();
                    data = input.getBytes(StandardCharsets.UTF_8);
                }
                case "2" -> {
                    System.out.print("Enter file path: ");
                    String path = scanner.nextLine().trim();
                    data = Files.readAllBytes(Path.of(path));
                }
                default -> {
                    throw new IllegalArgumentException("Incorrect mode.");
                }
            }

            String result = md5.run(data);
            System.out.println("Result: " + result);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}