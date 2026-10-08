# BÁO CÁO TỔNG KẾT SPRINT 2 - PHASE 1: JAVA CORE

TUẦN 3–4 — QUẢN LÝ NHÂN SỰ

## Mục lục

- [1. Bốn tính chất OOP](#muc-1)
  - [1.1. Đóng gói](#muc-2)
  - [1.2. Kế thừa](#muc-3)
  - [1.3. Đa hình](#muc-4)
  - [1.4. Trừu tượng](#muc-5)
- [2. Overloading, overriding và dynamic dispatch qua V-Table](#muc-6)
  - [2.1. Phân biệt overloading và overriding](#muc-7)
  - [2.2. Hai cơ chế trong luồng tính lương có thưởng](#muc-8)
  - [2.3. Dynamic dispatch và V-Table](#muc-9)
- [3. Interface và abstract class](#muc-10)
  - [3.1. Lựa chọn trong thiết kế](#muc-11)
  - [3.2. Áp dụng qua PaymentMethod](#muc-12)
- [4. Từ khóa static, final và Metaspace](#muc-13)
  - [4.1. static](#muc-14)
  - [4.2. final](#muc-15)
  - [4.3. Metaspace và liên hệ với chương trình](#muc-16)

<a name="muc-1"></a>

## 1. Bốn tính chất OOP

<a name="muc-2"></a>

### 1.1. Đóng gói

Trong Employee và hai lớp con, các field được khai báo private; mã bên ngoài phải đi qua setter để thay đổi dữ liệu. Em giữ validation ngay tại bước gán để đối tượng không nhận giá trị sai khi được dùng ngoài luồng console.

Ví dụ trong FullTimeEmployee.java:

```java
public final void setBaseSalary(long baseSalary) {
    validateMoney(baseSalary, "Lương cơ bản");
    this.baseSalary = baseSalary;
}
```

Setter kiểm tra lương trước khi gán. Vì vậy, dù nhập qua console hay gọi setter trực tiếp, giá trị âm hoặc vượt giới hạn đều bị từ chối. Em đặt kiểm tra trong model để quy tắc không phụ thuộc riêng vào màn hình nhập liệu.

<a name="muc-3"></a>

### 1.2. Kế thừa

FullTimeEmployee và PartTimeEmployee kế thừa Employee vì đều là nhân viên, có chung mã, họ tên và phòng ban. Constructor của hai lớp gọi super(id, fullName, department) để khởi tạo phần chung; các lớp con cũng dùng lại validateMoney().

Phần riêng được giữ tại từng lớp: FullTimeEmployee có baseSalary và allowance; PartTimeEmployee có hourlyRate và workingHours. Cách chia này giúp tránh lặp dữ liệu, đồng thời giữ công thức lương cạnh các field mà nó sử dụng.

<a name="muc-4"></a>

### 1.3. Đa hình

EmployeeManager lưu cả hai loại nhân viên trong List<Employee>. Khi tính tổng, manager gọi cùng một phương thức qua kiểu Employee:

```java
public long calculateTotalSalary() {
    long total = 0;
    for (Employee employee : employees) {
        total += employee.calculateSalary();
    }
    return total;
}
```

Nếu đối tượng là FullTimeEmployee, công thức là baseSalary + allowance; nếu là PartTimeEmployee, công thức là hourlyRate * workingHours. Vòng lặp không cần kiểm tra loại nhân viên bằng if-else hoặc instanceof.

Đây là cách áp dụng Open/Closed Principle (OCP) vào phần tổng hợp lương: có thể thêm InternEmployee extends Employee và triển khai các phương thức abstract mà không sửa calculateTotalSalary(). Tính mở rộng (OCP) ở đây tập trung vào luồng xử lý nghiệp vụ (Business Logic). Tầng giao diện (Console Input) tất nhiên vẫn cần cập nhật để hỗ trợ việc khởi tạo InternEmployee mới.

Ví dụ đã kiểm tra: nhân viên toàn thời gian có lương cơ bản 10 triệu và phụ cấp 2 triệu nhận 12 triệu; nhân viên bán thời gian làm 80 giờ với đơn giá 50.000 đồng nhận 4 triệu. Cùng vòng lặp trên tính được tổng 16 triệu đồng.

<a name="muc-5"></a>

### 1.4. Trừu tượng

Employee khai báo phương thức public abstract long calculateSalary() vì chỉ có dữ liệu chung thì chưa xác định được công thức lương. Lớp này không được khởi tạo trực tiếp; mỗi lớp con cụ thể phải cung cấp cách tính của mình.

Phần gọi chỉ cần biết nhân viên có thể tính lương, không cần biết từng phép cộng hoặc nhân bên trong. Trong thiết kế này, trừu tượng xác định hành vi cần có; đa hình cho phép gọi hành vi đó với nhiều loại nhân viên.

<a name="muc-6"></a>

## 2. Overloading, overriding và dynamic dispatch qua V-Table

<a name="muc-7"></a>

### 2.1. Phân biệt overloading và overriding

| Tiêu chí | Overloading — nạp chồng | Overriding — ghi đè |
| --- | --- | --- |
| Đặc điểm | Cùng tên, khác danh sách tham số | Lớp con định nghĩa lại phương thức instance được kế thừa |
| Trong chương trình | calculateSalary() và calculateSalary(long bonus) | Hai lớp con triển khai calculateSalary() của Employee |
| Cách lựa chọn | Chọn chữ ký phương thức khi biên dịch | Chọn phần triển khai theo đối tượng thực tế khi chạy |

Hai phương thức chỉ khác kiểu trả về thì chưa tạo thành overload. Với overriding, chữ ký phải phù hợp với phương thức ở lớp cha; @Override giúp compiler phát hiện việc khai báo sai.

<a name="muc-8"></a>

### 2.2. Hai cơ chế trong luồng tính lương có thưởng

Employee.java cung cấp phiên bản nhận thêm bonus:

```java
public long calculateSalary(long bonus) {
    validateMoney(bonus, "Tiền thưởng");
    return calculateSalary() + bonus;
}
```

Khi gọi employee.calculateSalary(500_000L), compiler chọn phiên bản nhận long, được biểu diễn bằng descriptor (J)J trong bytecode. Bên trong phương thức này, lời gọi calculateSalary() có descriptor ()J nhưng đối tượng nhận vẫn là this ban đầu, nên JVM chọn cách tính của lớp con. Overloading chọn chữ ký khi biên dịch; overriding quyết định phần triển khai khi chạy.

Với nhân viên toàn thời gian ở mục 1.3, kết quả có thưởng là 12.500.000 đồng. Phương thức chỉ trả kết quả tính toán, không sửa field lương; vì vậy tổng lương gốc vẫn là 16.000.000 đồng. Em dùng overload để thêm cách tính có thưởng mà vẫn dùng lại công thức của từng loại nhân viên.

<a name="muc-9"></a>

### 2.3. Dynamic dispatch và V-Table

Bytecode của vòng lặp dùng invokevirtual với Employee.calculateSalary:()J. Tham chiếu này xác định phương thức cần gọi; nó chưa cố định lời gọi vào FullTimeEmployee hay PartTimeEmployee.

Với đường dispatch qua V-Table trong HotSpot 21, JVM đọc klass pointer ở object header để tìm metadata của lớp thực tế trong Metaspace; con trỏ này có thể ở dạng nén. Từ metadata, slot ứng với calculateSalary() dẫn tới phương thức của FullTimeEmployee hoặc PartTimeEmployee. V-Table gắn với lớp và được các đối tượng cùng lớp dùng chung, nên không cần dựng lại bảng cho mỗi nhân viên.

Đó là lý do cùng một lệnh invokevirtual vẫn chọn được công thức đúng. JIT có thể chuyển lời gọi thành lời gọi trực tiếp hoặc inline dựa trên thông tin kiểu, nên đường tra bảng trên là cơ chế dispatch nền tảng, không phải chuỗi thao tác bắt buộc ở mọi lần gọi.

<a name="muc-10"></a>

## 3. Interface và abstract class

<a name="muc-11"></a>

### 3.1. Lựa chọn trong thiết kế

| Tiêu chí | Abstract class | Interface |
| --- | --- | --- |
| Vai trò trong bài | Employee giữ dữ liệu, constructor và kiểm tra chung | PaymentMethod xác định hành vi pay() |
| Trạng thái đối tượng | Có thể giữ field riêng cho từng đối tượng | Không có field instance hoặc constructor |
| Quan hệ lớp | Một lớp chỉ extends một lớp cha | Một lớp có thể implements nhiều interface |

Thiết kế này tận dụng Interface không chỉ để định nghĩa một hợp đồng (contract) thuần túy, mà cốt lõi là để tách biệt hoàn toàn hành vi thanh toán khỏi cây kế thừa (inheritance tree) của Employee.

Em dùng Employee làm abstract class vì hai loại nhân viên cần dùng lại dữ liệu và logic chung. PaymentMethod được tách thành interface vì cách nhận lương độc lập với bản chất của nhân viên: một nhân viên toàn thời gian có thể nhận tiền mặt hoặc chuyển khoản mà công thức lương không bị ảnh hưởng.

<a name="muc-12"></a>

### 3.2. Áp dụng qua PaymentMethod

Hợp đồng trong PaymentMethod.java:

```java
public interface PaymentMethod {
    void pay();
}
```

CashPayment và BankTransferPayment cùng triển khai interface này. Mỗi đối tượng thanh toán giữ một tham chiếu Employee để lấy thông tin người nhận và gọi calculateSalary(). ConsoleApplication.payAllEmployees() gom các đối tượng vào List<PaymentMethod>, rồi thực hiện:

```java
for (PaymentMethod payment : payments) {
    payment.pay();
}
```

Lời gọi payment.pay() được biên dịch thành invokeinterface; JVM chọn phần triển khai theo đối tượng CashPayment hoặc BankTransferPayment. Thêm một lớp implements PaymentMethod không làm đổi vòng lặp thực thi này, nhưng readPaymentMethod() vẫn cần thêm lựa chọn để tạo đối tượng mới. Đây cũng là phạm vi áp dụng OCP trong phần thanh toán. Hai lớp hiện chỉ in thông báo mô phỏng.

<a name="muc-13"></a>

## 4. Từ khóa static, final và Metaspace

<a name="muc-14"></a>

### 4.1. static

Trong Employee.java, em dùng một giới hạn nhập tiền chung cho các nhân viên:

```java
public static final long MAX_MONEY_INPUT = 1_000_000_000L;
```

Model và phần nhập liệu cùng tham chiếu MAX_MONEY_INPUT. Vì đây là long final khởi tạo bằng biểu thức hằng, javac thay chỗ sử dụng bằng giá trị 1.000.000.000; bytecode đã phân tích có lệnh ldc2_w tải giá trị này. Đây là xử lý ở compile time, khác với JIT inlining phương thức. Khi đổi giới hạn, cần biên dịch lại cả các lớp sử dụng hằng.

MoneyFormatter.format(long) là static vì định dạng tiền không cần trạng thái của một đối tượng MoneyFormatter. Lời gọi static xác định phương thức theo lớp, không chọn phiên bản theo kiểu runtime của một nhân viên.

Ngược lại, tên, phòng ban và các khoản lương là field instance. Mỗi nhân viên cần giữ dữ liệu riêng; dùng static cho các field đó sẽ khiến các đối tượng cùng dùng một giá trị.

<a name="muc-15"></a>

### 4.2. final

| Vị trí trong chương trình | Tác dụng |
| --- | --- |
| Employee.id | Chỉ gán khi khởi tạo, không đổi mã trên cùng đối tượng |
| EmployeeManager.employees | Không gán lại biến sang một danh sách khác |
| Các setter final | Lớp con không được ghi đè phần kiểm tra và gán dữ liệu |
| MAX_MONEY_INPUT là static final | Khai báo giới hạn dùng chung và không cho gán lại |

final trên biến tham chiếu không làm đối tượng bất biến: employees vẫn thêm hoặc xóa phần tử được, còn Employee vẫn có thể đổi tên qua setter.

Với các setter final, đích gọi không thể bị lớp con thay thế. Điều này giúp JIT xác định phần triển khai và có thể inline, tức hợp phần xử lý của phương thức vào nơi gọi để giảm chi phí gọi hàm. JIT cũng có thể inline phương thức không final; quyết định còn phụ thuộc độ lớn hàm và tần suất gọi. Tuy nhiên, mục tiêu chính của em khi dùng final là bảo vệ tính toàn vẹn của validation; đối với việc tối ưu của JIT, em chưa thực hiện benchmark để có số liệu kết luận chính thức về hiệu năng.

Employee.id còn được hưởng cơ chế bảo đảm khởi tạo của final field: nếu this không bị lộ ra ngoài trước khi constructor hoàn tất, thread khác nhận được đối tượng sẽ đọc đúng giá trị id đã khởi tạo. Bảo đảm này không làm các field có thể thay đổi hoặc danh sách employees trở nên thread-safe; ứng dụng hiện vẫn xử lý tuần tự trên một thread.

<a name="muc-16"></a>

### 4.3. Metaspace và liên hệ với chương trình

Trong HotSpot, Metaspace là vùng bộ nhớ native lưu metadata của lớp, như cấu trúc lớp và thông tin phương thức. Khi Employee và hai lớp con được nạp, JVM có metadata để sử dụng các lớp đó. Các đối tượng nhân viên được tạo bằng new nằm trên Heap; tạo thêm nhân viên không đồng nghĩa với nạp lại cùng lớp cho từng đối tượng.

Log đã thu được khi chạy chương trình:

```text
Metaspace used 689K, committed 896K, reserved 1114112K
```

used là phần đang sử dụng, committed là phần đã được cấp phát để sử dụng, còn reserved là vùng địa chỉ được dành trước. Số 689K bao gồm metadata của cả ứng dụng và thư viện trong JVM, không phải riêng các lớp personnel.

Qua quan sát này, có thể thấy rõ sự tách biệt giữa metadata của class và dữ liệu của instance. Từ khóa static mô tả thành viên thuộc về lớp; nó không có nghĩa mọi dữ liệu static đều nằm trong Metaspace. Trong bài này, mỗi nhân viên có dữ liệu riêng trên Heap, còn thông tin định nghĩa lớp được JVM nạp và quản lý độc lập.
