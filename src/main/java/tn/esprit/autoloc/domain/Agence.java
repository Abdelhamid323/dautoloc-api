package tn.esprit.autoloc.domain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
@Entity
@Table(name = "agence")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Agence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgence;
    @Column(nullable = false, length = 20)
    private String nom;
    @Column(nullable = false, length = 50)
    private String ville;
    @Column(nullable = false, length = 50)
    private String adresse;

    @Column(nullable = false, precision = 8)
    private BigDecimal telephone;

}
