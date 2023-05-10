package DataModel;

import java.math.BigDecimal;

/**
 * Represents a product in the products table.
 */
public class Product {
    private int id;
    private String name;
    private String category;
    private BigDecimal price;
    private String productType;
    private String storageConditions;
    private BigDecimal weight;
    private String shelfLife;
    private String ingredients;
    private BigDecimal kcalPer100g;
    private BigDecimal kjPer100g;
    private BigDecimal fats;
    private BigDecimal saturatedFats;
    private BigDecimal carbohydrates;
    private BigDecimal sugars;
    private BigDecimal salt;
    private BigDecimal fiber;
    private BigDecimal proteins;

    /**
     * Constructor for a Product object.
     *
     * @param id                The product ID.
     * @param name              The product name.
     * @param category          The product category.
     * @param price             The product price.
     * @param productType       The product type.
     * @param storageConditions The product storage conditions.
     * @param weight            The product weight.
     * @param shelfLife         The product shelf life.
     * @param ingredients       The product ingredients.
     * @param kcalPer100g       The product kcal per 100g.
     * @param kjPer100g         The product kj per 100g.
     * @param fats              The product fats.
     * @param saturatedFats     The product saturated fats.
     * @param carbohydrates     The product carbohydrates.
     * @param sugars            The product sugars.
     * @param salt              The product salt.
     * @param fiber             The product fiber.
     * @param proteins          The product proteins.
     */
    public Product(int id, String name, String category, BigDecimal price, String productType, String storageConditions, BigDecimal weight, String shelfLife, String ingredients, BigDecimal kcalPer100g, BigDecimal kjPer100g, BigDecimal fats, BigDecimal saturatedFats, BigDecimal carbohydrates, BigDecimal sugars, BigDecimal salt, BigDecimal fiber, BigDecimal proteins) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.productType = productType;
        this.storageConditions = storageConditions;
        this.weight = weight;
        this.shelfLife = shelfLife;
        this.ingredients = ingredients;
        this.kcalPer100g = kcalPer100g;
        this.kjPer100g = kjPer100g;
        this.fats = fats;
        this.saturatedFats = saturatedFats;
        this.carbohydrates = carbohydrates;
        this.sugars = sugars;
        this.salt = salt;
        this.fiber = fiber;
        this.proteins = proteins;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getStorageConditions() {
        return storageConditions;
    }

    public void setStorageConditions(String storageConditions) {
        this.storageConditions = storageConditions;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public BigDecimal getKcalPer100g() {
        return kcalPer100g;
    }

    public void setKcalPer100g(BigDecimal kcalPer100g) {
        this.kcalPer100g = kcalPer100g;
    }

    public BigDecimal getKjPer100g() {
        return kjPer100g;
    }

    public void setKjPer100g(BigDecimal kjPer100g) {
        this.kjPer100g = kjPer100g;
    }

    public BigDecimal getFats() {
        return fats;
    }

    public void setFats(BigDecimal fats) {
        this.fats = fats;
    }

    public BigDecimal getSaturatedFats() {
        return saturatedFats;
    }

    public void setSaturatedFats(BigDecimal saturatedFats) {
        this.saturatedFats = saturatedFats;
    }

    public BigDecimal getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(BigDecimal carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public BigDecimal getSugars() {
        return sugars;
    }

    public void setSugars(BigDecimal sugars) {
        this.sugars = sugars;
    }

    public BigDecimal getSalt() {
        return salt;
    }

    public void setSalt(BigDecimal salt) {
        this.salt = salt;
    }

    public BigDecimal getFiber() {
        return fiber;
    }

    public void setFiber(BigDecimal fiber) {
        this.fiber = fiber;
    }

    public BigDecimal getProteins() {
        return proteins;
    }

    public void setProteins(BigDecimal proteins) {
        this.proteins = proteins;
    }
}

