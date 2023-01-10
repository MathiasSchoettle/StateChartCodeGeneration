package component;

import gen.Generator;

public class Timer {
    public String name;
    public Integer id;
    public Integer duration;

    public String getSetTimer() {
        return String.format(Generator.SET_TIMER, id, duration, name);
    }
}
