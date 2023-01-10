package component.state;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static gen.Generator.*;

public abstract class ComplexState extends State {
    public ComplexState(String name, String stateName, int stateValue, String activationPath) {
        super(name, stateName, stateValue, activationPath);
    }
    public abstract String getStateVariables();
    @Override
    public String step(String prefix, String postfix) {
        return String.format(CALL_DO_TEMPLATE, createName(prefix, postfix)) + "\n";
    }
    @Override
    public String leave(String prefix, String postfix) {
        return String.format(CALL_LEAVE_TEMPLATE, createName(prefix, postfix)) + "\n";
    }
    private String createName(String prefix, String postfix) {
        return Stream.of(prefix, name, postfix)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining("_"));
    }
}
