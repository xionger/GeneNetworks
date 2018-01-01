package util;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import graph.BioGraph;

public class BioGraphLoader {
    /**
     * Loads graph with data from a file.
     * The file should consist of lines with 2 strings and 1 double each, corresponding
     * to a "from" vertex, a "to" vertex and a "length".
     */ 
    public static void loadGraph(graph.BioGraph g, String filename) {
        Set<String> seen = new HashSet<String>();
        Scanner sc;
        try {
            sc = new Scanner(new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        // Iterate over the lines in the file, adding new
        // vertices as they are found and connecting them with edges.
        while (sc.hasNextLine()) {
            String s1 = sc.next();
            String s2 = sc.next();
            double l = sc.nextDouble();
            if (!seen.contains(s1)) {
                g.addVertex(s1);
                seen.add(s1);
            }
            if (!seen.contains(s2)) {
                g.addVertex(s2);
                seen.add(s2);
            }
            g.addEdge(s1, s2, l);
        }
        
        sc.close();
    }
}
