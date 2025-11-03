package org.ynov.communication;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class Connection {
    public BufferedReader in;
    public PrintWriter out;

    public Connection(BufferedReader in, PrintWriter out){
        this.in = in;
        this.out = out;
    }
}
