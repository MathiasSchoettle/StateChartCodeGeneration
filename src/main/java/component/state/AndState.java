package component.state;

import component.StateChart;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static gen.Generator.*;

public class AndState extends ComplexState {
    public StateChart stateChartOne;
    public StateChart stateChartTwo;

    public AndState(String name, String stateName, int stateValue, String activationPath) {
        super(name, stateName, stateValue, activationPath);
    }

    @Override
    public String getDefines() {
        String define = String.format(STATE_DEFINE_TEMPLATE, stateName(), stateValue);
        String definesOne = stateChartOne.states.stream().map(State::getDefines).collect(Collectors.joining("\n"));
        String definesTwo = stateChartTwo.states.stream().map(State::getDefines).collect(Collectors.joining("\n"));
        return define + "\n" + definesOne + "\n" + definesTwo;
    }

    @Override
    public List<String> getEvents() {
        return Stream.concat(stateChartOne.getEvents().stream(), stateChartTwo.getEvents().stream()).toList();
    }

    @Override
    public String getFunctions() {
        var builder = new StringBuilder();

        String nameOne = stateName + "_" + REGION_APPENDIX_ONE;
        String nameTwo = stateName + "_" + REGION_APPENDIX_TWO;

        builder.append(stateChartOne.getFunction(nameOne));
        builder.append(stateChartTwo.getFunction(nameTwo));

        String stepCode = step("", REGION_APPENDIX_ONE) + step("", REGION_APPENDIX_TWO);
        builder.append(String.format(DO_TEMPLATE, name, indent(stepCode)));

        String leaveCode = leave("", REGION_APPENDIX_ONE) + leave("", REGION_APPENDIX_TWO) + String.format(IF_DEF_TEMPLATE, deactivation() + "\n");
        builder.append(String.format(LEAVE_TEMPLATE, name, indent(leaveCode)));

        return builder.toString();
    }

    @Override
    public String getStateVariables() {
        return stateChartOne.getStateVariables() + stateChartTwo.getStateVariables();
    }
}
