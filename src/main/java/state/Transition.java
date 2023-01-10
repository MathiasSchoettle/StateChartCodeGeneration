package state;

import state.imported.Timer;

public class Transition {
    public State from;
    public State to;
    public String trigger;
    public String emit;
    public Timer timer;
    public String condition;
    public String action;
    public Transition(State from, State to, String trigger, String emit, Timer timer, String condition, String action) {
        this.from = from;
        this.to = to;
        this.trigger = trigger;
        this.emit = emit;
        this.timer = timer;
        this.condition = condition;
        this.action = action;
    }
}
