package net.foxtam.antifraudsystem.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.foxtam.antifraudsystem.Role;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonPropertyOrder({"id", "name", "username"})
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    
    @NotBlank
    @Column(name = "name")
    private String name;
    
    @NotBlank
    @Column(name = "username")
    private String username;
    
    @NotBlank
    @Column(name = "password")
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
    
    @Column(name = "locked")
    private boolean locked = true;
    
    @JsonCreator
    public User(@JsonProperty("name") String name,
                @JsonProperty("username") String username,
                @JsonProperty("password") String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
    
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public boolean isLocked() {
        return locked;
    }
}
