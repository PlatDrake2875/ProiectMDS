package shopScraping.log;

import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.*;
import java.util.stream.Stream;

public class AppLogger {
    private static final int MAX_LOG_FILES = 3;
    private static final String LOG_DIRECTORY_PATH = "./logs";
    private static final String BACKUP_DIRECTORY_PATH = "./logs/backup";
    private final Logger logger;
    private final Class<?> className;

    public AppLogger(Class<?> className) {
        this.className = className;
        this.logger = Logger.getLogger(className.getName());
        prepareLogger();
    }

    public Logger getLogger() {
        return this.logger;
    }

    private void prepareLogger() {
        try {
            setupLogDirectory();
            limitLogFiles();
            attachFileHandler();
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setupLogDirectory() throws IOException {
        Files.createDirectories(Paths.get(LOG_DIRECTORY_PATH));
        Files.createDirectories(Paths.get(BACKUP_DIRECTORY_PATH));
    }

    private void limitLogFiles() {
        try {
            Path[] logFiles = getSortedLogFiles();
            for (int i = MAX_LOG_FILES; i < logFiles.length; i++) {
                removeHandlersForFile(logFiles[i]);
                Files.move(logFiles[i], Paths.get(BACKUP_DIRECTORY_PATH, logFiles[i].getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error while limiting log files", e);
        }
    }

    private void removeHandlersForFile(Path path) {
        List<Handler> handlersToRemove = new ArrayList<>();
        for (Handler handler : logger.getHandlers()) {
            if (handler instanceof FileHandler fileHandler) {
                try {
                    if (Files.isSameFile(Paths.get(fileHandler.getEncoding()), path)) {
                        handlersToRemove.add(fileHandler);
                    }
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Error when comparing files", e);
                }
            }
        }
        handlersToRemove.forEach(logger::removeHandler);
    }



    private Path[] getSortedLogFiles() throws IOException {
        Path logDirectory = Paths.get(LOG_DIRECTORY_PATH);
        try (Stream<Path> paths = Files.list(logDirectory)) {
            return paths
                    .filter(p -> p.toString().endsWith(".log"))
                    .sorted(Comparator.comparing(p -> -p.toFile().lastModified()))
                    .toArray(Path[]::new);
        }
    }

    private void attachFileHandler() throws IOException {
        String logFilename = getLogFilename(this.className);
        FileHandler fileHandler = new FileHandler(String.format("%s/%s", LOG_DIRECTORY_PATH, logFilename), true);
        fileHandler.setFormatter(new SimpleFormatter());

        for (Handler h : logger.getHandlers()) {
            logger.removeHandler(h);
        }
        logger.addHandler(fileHandler);
    }

    private static String getLogFilename(Class<?> clazz) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        String className = clazz.getSimpleName();
        return className + "-" + date + ".log";
    }
}
