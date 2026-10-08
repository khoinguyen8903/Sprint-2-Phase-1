package personnel;

/** Trả lương bằng tiền mặt (mô phỏng). */
public class CashPayment implements PaymentMethod {

    private final Employee employee;

    public CashPayment(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Nhân viên không được null.");
        }
        this.employee = employee;
    }

    @Override
    public void pay() {
        System.out.println("[MÔ PHỎNG] Trả tiền mặt: " + employee.getId()
                + " - " + employee.getFullName() + " | "
                + MoneyFormatter.format(employee.calculateSalary()));
    }
}
