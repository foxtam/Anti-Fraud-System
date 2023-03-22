package net.foxtam.antifraudsystem.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import net.foxtam.antifraudsystem.model.Card;
import net.foxtam.antifraudsystem.model.IP;

import javax.validation.constraints.Min;

@Getter
@ToString
public class Payment {
    @Min(1)
    private long amount;
    private final IP ip;
    private final Card card;

    @JsonCreator
    public Payment(@JsonProperty("amount") long amount,
                   @JsonProperty("ip") String ip,
                   @JsonProperty("number") String number) {
        this.amount = amount;
        this.ip = new IP(ip);
        this.card = new Card(number);
    }
}
