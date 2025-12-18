import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class forTest {
    public static void main(String[] args) {
        System.out.println(dateToTimeStamp(new Date()));
    }
    // 用于返回指定git格式的日期
    private static String dateToTimeStamp(Date date) {
        // 格式字符串必须严格遵守 Spec
        // Thu Nov 9 20:00:05 2017 -0800
        // EEE = 星期几 (Wed)
        // MMM = 月份 (Dec)
        // d = 日期 (31)
        // HH:mm:ss = 时分秒
        // yyyy = 年
        // Z = 时区 (-0800)
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy  Z", Locale.US);
        return dateFormat.format(date);
    }
}
