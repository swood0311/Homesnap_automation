package homesnap.automation.framework;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TestHelper {
	
	public static String getTime() {
		
		SimpleDateFormat formatObj = new SimpleDateFormat("h:mm");
        Date date = new Date();
        return formatObj.format(date);
        
	}

	public static String generateFiveRandomString() {
		
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		String output = sb.toString();
		return output;
	}
}

		

