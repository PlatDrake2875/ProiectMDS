package DataModel;

import java.util.UUID;

public class Ingredient {
    private UUID id;
    private String name;
    private Double quantity;

    private String unitOfMeasurement;

    public Ingredient (String name, Double quantity, String unitOfMeasurement) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.quantity = quantity;
        this.unitOfMeasurement = unitOfMeasurement;
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

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }
}
