package component.imports;

import component.state.AndState;
import component.state.State;

public class AndStateImport extends StateImport {
    public StateChartImport stateChartOne;
    public StateChartImport stateChartTwo;
    @Override
    public State fromImport(int count, String statePreamble, String activationPathPreamble) {
        String stateName = formatStateName(statePreamble);
        String nextPreamble = (statePreamble.isEmpty() ? "" : statePreamble + "_") + this.name;
        String activationPath = String.format(ACTIVATION_PATH_TEMPLATE, activationPathPreamble, name);

        var state =  new AndState(name, stateName, count, activationPath);
        state.stateChartOne = this.stateChartOne.fromImport(nextPreamble + "_Region", activationPath + "/Region");
        state.stateChartTwo = this.stateChartTwo.fromImport(nextPreamble + "_Region1", activationPath + "/Region1");

        return state;
    }
}
