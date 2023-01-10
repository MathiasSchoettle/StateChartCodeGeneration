package state;

import java.util.List;
import java.util.stream.Collectors;

import static code.CodeGenerator.*;

public class OrState extends ComplexState {
    public StateChart stateChart;

    public OrState(String name, String stateName, int stateValue, String activationPath) {
        super(name, stateName, stateValue, activationPath);
    }
    @Override
    public String getDefines() {
        String define = String.format(STATE_DEFINE_TEMPLATE, stateName(), stateValue);
        return define + "\n" + stateChart.states.stream().map(State::getDefines).collect(Collectors.joining("\n"));
    }

    @Override
    public List<String> getEvents() {
        return stateChart.getEvents();
    }

    @Override
    public String getFunctions() {
        return stateChart.getFunction(stateName);
    }
    @Override
    public String getStateVariables() {
        return stateChart.getStateVariables();
    }
}
