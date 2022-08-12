import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * General utility class for setting values instead of curly braces with parameters
 */
public class WordInTextMapperUtil {

    private static final Logger logger = Logger.getLogger(String.valueOf(WordInTextMapperUtil.class));
    private static final String PATTERN_FOR_GENERAL_CURLY_BRACED = "(?<!\\{)\\{\\{(?:([^(?!*${}).+]+)|})}}(?!})";

    /**
     * Replaces all the parameters with the values from mappingInfo
     * @param unformattedText
     * @param mappingInfo
     * @return formatted text if mapping info is not empty
     */
    public static String getFormattedText(String unformattedText, Map<String, String> mappingInfo) {
        assertNotNullArguments(unformattedText, mappingInfo);
        if(mappingInfo.isEmpty()){
            logger.info("No mappingInfo for setting parameters into text");
            return unformattedText;
        }
        String formattedRes = unformattedText;
        for (Map.Entry<String, String> entry : mappingInfo.entrySet()) {
            formattedRes = replaceCurlyBracedWord(formattedRes, entry.getKey(), entry.getValue());
        }
        return formattedRes;
    }

    /**
     * Replaces all the occurrences of the given key inside the text with the given value
     * @param text
     * @param key
     * @param replacementValue
     * @return formatted text or IllegalArgumentException if null passed
     */
    public static String replaceCurlyBracedWord(String text, String key, String replacementValue) {
        assertNotNullArguments(text, key, replacementValue);
        String regex = "(?<!\\{)\\{\\{(?:([" + key + "]+)|})}}(?!})";
        return text.replaceAll(regex, replacementValue);
    }

    /**
     * Checks whether the unformatted text has curly braces with parameter
     * @param text
     * @return true if there are any unset parameters, otherwise false
     */
    public static boolean hasUnsetParameters(String text){
        assertNotNullArguments(text);
        return Pattern.compile(PATTERN_FOR_GENERAL_CURLY_BRACED).matcher(text).find();
    }

    /**
     * Identifies the unset parameters inside unformatted text
     * @param text
     * @return list of parameters that should be filled, otherwise empty list
     */
    public static List<String> getUnsetParameters(String text){
        assertNotNullArguments(text);
        List<String> matches = new ArrayList<>();
        Matcher m = Pattern.compile(PATTERN_FOR_GENERAL_CURLY_BRACED).matcher(text);
        while(m.find()) {
            matches.add(m.group(1));
        }
        return matches;
    }

    private static void assertNotNullArguments(Object... obj){
        for(Object o : obj){
            if(o==null){
                throw new IllegalArgumentException("Argument passed is null");
            }
        }
    }

}
