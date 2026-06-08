package lumina.usuario.dto;

import lumina.usuario.entity.Rol;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDto {
    private String nombre;
    private String email;
    private String run;
    private String direccion;
    private Rol rol;
    private LocalDateTime fechaCreacion;
    private Boolean activo;
}
