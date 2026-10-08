package personnel;

import java.util.ArrayList;
import java.util.List;

/** Điều phối menu, nhận dữ liệu và gọi các lớp xử lý. */
public class ConsoleApplication {

    private final EmployeeManager manager;
    private final InputReader input;

    public ConsoleApplication(EmployeeManager manager, InputReader input) {
        this.manager = manager;
        this.input = input;
    }

    public void run() {
        System.out.println("ỨNG DỤNG QUẢN LÝ NHÂN SỰ - SPRINT 2");
        System.out.println("Dữ liệu chỉ lưu trong phiên chạy. Thanh toán là mô phỏng.");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = input.readIntInRange("Chọn chức năng: ", 0, 9);

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    displayEmployees(manager.getEmployees());
                    break;
                case 3:
                    findEmployeeById();
                    break;
                case 4:
                    findEmployeesByName();
                    break;
                case 5:
                    updateEmployee();
                    break;
                case 6:
                    removeEmployee();
                    break;
                case 7:
                    showPayroll();
                    break;
                case 8:
                    payAllEmployees();
                    break;
                case 9:
                    demonstrateOverloading();
                    break;
                case 0:
                    running = false;
                    System.out.println("Đã thoát. Dữ liệu phiên chạy không được lưu lại.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n---------------- MENU ----------------");
        System.out.println("1. Thêm nhân viên");
        System.out.println("2. Xem danh sách nhân viên");
        System.out.println("3. Tìm nhân viên theo mã");
        System.out.println("4. Tìm nhân viên theo tên");
        System.out.println("5. Cập nhật nhân viên");
        System.out.println("6. Xóa nhân viên");
        System.out.println("7. Xem bảng lương");
        System.out.println("8. Trả lương toàn bộ nhân viên (mô phỏng)");
        System.out.println("9. Tính thử lương kèm thưởng (overloading)");
        System.out.println("0. Thoát");
    }

    private void addEmployee() {
        System.out.println("\n--- THÊM NHÂN VIÊN ---");
        String id = input.readNonEmptyString("Mã nhân viên (ví dụ NV001): ");
        if (manager.findById(id) != null) {
            System.out.println("Mã nhân viên đã tồn tại. Hãy dùng chức năng cập nhật.");
            return;
        }

        Employee employee = readEmployeeData(id);
        if (manager.addEmployee(employee)) {
            System.out.println("Đã thêm nhân viên " + employee.getFullName() + ".");
        } else {
            System.out.println("Không thêm được nhân viên.");
        }
    }

    /** Dùng lại cho cả thêm mới và cập nhật để không lặp phần nhập thông tin. */
    private Employee readEmployeeData(String id) {
        String fullName = input.readNonEmptyString("Họ tên: ");
        String department = input.readNonEmptyString("Phòng ban: ");
        System.out.println("1. Toàn thời gian");
        System.out.println("2. Bán thời gian");
        int type = input.readIntInRange("Loại nhân viên: ", 1, 2);

        if (type == 1) {
            long baseSalary = input.readLongInRange(
                    "Lương cơ bản (VND): ", 0, Employee.MAX_MONEY_INPUT);
            long allowance = input.readLongInRange(
                    "Phụ cấp (VND): ", 0, Employee.MAX_MONEY_INPUT);
            return new FullTimeEmployee(id, fullName, department, baseSalary, allowance);
        }

        long hourlyRate = input.readLongInRange(
                "Tiền mỗi giờ (VND): ", 0, Employee.MAX_MONEY_INPUT);
        int workingHours = input.readIntInRange(
                "Số giờ làm trong tháng (giờ nguyên): ", 0, PartTimeEmployee.MAX_WORKING_HOURS);
        return new PartTimeEmployee(id, fullName, department, hourlyRate, workingHours);
    }

    private void displayEmployees(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("Không có nhân viên để hiển thị.");
            return;
        }

