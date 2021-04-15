package uz.pdp.apisecurityhrmanagement.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class SalaryDTO {

    private UUID UserId;

    private double amount;

    private String month;

    private boolean isPaid;

    private Integer year;
}
