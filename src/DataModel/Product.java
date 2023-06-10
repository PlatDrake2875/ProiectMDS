package DataModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private LocalDateTime lastModified;
    private String speciality;


    /**
     * Builder class for a Product object.
     */
    private Product(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.category = builder.category;
        this.price = builder.price;
        this.productType = builder.productType;
        this.storageConditions = builder.storageConditions;
        this.weight = builder.weight;
        this.shelfLife = builder.shelfLife;
        this.ingredients = builder.ingredients;
        this.kcalPer100g = builder.kcalPer100g;
        this.kjPer100g = builder.kjPer100g;
        this.fats = builder.fats;
        this.saturatedFats = builder.saturatedFats;
        this.carbohydrates = builder.carbohydrates;
        this.sugars = builder.sugars;
        this.salt = builder.salt;
        this.fiber = builder.fiber;
        this.proteins = builder.proteins;
        this.lastModified = builder.lastModified;
        this.speciality = builder.speciality;
    }


    /**
     * Default constructor for a Product object.
     */

    public Product() {
        this.id = 0;
        this.name = null;
        this.category = null;
        this.price = BigDecimal.ZERO;
        this.productType = null;
        this.storageConditions = null;
        this.weight = BigDecimal.ZERO;
        this.shelfLife = null;
        this.ingredients = null;
        this.kcalPer100g = BigDecimal.ZERO;
        this.kjPer100g = BigDecimal.ZERO;
        this.fats = BigDecimal.ZERO;
        this.saturatedFats = BigDecimal.ZERO;
        this.carbohydrates = BigDecimal.ZERO;
        this.sugars = BigDecimal.ZERO;
        this.salt = BigDecimal.ZERO;
        this.fiber = BigDecimal.ZERO;
        this.proteins = BigDecimal.ZERO;
        this.lastModified = LocalDateTime.now();
        this.speciality = null;
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

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    /**
     * Adds the nutritional components of the given product to the current product.
     * Assumes both products have the same units of measurement.
     *
     * @param other The other product to add nutritional information from
     */
    public void addNutritionalInfo(Product other) {
        this.kcalPer100g = this.kcalPer100g.add(other.kcalPer100g);
        this.kjPer100g = this.kjPer100g.add(other.kjPer100g);
        this.fats = this.fats.add(other.fats);
        this.saturatedFats = this.saturatedFats.add(other.saturatedFats);
        this.carbohydrates = this.carbohydrates.add(other.carbohydrates);
        this.sugars = this.sugars.add(other.sugars);
        this.salt = this.salt.add(other.salt);
        this.fiber = this.fiber.add(other.fiber);
        this.proteins = this.proteins.add(other.proteins);
        if (this.weight != null && other.weight != null) {
            this.weight = this.weight.add(other.weight);
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", productType='" + productType + '\'' +
                ", storageConditions='" + storageConditions + '\'' +
                ", weight=" + weight +
                ", shelfLife='" + shelfLife + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", kcalPer100g=" + kcalPer100g +
                ", kjPer100g=" + kjPer100g +
                ", fats=" + fats +
                ", saturatedFats=" + saturatedFats +
                ", carbohydrates=" + carbohydrates +
                ", sugars=" + sugars +
                ", salt=" + salt +
                ", fiber=" + fiber +
                ", proteins=" + proteins +
                ", lastModified=" + lastModified +
                ", speciality=" + speciality +
                '}';
    }

    public static class Builder {
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
        private LocalDateTime lastModified;
        private String speciality;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder productType(String productType) {
            this.productType = productType;
            return this;
        }

        public Builder storageConditions(String storageConditions) {
            this.storageConditions = storageConditions;
            return this;
        }

        public Builder weight(BigDecimal weight) {
            this.weight = weight;
            return this;
        }

        public Builder shelfLife(String shelfLife) {
            this.shelfLife = shelfLife;
            return this;
        }

        public Builder ingredients(String ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public Builder kcalPer100g(BigDecimal kcalPer100g) {
            this.kcalPer100g = kcalPer100g;
            return this;
        }

        public Builder kjPer100g(BigDecimal kjPer100g) {
            this.kjPer100g = kjPer100g;
            return this;
        }

        public Builder fats(BigDecimal fats) {
            this.fats = fats;
            return this;
        }

        public Builder saturatedFats(BigDecimal saturatedFats) {
            this.saturatedFats = saturatedFats;
            return this;
        }

        public Builder carbohydrates(BigDecimal carbohydrates) {
            this.carbohydrates = carbohydrates;
            return this;
        }

        public Builder sugars(BigDecimal sugars) {
            this.sugars = sugars;
            return this;
        }

        public Builder salt(BigDecimal salt) {
            this.salt = salt;
            return this;
        }

        public Builder fiber(BigDecimal fiber) {
            this.fiber = fiber;
            return this;
        }

        public Builder proteins(BigDecimal proteins) {
            this.proteins = proteins;
            return this;
        }

        public Builder lastModified(LocalDateTime lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public Builder speciality(String speciality) {
            this.speciality = speciality;
            return this;
        }

        public Product build() {
            return new Product(this);
        }


    }

}

