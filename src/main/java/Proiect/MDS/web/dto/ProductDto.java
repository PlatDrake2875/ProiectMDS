package Proiect.MDS.web.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * Data Transfer Object for transferring Product data between different parts of the application.
 */
@Builder
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    /**
     * The unique ID of the Product.
     */
    private int id;

    /**
     * The name of the Product.
     */
    private String name;

    /**
     * The price of the Product.
     */
    private BigDecimal price;
}
