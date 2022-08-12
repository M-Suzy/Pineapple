import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class WordInTextMapperUtilTest {

    @Test
    void givenTextWithCurlyBracedParams_shouldReturnTrue(){
        assertTrue(WordInTextMapperUtil.hasUnsetParameters("Hello, {{name}}! {{name}} how r u?"));
    }
    @Test
    void givenTextWithoutCurlyBracedParam_shouldReturnFalse(){
        assertFalse(WordInTextMapperUtil.hasUnsetParameters("Hello, Valod! {} how r u?"));
    }
    @Test
    void givenTextWithCurlyBracesWithEmptyParam_shouldReturnFalse(){
        assertFalse(WordInTextMapperUtil.hasUnsetParameters("Hello, {{}}! {{}} how r u?"));
    }

    @Test
    void givenTextWithValidBraces_replacedAllInsideTwoCurlyBraces_thenReturnTrue(){
        String unformattedText = "Hello, {{name}}! {{name}} how r u?";
        String expectedResult = "Hello, Valod! Valod how r u?";
        assertEquals(expectedResult,
                WordInTextMapperUtil.replaceCurlyBracedWord(unformattedText, "name", "Valod"));
    }


    @ParameterizedTest
    @ArgumentsSource(TextWithRemainingUnsetParametersArgumentProvider.class)
    void givenMissingParametersAndText_correctNumberAndMatchList_thenReturnTrue(String text, String[] missingParamsExpected){
        List<String> missingParamsRes = WordInTextMapperUtil.getUnsetParameters(text);
        assertEquals(missingParamsExpected.length, missingParamsRes.size());
        for(int i = 0; i<missingParamsExpected.length; i++){
            assertEquals(missingParamsExpected[i], missingParamsRes.get(i));
        }

    }

    @ParameterizedTest
    @ArgumentsSource(IncorrectBracesArgumentProvider.class)
    void givenTextWithInValidNumberOfBraces_notReplaced_thenReturnTrue(String text, String key, String word){
        assertEquals(text, WordInTextMapperUtil.replaceCurlyBracedWord(text, key, word));
    }

    private static class IncorrectBracesArgumentProvider implements ArgumentsProvider {
        private static final String[] TEXT_WITH_INCORRECT_BRACES = {
                "Hello, {{name}}}! ",
                "{{{name}} how are you?",
                "phone: {number}, ",
                "firstname: {{firstname}}}}",
                "Hello, {{world "
        };
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context){
            return Stream.of(
                    Arguments.of(TEXT_WITH_INCORRECT_BRACES[0], "name","Suzy"),
                    Arguments.of(TEXT_WITH_INCORRECT_BRACES[1], "name", ""),
                    Arguments.of(TEXT_WITH_INCORRECT_BRACES[2], "number", "12314"),
                    Arguments.of(TEXT_WITH_INCORRECT_BRACES[3], "firstname", "Suzy"),
                    Arguments.of(TEXT_WITH_INCORRECT_BRACES[3], "world", "Europe")
            );
        }
    }

    private static class TextWithRemainingUnsetParametersArgumentProvider implements ArgumentsProvider{
        private static final String[] TEXT_WITH_UNSET_PARAMETERS = {
                "name: {{string}}, {{}}",
                "firstname: {{name}}, phone: {{phoneNumber}}, age: {{age}} ",
                "phone: {number}, "
        };
        private static final String[] resFirstLine = {"string"};
        private static final String[] resSecondLine = {"name", "phoneNumber", "age"};
        private static final String[] resThirdLine = {};
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext){
            return Stream.of(
                    Arguments.of(TEXT_WITH_UNSET_PARAMETERS[0], resFirstLine),
                    Arguments.of(TEXT_WITH_UNSET_PARAMETERS[1], resSecondLine),
                    Arguments.of(TEXT_WITH_UNSET_PARAMETERS[2],resThirdLine)
            );
        }
    }

}
