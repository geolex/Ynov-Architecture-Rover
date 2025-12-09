package org.ynov.utils;

import org.ynov.communication.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;


public class InMemoryConnectionFactory {

    public static class Pair {
        public final Connection connA;
        public final Connection connB;

        public Pair(Connection a, Connection b) {
            this.connA = a;
            this.connB = b;
        }
    }

    public static Pair createPair() throws IOException {
        // pipes
        PipedWriter wA = new PipedWriter();
        PipedReader rA = new PipedReader(wA);

        PipedWriter wB = new PipedWriter();
        PipedReader rB = new PipedReader(wB);

        // Connection A reads rA (quelqu'un écrit sur wA), writes to wB (B lit rB)
        BufferedReader brA = new BufferedReader(rA);
        PrintWriter pwA = new PrintWriter(wB, true);

        // Connection B reads rB, writes to wA
        BufferedReader brB = new BufferedReader(rB);
        PrintWriter pwB = new PrintWriter(wA, true);

        Connection a = new Connection(brA, pwA);
        Connection b = new Connection(brB, pwB);

        return new Pair(a, b);
    }
}

