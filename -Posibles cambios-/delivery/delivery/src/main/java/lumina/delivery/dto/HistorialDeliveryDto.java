package lumina.delivery.dto;

import lumina.delivery.entity.EstadoDelivery;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialDeliveryDto {
    private EstadoDelivery estado;
    private String descripcion;
    private LocalDateTime fecha;
}
