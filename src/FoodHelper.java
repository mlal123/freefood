import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FoodHelper {
    public static Date checkDate;

    public static boolean check_for_free_food(String sentence){

        Pattern p = Pattern.compile(".*\\bfree\\b.*");
        Matcher m = p.matcher(sentence.toLowerCase());
        boolean b = m.matches();

        return b;
    }

}
