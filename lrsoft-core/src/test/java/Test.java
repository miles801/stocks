import com.alidayu.utils.SMSSender;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael
 */
public class Test {
    public static void main(String[] args) {
        SMSSender sender = SMSSender.getInstance();
        final Map<String, String> params = new HashMap<String, String>();
        params.put("name", "Michael");
        sender.send("SMS_13246319", "18511580358", params);
    }
}
