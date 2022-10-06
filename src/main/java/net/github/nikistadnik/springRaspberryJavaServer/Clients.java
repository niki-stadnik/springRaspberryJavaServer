package net.github.nikistadnik.springRaspberryJavaServer;

import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.util.ArrayList;

@Component
public class Clients {
    private final ArrayList<PrintWriter> pW;

    public Clients() {
        pW = new ArrayList<>(10);
    }

    public synchronized void addC(PrintWriter p) {
        pW.add(p);
    }

    public synchronized void rmvC(PrintWriter p) {
        pW.remove(p);
    }

    public synchronized void sendC(String s) {
        for (PrintWriter p : pW) {
            p.println(s);
        }
    }

    public synchronized int nCl() {
        return pW.size();
    }

}
