package personnel;

/** Trả lương bằng chuyển khoản (chỉ in thông báo để minh họa interface). */
public class BankTransferPayment implements PaymentMethod {

    private final Employee employee;

    public BankTransferPayment(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Nhân viên không được null.");
        }
        this.employee = employee;
    }

    @Override
    public void pay() {
        System.out.println("[MÔ PHỎNG] Chuyển khoản: " + employee.getId()
                + " - " + employee.getFullName() + " | "
                + MoneyFormatter.format(employee.calculateSalary()));
    }
}
