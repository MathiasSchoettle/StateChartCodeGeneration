package state.imported;

import static code.CodeGenerator.SET_TIMER;

public class Timer {
    public String name;
    public Integer id;
    public Integer duration;

    public String getSetTimer() {
        return String.format(SET_TIMER, id, duration, name);
    }
}
