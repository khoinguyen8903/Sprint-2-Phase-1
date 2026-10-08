package personnel;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            EmployeeManager manager = new EmployeeManager();
            InputReader input = new InputReader(scanner);
            ConsoleApplication application = new ConsoleApplication(manager, input);
            application.run();
        } catch (NoSuchElementException e) {
            System.out.println("\nĐầu vào đã kết thúc. Đóng chương trình.");
        } finally {
            scanner.close();
        }
    }
}
