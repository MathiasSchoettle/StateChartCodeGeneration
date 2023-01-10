import gen.Generator;

import java.io.IOException;
import java.net.URISyntaxException;

import static component.Component.importComponent;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var imported = importComponent("controller.json");
        new Generator(imported).saveToFile("generated");
    }
}
