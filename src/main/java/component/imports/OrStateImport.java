package component.imports;

import component.state.OrState;
import component.state.State;

public class OrStateImport extends StateImport {
    public StateChartImport stateChart;
    @Override
    public State fromImport(int count, String statePreamble, String activationPathPreamble) {
        String stateName = formatStateName(statePreamble);
        String activationPath = String.format(ACTIVATION_PATH_TEMPLATE, activationPathPreamble, name);
        String nextPreamble = (statePreamble.isEmpty() ? "" : statePreamble + "_" + this.name);

        var state =  new OrState(name, stateName, count, activationPath);
        state.stateChart = this.stateChart.fromImport(nextPreamble, activationPath + "/Region");

        return state;
    }
}
