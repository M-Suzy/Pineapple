import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ParameterizedTextMapper {
    private static final Logger logger = Logger.getLogger(String.valueOf(ParameterizedTextMapper.class));
    private String unformattedText;
    private Map<String, String> mappingInfo;

    private String formattedText;

    public ParameterizedTextMapper(String unformattedText, Map<String, String> mappingInfo) {
        this.unformattedText = unformattedText;
        this.mappingInfo = mappingInfo;
        this.formattedText=unformattedText;
    }

    public void setUnformattedText(String unformattedText) {
        this.unformattedText = unformattedText;
        this.formattedText = unformattedText;
    }

    public String getUnformattedText() {
        return unformattedText;
    }

    public Map<String, String> getMappingInfo() {
        return mappingInfo;
    }

    public void setMappingInfo(Map<String, String> mappingInfo) {
        this.mappingInfo = mappingInfo;
    }

    public void addMappingInfoEntry(String key, String value){
        mappingInfo.put(key, value);
    }

    /**
     * Given key and value dynamically formats the text and saves mappingInfo in the corresponding hashmap.
     * @param key
     * @param word
     * @return formatted text, otherwise AssertionError for not setting unformatted text
     */
    public String formatTextDynamically(String key, String word) {
        if(mappingInfo.containsKey(key)){
           logger.info("Changing value(s) for the key"+key);
        }
        mappingInfo.put(key, word);
        formattedText = WordInTextMapperUtil.replaceCurlyBracedWord(unformattedText, key,word);
        return formattedText;
    }

    public String getFullFormattedText(){
        formattedText = WordInTextMapperUtil.getFormattedText(unformattedText, mappingInfo);
        return formattedText;
    }

    public List<String> getMissingParameters(){
        return WordInTextMapperUtil.getUnsetParameters(formattedText);
    }

}
