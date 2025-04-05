import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileReader {
    private static final String INPUT_FILE = "input.txt";
    private Map<String, String> values;

    public FileReader() {
        values = new HashMap<>();
        readFile();
    }

    private void readFile() {
        try {
            File file = new File(INPUT_FILE);
            Scanner scanner = new Scanner(file);
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    values.put(key, value);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Không tìm thấy file " + INPUT_FILE);
            e.printStackTrace();
        }
    }

    public String getValue(String key) {
        return values.get(key);
    }
} 