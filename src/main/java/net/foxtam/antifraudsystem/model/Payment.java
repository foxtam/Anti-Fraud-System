package net.foxtam.antifraudsystem.model;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class Payment {
    @Min(1)
    private long amount;
}
