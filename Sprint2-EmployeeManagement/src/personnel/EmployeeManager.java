package personnel;

import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý danh sách bằng ArrayList và vòng lặp thông thường.
 * Lớp này không đọc Scanner hoặc hiển thị menu.
 */
public class EmployeeManager {

    // final giữ nguyên biến tham chiếu; vẫn thêm/xóa phần tử được.
    private final List<Employee> employees;

    public EmployeeManager() {
        employees = new ArrayList<>();
    }

    public boolean addEmployee(Employee employee) {
        if (employee == null || findById(employee.getId()) != null) {
            return false;
        }

        employees.add(employee);
        return true;
    }

    /** Không phân biệt chữ hoa/thường khi tìm mã, ví dụ NV001 và nv001. */
    public Employee findById(String id) {
        if (id == null) {
            return null;
        }

        for (Employee employee : employees) {
            if (employee.getId().equalsIgnoreCase(id.trim())) {
                return employee;
            }
        }
        return null;
    }

    /**
     * Thay nhân viên có cùng mã bằng dữ liệu mới.
     * Cách này cho phép đổi cả loại nhân viên mà không cần ép kiểu.
     */
    public boolean updateEmployee(Employee updatedEmployee) {
        if (updatedEmployee == null) {
            return false;
        }

        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId().equalsIgnoreCase(updatedEmployee.getId())) {
                employees.set(i, updatedEmployee);
                return true;
            }
        }
        return false;
    }

    public boolean removeEmployee(String id) {
        Employee employee = findById(id);
        if (employee == null) {
            return false;
        }

        return employees.remove(employee);
    }

    /** Tìm theo một phần tên; không phân biệt hoa/thường, vẫn phân biệt dấu. */
    public List<Employee> findByName(String keyword) {
        List<Employee> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return result;
        }

        String searchText = keyword.trim().toLowerCase(java.util.Locale.ROOT);
        for (Employee employee : employees) {
            String fullName = employee.getFullName().toLowerCase(java.util.Locale.ROOT);
            if (fullName.contains(searchText)) {
                result.add(employee);
            }
        }
        return result;
    }

    /**
     * Trả về bản sao danh sách để bên ngoài không trực tiếp thêm/xóa vào danh sách gốc.
     * Các đối tượng Employee bên trong vẫn được dùng chung (bản sao nông).
     */
    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    public boolean isEmpty() {
        return employees.isEmpty();
    }

    public int getEmployeeCount() {
        return employees.size();
    }

    public long calculateTotalSalary() {
        long total = 0;

        for (Employee employee : employees) {
            // Đa hình: không kiểm tra đây là FullTimeEmployee hay PartTimeEmployee.
            total += employee.calculateSalary();
        }
        return total;
    }
}
