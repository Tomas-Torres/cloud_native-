package lumina.delivery.dto;

import lumina.delivery.entity.EstadoDelivery;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryDto {
    private Long id;
    private String ordenId;
    private Long usuarioId;
    private String direccionEntrega;
    private EstadoDelivery estado;
    private String repartidorNombre;
    private LocalDateTime fechaEstimadaEntrega;
    private LocalDateTime fechaEntregaReal;
    private LocalDateTime fechaCreacion;
    private List<HistorialDeliveryDto> historial; // Lista de DTOs, no de entidades
}
