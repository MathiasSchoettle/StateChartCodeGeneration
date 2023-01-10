package utils;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {

    public static String indent(String input) {
        return indent(input, 1);
    }

    public static String indent(String input, int levels) {
        return input.lines()
                .map(s -> IntStream.range(0, levels).mapToObj(i -> "\t").collect(Collectors.joining("")) + s)
                .collect(Collectors.joining("\n")) + "\n";
    }
}
