import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * Takes the following command line arguments from terminal:
 * - input : A plaintext file (UTF8 encoded) containing data elements.
 * - inputType : Specifies the type of data elements.
 * - operations : Describes a pipline of what to do with data elements.
 * - threads : Specifies the number of threads to use when reading the input file and applying operations.
 * - output : The output text file to write the results.
 *
 * Command line arguments are parsed by using the Apache Commons API.
 * Operations are applied on the data elements in the input text file.
 * Results are written to output text file.
 *
 * @author Cem Bila
 */
public class Main {


    public static void main(String[] args) throws IOException, ParseException {

        String outputFile = "output.txt";
        deleteFile(outputFile);


        String inputFile = "";
        String inputType = "";
        String operations = "";
        String threads = "";
        String output = "";


        // create the Options
        Options options = new Options();
        options.addOption("input", true, "input text file");
        options.addOption("inputType", true, "type of data elements");
        options.addOption("operations", true, "pipeline of operations");
        options.addOption("threads", true, "number of threads");
        options.addOption("output", true, "output file");


        // parse command line arguments
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException parseException) {
            System.err.println("Parsing exception: " + parseException.getMessage());
        }


        // retrieve argument values
        if(cmd.hasOption("input")) {
            inputFile = cmd.getOptionValue("input");
            System.out.println("Input file: " + inputFile);
        }
        if(cmd.hasOption("inputType")) {
            inputType = cmd.getOptionValue("inputType");
            System.out.println("Input type: " + inputType);
        }
        if(cmd.hasOption("operations")) {
            operations = cmd.getOptionValue("operations");
            System.out.println("Operations: " + operations);
        }
        if(cmd.hasOption("threads")) {
            threads = cmd.getOptionValue("threads");
            System.out.println("Threads: " + threads);
        }
        if(cmd.hasOption("output")) {
            output = cmd.getOptionValue("output");
            System.out.println("Output: " + output);
        }


        // number of operations
        String[] operationsSplit = operations.split(",");
        int numOfOperations = operationsSplit.length;


        // read input file
        ArrayList<String> lines = readInputFile(inputFile);


        // perform operations
        if (numOfOperations == 1) {
            if (Objects.equals(inputType, "string") && Objects.equals(operations, "capitalize")) {
                ArrayList<String> linesToOutput = capitalize(lines);
                writeToOutputFile(outputFile, linesToOutput);
            } else if (Objects.equals(inputType, "string") && Objects.equals(operations, "reverse")) {
                ArrayList<String> linesToOutput = reverse(lines);
                writeToOutputFile(outputFile, linesToOutput);
            } else if ((Objects.equals(inputType, "int") || Objects.equals(inputType, "double")) && Objects.equals(operations, "neg")) {
                ArrayList<String> linesToOutput = negate(lines, inputType);
                writeToOutputFile(outputFile, linesToOutput);
            } else if (Objects.equals(inputType, "int") && Objects.equals(operations, "reverse")) {
                ArrayList<String> linesToOutput = new ArrayList<String>();
                for (String line : lines) {
                    int lineReversedAsInt = reverseInt(Integer.parseInt(line));
                    linesToOutput.add(String.valueOf(lineReversedAsInt));
                }
                writeToOutputFile(outputFile, linesToOutput);
            } else if (Objects.equals(inputType, "double") && Objects.equals(operations, "reverse")) {
                ArrayList<String> linesToOutput = new ArrayList<String>();
                for (String line : lines) {
                    double lineReversedAsDouble = reverseDouble(Double.parseDouble(line));
                    linesToOutput.add(String.valueOf(lineReversedAsDouble));
                }
                writeToOutputFile(outputFile, linesToOutput);
            }
        } else if (numOfOperations > 1) { // operation chain
            List<String> operationsList = new ArrayList<>(Arrays.asList(operationsSplit));
            if (Objects.equals(inputType, "int") && (operationsList.contains("neg")) && (operationsList.contains("reverse"))){
                ArrayList<String> linesToOutput = new ArrayList<String>();
                ArrayList<String> linesNegated = negate(lines, inputType);
                for (String line : linesNegated) {
                    int lineReversedAsInt = reverseInt(Integer.parseInt(line));
                    linesToOutput.add(String.valueOf(lineReversedAsInt));
                }
                writeToOutputFile(outputFile, linesToOutput);
            } else if (Objects.equals(inputType, "double") && (operationsList.contains("neg")) && (operationsList.contains("reverse"))) {
                ArrayList<String> linesToOutput = new ArrayList<String>();
                ArrayList<String> linesNegated = negate(lines, inputType);
                for (String line : linesNegated) {
                    double lineReversedAsDouble = reverseDouble(Double.parseDouble(line));
                    linesToOutput.add(String.valueOf(lineReversedAsDouble));
                }
                writeToOutputFile(outputFile, linesToOutput);
            } else if ((Objects.equals(inputType, "string")) && (operationsList.contains("capitalize")) && (operationsList.contains("reverse"))) {
                ArrayList<String> linesCapitalized = capitalize(lines);
                ArrayList<String> linesReversed = reverse(linesCapitalized);
                writeToOutputFile(outputFile, linesReversed);
            }
        }