        System.out.println("\n--- DANH SÁCH NHÂN VIÊN ---");
        for (Employee employee : employees) {
            employee.displayDetails();
            System.out.println("--------------------------------------");
        }
        System.out.println("Số nhân viên hiển thị: " + employees.size());
    }

    private void findEmployeeById() {
        String id = input.readNonEmptyString("Mã nhân viên cần tìm: ");
        Employee employee = manager.findById(id);
        if (employee == null) {
            System.out.println("Không tìm thấy nhân viên có mã " + id + ".");
            return;
        }
        employee.displayDetails();
    }

    private void findEmployeesByName() {
        String keyword = input.readNonEmptyString("Nhập tên hoặc một phần tên: ");
        displayEmployees(manager.findByName(keyword));
    }

    private void updateEmployee() {
        String id = input.readNonEmptyString("Mã nhân viên cần cập nhật: ");
        Employee currentEmployee = manager.findById(id);
        if (currentEmployee == null) {
            System.out.println("Không tìm thấy nhân viên.");
            return;
        }

        System.out.println("Thông tin hiện tại:");
        currentEmployee.displayDetails();
        System.out.println("Nhập lại toàn bộ thông tin. Giữ mã cũ; có thể đổi loại nhân viên.");

        // Chỉ thay đối tượng cũ sau khi đã nhập xong và xác nhận.
        Employee updatedEmployee = readEmployeeData(currentEmployee.getId());
        System.out.println("Thông tin mới:");
        updatedEmployee.displayDetails();
        if (!input.confirm("Lưu thay đổi")) {
            System.out.println("Đã hủy cập nhật.");
            return;
        }

        if (manager.updateEmployee(updatedEmployee)) {
            System.out.println("Đã cập nhật nhân viên.");
        } else {
            System.out.println("Không cập nhật được nhân viên.");
        }
    }

    private void removeEmployee() {
        String id = input.readNonEmptyString("Mã nhân viên cần xóa: ");
        Employee employee = manager.findById(id);
        if (employee == null) {
            System.out.println("Không tìm thấy nhân viên.");
            return;
        }

        employee.displayDetails();
        if (!input.confirm("Xóa nhân viên này")) {
            System.out.println("Đã hủy xóa.");
            return;
        }

        if (manager.removeEmployee(id)) {
            System.out.println("Đã xóa nhân viên.");
        } else {
            System.out.println("Không xóa được nhân viên.");
        }
    }

    private void showPayroll() {
        if (manager.isEmpty()) {
            System.out.println("Danh sách trống. Hãy thêm nhân viên trước.");
            return;
        }

        System.out.println("\n--- BẢNG LƯƠNG HIỆN TẠI ---");
        for (Employee employee : manager.getEmployees()) {
            System.out.println(employee.getId() + " | " + employee.getFullName()
                    + " | " + employee.getEmployeeType()
                    + " | " + MoneyFormatter.format(employee.calculateSalary()));
        }
        System.out.println("Số nhân viên: " + manager.getEmployeeCount());
        System.out.println("TỔNG LƯƠNG: " + MoneyFormatter.format(manager.calculateTotalSalary()));
        System.out.println("Chưa tính thưởng thử ở menu 9, thuế hoặc bảo hiểm.");
    }

    private void payAllEmployees() {
        if (manager.isEmpty()) {
            System.out.println("Danh sách trống. Hãy thêm nhân viên trước.");
            return;
        }

        // Cùng một danh sách chứa cả CashPayment lẫn BankTransferPayment.
        List<PaymentMethod> payments = new ArrayList<>();

        for (Employee employee : manager.getEmployees()) {
            System.out.println("\nNhân viên: " + employee.getId() + " - "
                    + employee.getFullName() + " | "
                    + MoneyFormatter.format(employee.calculateSalary()));
            payments.add(readPaymentMethod(employee));
        }

        // YÊU CẦU NGHIỆM THU: gọi pay() trên từng phần tử của List<PaymentMethod>.
        // Không dùng if-else, switch hoặc instanceof để chọn cách thanh toán ở đây.
        for (PaymentMethod payment : payments) {
            payment.pay();
        }

        System.out.println("Hoàn tất mô phỏng trả lương cho " + payments.size() + " nhân viên.");
    }

    private PaymentMethod readPaymentMethod(Employee employee) {
        System.out.println("1. Tiền mặt");
        System.out.println("2. Chuyển khoản ngân hàng");
        int choice = input.readIntInRange("Hình thức trả lương: ", 1, 2);

        // if chỉ phục vụ việc tạo đối tượng theo lựa chọn trên menu.
        if (choice == 1) {
            return new CashPayment(employee);
        }

        return new BankTransferPayment(employee);
    }

    private void demonstrateOverloading() {
        String id = input.readNonEmptyString("Mã nhân viên cần tính lương kèm thưởng: ");
        Employee employee = manager.findById(id);
        if (employee == null) {
            System.out.println("Không tìm thấy nhân viên.");
            return;
        }

        long bonus = input.readLongInRange("Tiền thưởng thử (VND): ", 0, Employee.MAX_MONEY_INPUT);

        // Hai lời gọi cùng tên, khác danh sách tham số: ví dụ overloading.
        System.out.println("calculateSalary()      = "
                + MoneyFormatter.format(employee.calculateSalary()));
        System.out.println("calculateSalary(bonus) = "
                + MoneyFormatter.format(employee.calculateSalary(bonus)));
        System.out.println("Đây là tính thử. Không lưu thưởng hoặc thay đổi bảng lương.");
    }
}
