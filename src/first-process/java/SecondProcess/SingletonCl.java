package SecondProcess;

import SecondProcess.ControllerArea.Controller;

import java.util.ArrayList;

public class SingletonCl {
    private ArrayList<SourceJSON> sources = new ArrayList<>();

    private static  SingletonCl ourInstance = new SingletonCl();

    public static SingletonCl getInstance() {
        return ourInstance;
    }

    private SingletonCl() {
    }

    public ArrayList<SourceJSON> getSources() {
        return sources;
    }

    synchronized void setSources(ArrayList<SourceJSON> sources) {
        this.sources = sources;
    }
}
