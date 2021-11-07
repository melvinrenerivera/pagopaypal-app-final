package pe.todotic.taller_sba.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idventa")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idusuario",referencedColumnName = "idusuario")
    private Usuario usuario;

    private LocalDateTime fecha;

    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    public enum Estado{
        CREADO,
        COMPLETADO
    }

    @OneToMany(mappedBy = "venta",cascade = CascadeType.ALL)
    private List<ItemVenta> items;
}
