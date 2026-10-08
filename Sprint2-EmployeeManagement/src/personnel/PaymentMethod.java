package personnel;

/**
 * Hợp đồng chung cho các hình thức trả lương.
 * Mỗi đối tượng triển khai giữ nhân viên cần trả lương.
 */
public interface PaymentMethod {

    /** Chỉ in kết quả mô phỏng ra console, không chuyển tiền thật. */
    void pay();
}
