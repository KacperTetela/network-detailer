package networkdetailer.com.model.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class UndefinedHostReadWrite {

    private static final String FILE_PATH = "undefined.txt";

    public static int readNumberFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            log.warn("Error reading file. Default value to 0");
        }
        return 0;
    }

    private static void saveNumberToFile(int number) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(Integer.toString(number));
        } catch (IOException e) {
            log.warn("Error writing to file: " + e.getMessage());
        }
    }

    public static void incrementNumber() {
        int number = readNumberFromFile();
        saveNumberToFile(number + 1);
    }

}