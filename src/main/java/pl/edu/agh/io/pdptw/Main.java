package pl.edu.agh.io.pdptw;

import pl.edu.agh.io.pdptw.model.LLBenchmark;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestLLB;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.reader.RequestReader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


public class Main {

    public static void main(String[] args) throws IOException {

        RequestReader requestReader = new RequestReader();
        File file = new File("Li & Lim benchmark/readerTest/lc101.txt");
        System.out.println(file.getCanonicalPath());
        LLBenchmark benchmark = requestReader.readFile(file);
    }
}
