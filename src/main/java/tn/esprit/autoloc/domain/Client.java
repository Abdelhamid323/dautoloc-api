package tn.esprit.autoloc.domain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClient;
    @Column(nullable = false, length = 20)
    private String nom;
    @Column(nullable = false, length = 20)
    private String prenom;
    @Column(nullable = false, length = 50)
    private String email;
    @Column(nullable = false, precision = 8)
    private Number telephone;
    @Column(nullable = false, precision = 10)
    private Number numPermis;
    @Column(nullable = false)
    private Date dateInscription;

}
