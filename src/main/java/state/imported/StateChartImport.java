package state.imported;

import state.State;
import state.StateChart;
import state.Transition;

import java.util.ArrayList;
import java.util.List;

import static code.CodeGenerator.SOURCE_CODE_STATE_TEMPLATE;

public class StateChartImport {
    public List<StateImport> states;
    public List<TransitionImport> transitions;
    public List<String> timers;
    public List<String> events;
    public StateChart fromImport(String componentName) {
        return fromImport("", componentName);
    }
    public StateChart fromImport(String statePreamble, String activationPathPreamble) {
        List<State> states = new ArrayList<>();
        for (int count = 0; count < this.states.size(); count++) {
            states.add(this.states.get(count).fromImport(count + 1, statePreamble, activationPathPreamble));
        }

        List<Transition> transitions = new ArrayList<>();
        for (var transition : this.transitions) {
            transitions.add(transition.fromImport(states));
        }

        String stateName = SOURCE_CODE_STATE_TEMPLATE + (statePreamble.isEmpty() ? "" : "_" + statePreamble);
        return new StateChart(states, transitions, stateName, timers, events);
    }
}
