package com.jyh.excise.io.reader;

import com.jyh.excise.io.ZipInputStreamSimple;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * TODO
 *
 * @Author jiangyonghua
 * @Date 2020/1/29 20:15
 * @Version 1.0
 **/
public class Simple {

    public static void main(String[] args) throws URISyntaxException, IOException {

        Path path = Paths.get(Simple.class.getResource("demo.txt").toURI());
        try (Reader reader = new FileReader(path.toUri().getPath())) {
            char[] buffer = new char[1000];
            int n;
            while ((n = reader.read(buffer)) != -1) {
                String msg = new String(buffer, 0, n);
                System.out.println("reader:" + n + "chars,content is:" + msg);
            }
        }
    }
}
