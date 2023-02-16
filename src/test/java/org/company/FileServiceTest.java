package org.company;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    private static FileService fs ;
    private final static String path = "src/test/resources/";
    private final static String filename = "FileServiceTest.csv";

    @BeforeAll
    static void setUp() {
        fs = new FileService();
    }

    @AfterAll
    static void tearDown() {
        deleteFile(path+filename);
    }

    @Test
    void readWhenFileExists() {
        createFile(path+filename, "Testing readWhenFileExists");

        try (InputStream data = fs.read(new Object[]{path+filename});) {
            assertEquals("Testing readWhenFileExists", new String(data.readAllBytes()));
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Test
    void readWhenFileDoesNotExists() {
        Class clazz = null;
        try (InputStream data = fs.read(new Object[]{path+"somefile.csv"});) {

        } catch (IOException e) {
            clazz = e.getClass();
        }
        assertEquals(java.io.FileNotFoundException.class, clazz);
    }

    @Test
    void writeNormalData() {
        try  {
            fs.write(new Object[]{path+filename, new StringBuffer("Testing writeNormalData")});
            assertEquals("Testing writeNormalData", readFile(path+filename));
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Test
    void writeBlankData() {
        try  {
            fs.write(new Object[]{path+filename, new StringBuffer("")});
            assertEquals("", readFile(path+filename));
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

    private static void createFile(String file, String data) {
        try (OutputStream out = new FileOutputStream(file)) {
            out.write(data.getBytes());
        }
        catch (IOException io) {
            System.out.println("Could not create the test file " + file);
            io.printStackTrace();
        }
    }

    private static void deleteFile(String file) {
        try {
            Files.deleteIfExists(new File(file).toPath());
        }
        catch (IOException io) {
            System.out.println("Could not delete test file "+file);
            io.printStackTrace();
        }
    }

    private static String readFile(String file) {
        String data = null;
        try (InputStream inputStream = new FileInputStream(file)) {
            data = new String(inputStream.readAllBytes());
        } catch (IOException io) {
            System.out.println("Could not read the test file "+file);
            io.printStackTrace();
        }
        return data;
    }
}