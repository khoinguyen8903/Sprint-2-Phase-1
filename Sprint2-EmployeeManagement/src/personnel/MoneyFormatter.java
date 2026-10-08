package personnel;

import java.text.NumberFormat;
import java.util.Locale;

/** Hàm dùng chung để hiển thị tiền nguyên đồng Việt Nam. */
public class MoneyFormatter {

    // Không cần tạo đối tượng cho lớp chỉ chứa phương thức tiện ích static.
    private MoneyFormatter() {
    }

    public static String format(long amount) {
        NumberFormat formatter = NumberFormat.getIntegerInstance(Locale.forLanguageTag("vi-VN"));
        return formatter.format(amount) + " VND";
    }
}
