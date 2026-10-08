package personnel;

/** Nhân viên bán thời gian: lương = tiền mỗi giờ x số giờ làm. */
public class PartTimeEmployee extends Employee {

    // Giới hạn kỹ thuật cho bài tập theo tháng, không phải quy định lao động.
    public static final int MAX_WORKING_HOURS = 31 * 24;

    private long hourlyRate;
    private int workingHours;

    public PartTimeEmployee(String id, String fullName, String department,
                            long hourlyRate, int workingHours) {
        super(id, fullName, department);
        setHourlyRate(hourlyRate);
        setWorkingHours(workingHours);
    }

    public long getHourlyRate() {
        return hourlyRate;
    }

    public final void setHourlyRate(long hourlyRate) {
        validateMoney(hourlyRate, "Tiền mỗi giờ");
        this.hourlyRate = hourlyRate;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public final void setWorkingHours(int workingHours) {
        if (workingHours < 0 || workingHours > MAX_WORKING_HOURS) {
            throw new IllegalArgumentException(
                    "Số giờ làm phải từ 0 đến " + MAX_WORKING_HOURS + ".");
        }
        this.workingHours = workingHours;
    }

    @Override
    public long calculateSalary() {
        return hourlyRate * workingHours;
    }

    @Override
    public String getEmployeeType() {
        return "Bán thời gian";
    }

    @Override
    public String getSalaryDetails() {
        return MoneyFormatter.format(hourlyRate) + "/giờ x " + workingHours + " giờ";
    }
}
