package component.imports;

import component.state.State;
import component.Timer;
import component.Transition;

import java.util.List;

public class TransitionImport {
    public String from;
    public String to;
    public String trigger;
    public String emit;
    public Timer timer;
    public String condition;
    public String action;

    public Transition fromImport(List<State> states) {
        State from = states.stream().filter(s -> s.name.equals(this.from)).findFirst().orElse(null);
        State to = states.stream().filter(s -> s.name.equals(this.to)).findFirst().orElseThrow();
        return new Transition(from, to, trigger, emit, timer, condition, action);
    }
}
