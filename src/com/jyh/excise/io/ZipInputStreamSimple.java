package com.jyh.excise.io;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * TODO
 *
 * @Author jiangyonghua
 * @Date 2020/1/29 12:56
 * @Version 1.0
 **/
public class ZipInputStreamSimple {

    public static void main(String[] args) throws URISyntaxException, IOException {
        // 写入zip
        Path path = Paths.get(ZipInputStreamSimple.class.getResource(".").toURI());
        System.out.println(path.toRealPath());
        File file = new File(path.toRealPath() + "/ZipStreamTest.zip");
        if (!file.exists()) {
            file.createNewFile();
        }
        byte[] content = "hello zipInputStreamSimple test...".getBytes();
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file))) {
            zos.putNextEntry(new ZipEntry("test"));
            zos.write(content);
            zos.closeEntry();
        }

        // 读取zip
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(file))) {
            ZipEntry entry = null;
            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();
                if (!entry.isDirectory()) {
                    int n;
                    while ((n = zis.read()) != -1) {
                        System.out.println(n);
                    }
                }
            }
        }
    }
}