        // DO NOT CHANGE THE FOLLOWING LINES OF CODE
        System.out.println(String.format("Processed %d lines (%d of which were unique)",
                Statistics.getInstance().getNoOfLinesRead(),
                Statistics.getInstance().getNoOfUniqueLines()));
    }


    /**
     * Reads the input text file line by line.
     * Calls updateStatisticsWithLine() method of Statistics.java for each line.
     * Returns lines of input text file as an array list.
     *
     * @param  inputFile  a plaintext file (UTF8 encoded)
     * @return            lines of input file as array list
     */
     public static ArrayList<String> readInputFile(String inputFile) {
        Path filePath = Paths.get(inputFile);
        ArrayList<String> linesAsList = new ArrayList<String>();

        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = br.readLine()) != null) {
                Statistics.getInstance().updateStatisticsWithLine(line);
                linesAsList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return linesAsList;
    }


    /**
     * Writes data resulting from operations to a file.
     *
     * @param  outputFile  output file to write results
     * @param  lines       lines written to output file
     */
    public static void writeToOutputFile(String outputFile, ArrayList<String> lines) {
        Path path = Paths.get(outputFile);

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                writer.append(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Capitalizes the lines of input text file.
     * Returns capitalized lines as array list.
     *
     * @param  lines       lines from input text file
     */
    public static ArrayList<String> capitalize(ArrayList<String> lines){
        ArrayList<String> linesCapitalized = new ArrayList<String>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String lineCapitalized = line.toUpperCase();
            linesCapitalized.add(lineCapitalized);
        }

        return linesCapitalized;
    }


    /**
     * Reverses the lines of input text file.
     * Returns reversed lines as array list.
     *
     * @param  lines       lines from input text file
     */
    public static ArrayList<String> reverse(ArrayList<String> lines){
        ArrayList<String> linesReversed = new ArrayList<String>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            StringBuilder sb = new StringBuilder(line);
            String lineReversed = String.valueOf(sb.reverse());
            linesReversed.add(lineReversed);
        }

        return linesReversed;
    }


    /**
     * Reverses integer numbers.
     * Returns reversed integer number.
     *
     * @param  num       integer number from input txt file
     */
    public static int reverseInt(int num) {
        boolean isNegative;
        isNegative = num < 0 ? true : false;
        if (isNegative) num = -1 * num;

        int reverseNum = 0;
        int lastDigit = 0;

        while(num != 0) {
            int remainder = num % 10;
            reverseNum = reverseNum * 10 + remainder;
            num = num/10;
        }

        return isNegative == true ? -1 * reverseNum : reverseNum;
    }


    /**
     * Reverses double numbers.
     * Returns reversed double number.
     *
     * @param  num       double number from input txt file
     */
    public static double reverseDouble(double num) {
        boolean isNegative;
        isNegative = num < 0 ? true : false;
        if (isNegative) num = -1 * num;

        BigDecimal bigDecimal = new BigDecimal(String.valueOf(num));

        int numIntPart = bigDecimal.intValue();
        BigDecimal numDecimalPartBD = bigDecimal.remainder(BigDecimal.ONE);

        while (numDecimalPartBD.remainder(BigDecimal.ONE).floatValue() > 0){
            numDecimalPartBD = numDecimalPartBD.movePointRight(1);
        }
        int numDecimalPart = numDecimalPartBD.intValue();

        int intPartReversed = reverseInt(numIntPart);
        int decimalPartReversed = reverseInt(numDecimalPart);

        String reverseDoubleAsStr = "";
        reverseDoubleAsStr = reverseDoubleAsStr + String.valueOf(decimalPartReversed) + "." + String.valueOf(intPartReversed);

        double reverseDouble = Double.parseDouble(reverseDoubleAsStr);

        return isNegative == true ? -1 * reverseDouble : reverseDouble;
    }



    /**
     * Negates the lines of input text file.
     * Returns negated lines as array list.
     *
     * @param  lines       lines from input text file
     */
    public static ArrayList<String> negate(ArrayList<String> lines, String inputType){
        ArrayList<String> linesNegated = new ArrayList<String>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String lineNegated = "";
            if (isNumber(line)) {
                if (Objects.equals(inputType, "double")) {
                    double lineDouble = Double.parseDouble(line);
                    double lineNegatedDouble = -1 * lineDouble;
                    lineNegated = Double.toString(lineNegatedDouble);
                } else if (Objects.equals(inputType, "int")) {
                    int lineInt = Integer.parseInt(line);
                    int lineNegatedInt = -1 * lineInt;
                    lineNegated = Integer.toString(lineNegatedInt);
                }
                linesNegated.add(lineNegated);
            } else {
                linesNegated.add(lines.get(i));
            }
        }

        return linesNegated;
    }


    public static boolean isNumber(String string) {
        if (string == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(string);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    public static void deleteFile(String fileToDelete) {
        File file = new File(fileToDelete);
        if (file.delete()) {
            System.out.println("Output file deleted");
        } else {
            System.out.println("Failed to delete the output file");
        }
    }

}