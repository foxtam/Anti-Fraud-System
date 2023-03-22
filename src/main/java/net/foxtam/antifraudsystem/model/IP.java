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
@ToString
@NoArgsConstructor
@Entity
@Table(name = "ips")
public class IP {
    private static final String PATTERN = """
            (25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\
            \\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\
            \\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\
            \\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)""";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

//    @Pattern(regexp = PATTERN)
    @Column(name = "ip")
    private String ip;

    @JsonCreator
    public IP(@JsonProperty("ip") String ip) {
        if (!isIPCorrect(ip)) throw new WrongFormatException(ip);
        this.ip = ip;
    }

    private static boolean isIPCorrect(String ip) {
        String[] octets = ip.split("\\.");
        if (octets.length != 4) return false;
        for (String octet : octets) {
            try {
                int n = Integer.parseInt(octet);
                if (n < 0 || n > 255) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}
