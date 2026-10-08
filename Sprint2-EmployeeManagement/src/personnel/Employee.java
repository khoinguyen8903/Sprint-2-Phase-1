package personnel;

/**
 * Lớp cha chứa dữ liệu chung của mọi nhân viên.
 *
 * Đây là abstract class: không tạo trực tiếp new Employee(...).
 * Mỗi loại nhân viên phải tự định nghĩa cách tính lương của mình.
 */
public abstract class Employee {

    // Giới hạn nhập liệu của bài tập, áp dụng cho từng khoản tiền đầu vào.
    public static final long MAX_MONEY_INPUT = 1_000_000_000L;

    // final: mã nhân viên được gán trong constructor và không đổi sau đó.
    private final String id;
    private String fullName;
    private String department;

    protected Employee(String id, String fullName, String department) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên không được để trống.");
        }

        this.id = id.trim();
        setFullName(fullName);
        setDepartment(department);
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    // final: lớp con không ghi đè setter, giữ nguyên quy tắc kiểm tra dữ liệu.
    public final void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống.");
        }
        this.fullName = fullName.trim();
    }

    public String getDepartment() {
        return department;
    }

    public final void setDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Phòng ban không được để trống.");
        }
        this.department = department.trim();
    }

    /** Mỗi lớp con cung cấp một công thức tính lương khác nhau. */
    public abstract long calculateSalary();

    public abstract String getEmployeeType();

    /** Trả về phần thông tin chỉ có ở từng loại nhân viên. */
    public abstract String getSalaryDetails();

    /**
     * OVERLOADING: cùng tên calculateSalary nhưng khác danh sách tham số.
     * Khoản thưởng chỉ dùng cho lần tính này, không sửa lương trong nhân viên.
     */
    public long calculateSalary(long bonus) {
        validateMoney(bonus, "Tiền thưởng");

        // calculateSalary() sẽ chạy phiên bản của lớp con tại runtime.
        return calculateSalary() + bonus;
    }

    /** protected cho phép các lớp con dùng lại phần kiểm tra này. */
    protected static void validateMoney(long amount, String fieldName) {
        if (amount < 0 || amount > MAX_MONEY_INPUT) {
            throw new IllegalArgumentException(
                    fieldName + " phải từ 0 đến "
                            + MoneyFormatter.format(MAX_MONEY_INPUT) + ".");
        }
    }

    /** Phương thức chung, dùng được cho cả hai loại nhân viên. */
    public void displayDetails() {
        System.out.println("Mã nhân viên : " + id);
        System.out.println("Họ tên       : " + fullName);
        System.out.println("Phòng ban    : " + department);
        System.out.println("Loại         : " + getEmployeeType());
        System.out.println("Cách tính    : " + getSalaryDetails());
        System.out.println("Lương        : " + MoneyFormatter.format(calculateSalary()));
    }
}
