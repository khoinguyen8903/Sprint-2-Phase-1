# Employee Management - Core Java 

Dự án này là phiên bản refactor của hệ thống quản lý nhân sự trên Console. Mục tiêu cốt lõi không nằm ở việc xây dựng UI phức tạp, mà là tạo ra một playground để hiện thực hóa và kiểm chứng các đặc tính của OOP, nguyên lý thiết kế SOLID, và cơ chế thực thi bên dưới của JVM (Under the hood).

## 1. Architecture Overview (Tổng quan kiến trúc)

Hệ thống được thiết kế theo hướng phân tách trách nhiệm (Separation of Concerns), giới hạn sự phụ thuộc giữa các tầng:

- **Data Models:** Chứa cấu trúc dữ liệu và validation nội bộ. Gốc là abstract class `Employee` chứa các state dùng chung (ID, Name, Department), được kế thừa bởi `FullTimeEmployee` và `PartTimeEmployee` với các state đặc thù (BaseSalary vs HourlyRate).
- **Business Logic:** `EmployeeManager` đóng vai trò là single source of truth cho danh sách nhân viên, xử lý nghiệp vụ tìm kiếm và tổng hợp lương.
- **Presentation & Coordination:** `ConsoleApplication` và `InputReader` lo việc giao tiếp với I/O và điều phối luồng khởi tạo đối tượng.

## 2. Core Design Decisions (Quyết định thiết kế)

### Đa hình (Polymorphism) & Open/Closed Principle (OCP)
Thay vì dùng `instanceof` hay switch-case để kiểm tra loại nhân viên, logic tính lương được abstract hóa thông qua hàm `calculateSalary()`. 
Tại Runtime, vòng lặp trong `EmployeeManager` sử dụng **Dynamic Dispatch** (thông qua V-Table tra cứu từ Object Header) để gọi chính xác công thức của lớp con. Thiết kế này tuân thủ tuyệt đối OCP ở tầng Business Logic: Nếu sau này dự án scale thêm loại `InternEmployee`, luồng tính tổng lương hoàn toàn không cần sửa đổi.

### Tách biệt hành vi bằng Interface
Nghiệp vụ thanh toán lương không thuộc về bản chất kế thừa (Inheritance tree) của một nhân viên. Do đó, logic thanh toán được bóc tách hoàn toàn vào interface `PaymentMethod` (với các implementation như `CashPayment`, `BankTransferPayment`). Thiết kế này cho phép thay đổi chiến lược thanh toán độc lập (tương tự Strategy Pattern) mà không làm phình to class `Employee`.

### Overloading & Tránh thay đổi State ngoài ý muốn
Tồn tại hai phiên bản `calculateSalary()`: một mặc định và một nhận tham số `long bonus`. Phiên bản có bonus chỉ trả về kết quả tính toán thuần túy thay vì ghi đè lại field state của đối tượng, đảm bảo tính toàn vẹn của quỹ lương gốc.

## 3. JVM & Memory Considerations

- **Sử dụng `final`:** Từ khóa `final` được áp dụng chặt chẽ lên các constant (`MAX_MONEY_INPUT`), định danh (`id`), và đặc biệt là các *setter methods*. Ngoài việc khóa ghi đè logic validation ở lớp con, thiết kế này đóng vai trò gửi tín hiệu rõ ràng để JIT Compiler tự tin thực hiện **Method Inlining** (nếu thỏa mãn điều kiện heuristic), giúp giảm overhead của việc tạo Stack Frame liên tục.
- **Phân bổ bộ nhớ:** Cấu trúc tĩnh (metadata, method bytecode) của các class như `FullTimeEmployee` được JVM nạp độc lập trên **Metaspace**. Khi hệ thống scale tạo ra hàng vạn nhân viên, chỉ có vùng nhớ **Heap** bị ảnh hưởng (cấp phát instance), trong khi chi phí cho Metaspace không phình to.
