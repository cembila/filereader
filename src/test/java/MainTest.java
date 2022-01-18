import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

class MainTest {


    Path testResources = Paths.get("src/test/resources");


    /**
     * Tests readInputFile method of Main.java class.
     */
    @Test
    void readInputFile() {
        Path inputTestFile = testResources.resolve("InputTestFile.txt");
        ArrayList<String> linesAsListExpected = new ArrayList<String>();
        linesAsListExpected.add("a");
        linesAsListExpected.add("b");
        linesAsListExpected.add("c");
        linesAsListExpected.add("d");
        linesAsListExpected.add("e");
        ArrayList<String> linesAsListActual = Main.readInputFile(String.valueOf(inputTestFile));
        assertEquals(linesAsListExpected, linesAsListActual);
    }


    /**
     * Tests writeToOutputFile method of Main.java class.
     */
    @Test
    void writeToOutputFile() {
        Path outputExpected = testResources.resolve("OutputExpected.txt");
        Path outputActual = testResources.resolve("OutputActual.txt");
        ArrayList<String> linesAsList = new ArrayList<String>();
        linesAsList.add("HELLO");
        linesAsList.add("HELLO WORLD");
        Main.writeToOutputFile(String.valueOf(outputActual), linesAsList);
        assertEquals(Main.readInputFile(String.valueOf(outputExpected)),
                Main.readInputFile(String.valueOf(outputActual)));
    }


    /**
     * Tests capitalize method of Main.java class.
     */
    @Test
    void capitalize() {
        ArrayList<String> linesAsListInput = new ArrayList<String>();
        linesAsListInput.add("a");
        linesAsListInput.add("helLO WorlD");
        linesAsListInput.add("hello");

        ArrayList<String> linesExpected = new ArrayList<String>();
        linesExpected.add("A");
        linesExpected.add("HELLO WORLD");
        linesExpected.add("HELLO");

        ArrayList<String> linesActual = new ArrayList<String>();
        linesActual = Main.capitalize(linesAsListInput);

        assertEquals(linesExpected, linesActual);
    }


    /**
     * Tests reverse method of Main.java class.
     */
    @Test
    void reverse() {
        ArrayList<String> linesAsListInput = new ArrayList<String>();
        linesAsListInput.add("hello world");
        linesAsListInput.add("HELLO WORLD");
        linesAsListInput.add("12345");

        ArrayList<String> linesExpected = new ArrayList<String>();
        linesExpected.add("dlrow olleh");
        linesExpected.add("DLROW OLLEH");
        linesExpected.add("54321");

        ArrayList<String> linesActual = new ArrayList<String>();
        linesActual = Main.reverse(linesAsListInput);

        assertEquals(linesExpected, linesActual);
    }


    /**
     * Tests negate method of Main.java class.
     */
    @Test
    void negate() {
        ArrayList<String> linesAsListInput = new ArrayList<String>();
        linesAsListInput.add("100");
        linesAsListInput.add("-647");
        linesAsListInput.add("789");

        ArrayList<String> linesExpected = new ArrayList<String>();
        linesExpected.add("-100");
        linesExpected.add("647");
        linesExpected.add("-789");

        ArrayList<String> linesActual = new ArrayList<String>();
        linesActual = Main.negate(linesAsListInput, "int");

        assertEquals(linesExpected, linesActual);
    }


    /**
     * Tests reverseInt method of Main.java class.
     */
    @Test
    void reverseInt() {
        int num1 = 476;    int expected1 = 674;    int actual1 = Main.reverseInt(num1);
        int num2 = 9345;   int expected2 = 5439;   int actual2 = Main.reverseInt(num2);
        int num3 = 7;      int expected3 = 7;      int actual3 = Main.reverseInt(num3);
        int num4 = -543;   int expected4 = -345;   int actual4 = Main.reverseInt(num4);
        int num5 = -9;     int expected5 = -9;     int actual5 = Main.reverseInt(num5);
        int num6 = -10;    int expected6 = -1;     int actual6 = Main.reverseInt(num6);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        assertEquals(expected3, actual3);
        assertEquals(expected4, actual4);
        assertEquals(expected5, actual5);
        assertEquals(expected6, actual6);
    }


    /**
     * Tests reverseDouble method of Main.java class.
     */
    @Test
    void reverseDouble() {
        double num1 = 568.934;    double expected1 = 439.865;    double actual1 = Main.reverseDouble(num1);
        double num2 = -874.265;   double expected2 = -562.478;   double actual2 = Main.reverseDouble(num2);
        double num3 = 0.654;      double expected3 = 456.0;      double actual3 = Main.reverseDouble(num3);
        double num4 = -0.8765;    double expected4 = -5678.0;    double actual4 = Main.reverseDouble(num4);

        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
        Assertions.assertEquals(expected3, actual3);
        Assertions.assertEquals(expected4, actual4);
    }

}