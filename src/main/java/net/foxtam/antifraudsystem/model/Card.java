package net.foxtam.antifraudsystem.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.foxtam.antifraudsystem.exceptions.WrongFormatException;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "number")
    private String number;

    @JsonCreator
    public Card(@JsonProperty("number") String number) {
        if (!isCorrectLuhn(number)) throw new WrongFormatException(number);
        this.number = number;
    }

    private static boolean isCorrectLuhn(String number) {
        int nDigits = number.length();
        if (nDigits != 16) return false;
        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {
            int d = number.charAt(i) - '0';
            if (isSecond) d = d * 2;
            nSum += d / 10;
            nSum += d % 10;
            isSecond = !isSecond;
        }
        return nSum % 10 == 0;
    }
}
