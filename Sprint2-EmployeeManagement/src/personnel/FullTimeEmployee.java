package personnel;

/** Nhân viên toàn thời gian: lương = lương cơ bản + phụ cấp. */
public class FullTimeEmployee extends Employee {

    private long baseSalary;
    private long allowance;

    public FullTimeEmployee(String id, String fullName, String department,
                            long baseSalary, long allowance) {
        // super(...) gọi constructor của Employee để khởi tạo dữ liệu chung.
        super(id, fullName, department);
        setBaseSalary(baseSalary);
        setAllowance(allowance);
    }

    public long getBaseSalary() {
        return baseSalary;
    }

    public final void setBaseSalary(long baseSalary) {
        validateMoney(baseSalary, "Lương cơ bản");
        this.baseSalary = baseSalary;
    }

    public long getAllowance() {
        return allowance;
    }

    public final void setAllowance(long allowance) {
        validateMoney(allowance, "Phụ cấp");
        this.allowance = allowance;
    }

    // OVERRIDING: triển khai phương thức được khai báo trong lớp cha.
    @Override
    public long calculateSalary() {
        return baseSalary + allowance;
    }

    @Override
    public String getEmployeeType() {
        return "Toàn thời gian";
    }

    @Override
    public String getSalaryDetails() {
        return "Lương cơ bản " + MoneyFormatter.format(baseSalary)
                + " + phụ cấp " + MoneyFormatter.format(allowance);
    }
}
