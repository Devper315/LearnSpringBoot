import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) {
        // Tạo một đối tượng Date hiện tại
        Date date = new Date();

        // Chuyển đổi Date thành Instant, cộng thêm 120 giây và chuyển đổi thành mili giây từ Epoch
        long epochMilli = date.toInstant().plus(120, ChronoUnit.SECONDS).toEpochMilli();

        // Tạo một đối tượng Date mới từ giá trị mili giây
        Date newDate = new Date(epochMilli);

        // In ra kết quả
        System.out.println("Thời gian hiện tại: " + date);
        System.out.println("Thời gian sau 120 giây (mili giây từ Epoch): " + epochMilli);
        System.out.println("Đối tượng Date mới: " + newDate);
    }
}
