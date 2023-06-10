package shopScraping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.*;
import java.util.stream.Stream;

/**
 * Class responsible for logging.
 */
public class AppLogger {

    private static final int MAX_LOG_FILES = 3;
    private static final String LOG_DIRECTORY_PATH = "./logs";
    private final Logger logger;
    private final Class<?> className;

    /**
     * Initializes a new AppLogger.
     *
     * @param className The name of the class that the logger is being created for.
     */
    public AppLogger(Class<?> className) {
        this.className = className;
        this.logger = Logger.getLogger(className.getName());
        prepareLogger();
    }

    /**
     * Returns the logger.
     *
     * @return The logger.
     */
    public Logger getLogger() {
        return this.logger;
    }

    /**
     * Prepares the logger by setting up the log directory, limiting the number
     * of log files and attaching the file handler.
     */
    private void prepareLogger() {
        try {
            setupLogDirectory();
            limitLogFiles();
            attachFileHandler();
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the log directory.
     *
     * @throws IOException if an I/O error occurs
     */
    private void setupLogDirectory() throws IOException {
        Files.createDirectories(Paths.get(LOG_DIRECTORY_PATH)); // creates the directory if it doesn't exist
    }

    /**
     * Limits the number of log files in the log directory to MAX_LOG_FILES.
     */
    private void limitLogFiles() {
        try {
            var logFiles = getSortedLogFiles();

            for (int i = MAX_LOG_FILES; i < logFiles.length; i++) {
                Files.delete(logFiles[i]);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error while limiting log files", e);
        }
    }

    /**
     * Method to fetch and sort the log files.
     *
     * @return An array of sorted Path objects representing the log files.
     * @throws IOException if an I/O error occurs
     */
    private Path[] getSortedLogFiles() throws IOException {
        // Define the directory where the log files are stored
        Path logDirectory = Paths.get(LOG_DIRECTORY_PATH);

        // Use try-with-resources to ensure that the Stream is closed automatically, to prevent potential memory leaks.
        // Here, Files.list(logDirectory) returns a Stream object representing the list of files in the directory.
        try (Stream<Path> paths = Files.list(logDirectory)) {
            // Filter the files to include only the ones that end with ".log"
            // Then, sort them in descending order of their last modified time
            // Finally, convert the Stream to an array
            return paths
                    .filter(p -> p.toString().endsWith(".log"))
                    .sorted(Comparator.comparing(p -> -p.toFile().lastModified()))
                    .toArray(Path[]::new);
        }
    }
    /**
     * Attaches a file handler to the logger.
     *
     * @throws IOException if an I/O error occurs
     */
    private void attachFileHandler() throws IOException {
        // Use getLogFilename() method to generate the log filename
        String logFilename = getLogFilename(this.className);
        FileHandler fileHandler = new FileHandler(String.format("%s/%s", LOG_DIRECTORY_PATH, logFilename), true);

        fileHandler.setFormatter(new SimpleFormatter());

        for (Handler h : logger.getHandlers()) {
            logger.removeHandler(h);
        }

        logger.addHandler(fileHandler);
    }

    /**
     * Generates a filename for the log file using the current date and the class name.
     *
     * @param clazz Class for which the log is being generated.
     * @return A string representing the filename for the log file.
     */
    private static String getLogFilename(Class<?> clazz) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        String className = clazz.getSimpleName();
        return className + "-" + date + ".log";
    }
}

