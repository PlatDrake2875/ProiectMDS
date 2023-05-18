package DataModel;

import DataModel.Product;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * The ProductStatistics class provides statistical information
 * about a collection of Product objects.
 */
public class ProductStatistics {

    private List<Product> products;

    /**
     * Constructs a new ProductStatistics object with the given list of products.
     *
     * @param products the list of products to analyze
     */
    public ProductStatistics(List<Product> products) {
        this.products = products;
    }

    /**
     * Returns the product with the lowest price.
     *
     * @return the product with the lowest price
     */
    public Product lowestPriceProduct() {
        return products.stream().min(Comparator.comparing(Product::getPrice)).orElse(null);
    }

    /**
     * Returns the product with the highest price.
     *
     * @return the product with the highest price
     */
    public Product highestPriceProduct() {
        return products.stream().max(Comparator.comparing(Product::getPrice)).orElse(null);
    }

    /**
     * Returns the product with the highest calorie content per 100g.
     *
     * @return the product with the highest calorie content per 100g
     */
    public Product highestCalorieProduct() {
        return products.stream().max(Comparator.comparing(Product::getKcalPer100g)).orElse(null);
    }

    /**
     * Returns the product with the lowest calorie content per 100g.
     *
     * @return the product with the lowest calorie content per 100g
     */
    public Product lowestCalorieProduct() {
        return products.stream().min(Comparator.comparing(Product::getKcalPer100g)).orElse(null);
    }

    /**
     * Returns the product with the highest protein content per 100g.
     *
     * @return the product with the highest protein content per 100g
     */
    public Product highestProteinProduct() {
        return products.stream().max(Comparator.comparing(Product::getProteins)).orElse(null);
    }

    /**
     * Returns the median value of calories per 100g for all products.
     *
     * @return the median value of calories per 100g
     */
    public BigDecimal medianCalories() {
        List<Product> sortedProducts = products.stream()
                .sorted(Comparator.comparing(Product::getKcalPer100g))
                .toList();
        int n = sortedProducts.size();
        if (n % 2 == 0) {
            return sortedProducts.get(n / 2).getKcalPer100g().add(sortedProducts.get(n / 2 - 1).getKcalPer100g()).divide(BigDecimal.valueOf(2));
        } else {
            return sortedProducts.get(n / 2).getKcalPer100g();
        }
    }

    /**
     * Returns the product with the highest quantity (weight) to price ratio.
     *
     * @return the product with the highest quantity to price ratio
     */
    public Product highestQuantityToPriceRatio() {
        return products.stream()
                .max(Comparator.comparing(p -> p.getWeight().divide(p.getPrice())))
                .orElse(null);
    }

    /**
     * Returns the average price for all products.
     *
     * @return the average price for all products
     */
    public BigDecimal averagePrice() {
        return products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(products.size()), BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Returns the average protein content per 100g for all products.
     *
     * @return the average protein content per 100g
     */
    public BigDecimal averageProteinContent() {
        return products.stream()
                .map(Product::getProteins)
                .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(products.size()), BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Returns the median value of proteins per 100g for all products.
     *
     * @return the median value of proteins per 100g
     */
    public BigDecimal medianProtein() {
        List<Product> sortedProducts = products.stream()
                .sorted(Comparator.comparing(Product::getProteins))
                .toList();
        int n = sortedProducts.size();
        if (n % 2 == 0) {
            return sortedProducts.get(n / 2).getProteins().add(sortedProducts.get(n / 2 - 1).getProteins()).divide(BigDecimal.valueOf(2));
        } else {
            return sortedProducts.get(n / 2).getProteins();
        }
    }

    /**
     * Returns the median value of sugars per 100g for all products.
     *
     * @return the median value of sugars per 100g
     */
    public BigDecimal medianSugars() {
        List<Product> sortedProducts = products.stream()
                .sorted(Comparator.comparing(Product::getSugars))
                .toList();
        int n = sortedProducts.size();
        if (n % 2 == 0) {
            return sortedProducts.get(n / 2).getSugars().add(sortedProducts.get(n / 2 - 1).getSugars()).divide(BigDecimal.valueOf(2));
        } else {
            return sortedProducts.get(n / 2).getSugars();
        }
    }

    /**
     * Returns the median value of fats per 100g for all products.
     *
     * @return the median value of fats per 100g
     */
    public BigDecimal medianFats() {
        List<Product> sortedProducts = products.stream()
                .sorted(Comparator.comparing(Product::getFats))
                .toList();
        int n = sortedProducts.size();
        if (n % 2 == 0) {
            return sortedProducts.get(n / 2).getFats().add(sortedProducts.get(n / 2 - 1).getFats()).divide(BigDecimal.valueOf(2));
        } else {
            return sortedProducts.get(n / 2).getFats();
        }
    }

}
