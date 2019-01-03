package spring.dev.calc;

import java.io.BufferedReader;
import java.io.IOException;

public interface BufferdReaderCallback {
    Integer doSumethingWithReader(BufferedReader br) throws IOException;
}