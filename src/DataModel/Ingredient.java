package DataModel;

import java.util.UUID;

public class Ingredient {
    private UUID id;
    private String name;
    private Double quantity;

    public Ingredient (String name, Double quantity) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
