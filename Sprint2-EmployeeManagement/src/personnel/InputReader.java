package personnel;

import java.util.Scanner;

public class InputReader {

    private final Scanner scanner;

    public InputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Không được để trống. Vui lòng nhập lại.");
        }
    }

    public long readLongInRange(String prompt, long min, long max) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine().trim();

            try {
                long value = Long.parseLong(text);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Vui lòng nhập từ " + min + " đến " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Hãy nhập số nguyên, không có dấu chấm/phẩy; ví dụ 5000000.");
            }
        }
    }

    public int readIntInRange(String prompt, int min, int max) {
        // Giá trị đã được giới hạn trong khoảng int nên phép ép kiểu này an toàn.
        return (int) readLongInRange(prompt, min, max);
    }

    public boolean confirm(String prompt) {
        while (true) {
            String answer = readNonEmptyString(prompt + " (y/n): ");
            if (answer.equalsIgnoreCase("y")) {
                return true;
            }
            if (answer.equalsIgnoreCase("n")) {
                return false;
            }
            System.out.println("Nhập y để đồng ý hoặc n để hủy.");
        }
    }
}
