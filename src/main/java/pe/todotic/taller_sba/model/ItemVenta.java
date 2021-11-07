package pe.todotic.taller_sba.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iditem_venta")
    private Integer id;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "num_desc_disp")
    private Integer numeroDescargasDisponibles;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idventa",referencedColumnName = "idventa")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "idlibro",referencedColumnName = "idlibro")
    private Libro libro;

    public Integer consumirDescargasDisponbiles(){
         return  --numeroDescargasDisponibles;
    }
}
