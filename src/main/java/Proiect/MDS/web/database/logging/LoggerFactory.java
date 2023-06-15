package Proiect.MDS.web.database.logging;

import Proiect.MDS.web.shopScraping.AppLogger;

public class LoggerFactory {

    public enum LoggerType {
        DATABASE,
        PRODUCT,
        RECIPE
    }

    public static AppLogger createLogger(LoggerType type, Class<?> className) {
        return switch (type) {
            case DATABASE -> new DatabaseLogger(className);
            case PRODUCT -> new ProductLogger(className);
            case RECIPE -> new RecipeLogger(className);
            default -> throw new IllegalArgumentException("Invalid logger type");
        };
    }
}
