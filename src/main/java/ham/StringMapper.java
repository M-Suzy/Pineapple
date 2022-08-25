package ham;

import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j
public class StringMapper {

    private String text;
    private static final String REGEX = "(\\{\\{)[^ ]*(\\}\\})";

    private final Map<String, String> map;

    public StringMapper(@NotNull final String text) {
        this.text = text;
        this.map = new HashMap<>();
    }

    public StringMapper(@NotNull final String text, @NotNull final Map<String, String> map) {
        this.text = text;
        this.map = map;
    }

    public void setMapping(@NotNull final String key, @NotNull final String value) {
        if (map.containsKey(key)) {
            log.warn(String.format("Current mapping already contains key '%s' and value '%s', replacing old value with new value as '%s'", key, map.get(key), value));
        } else {
            log.info(String.format("Inserting key '%s' and value '%s' to the current mapping", key, value));
        }
        this.map.put(key, value);
    }

    public String formatText() {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String grossPlaceholder = matcher.group(0);
            final String netPlaceholder = removeCurlyBraces(grossPlaceholder);
            final String realValue = getRealValue(netPlaceholder);
            text = text.replace(grossPlaceholder, realValue);
        }
        return text;
    }

    private String removeCurlyBraces(String grossPlaceholder) {
        log.trace(String.format("Removing curly brackets from '%s'", grossPlaceholder));
        return grossPlaceholder.replaceAll("[\\{\\}]", "");
    }

    private String getRealValue(@NotNull final String netPlaceholder) {
        if (!map.containsKey(netPlaceholder)) {
            log.error(String.format("Current mapping doesn't contain value for placeholder '%s', returning 'null'", netPlaceholder));
            return null;
        }
        return map.get(netPlaceholder);
    }
}
