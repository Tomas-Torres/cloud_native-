import com.lumina.bff.config.MicroservicesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bodega")
@RequiredArgsConstructor
public class BodegaProxyController {

    private final WebClient.Builder webClientBuilder;
    private final MicroservicesConfig config;

    @GetMapping("/inventario")
    public Mono<ResponseEntity<String>> listarInventario() {
        return webClientBuilder.build()
                .get()
                .uri(config.getBodegaUrl() + "/api/bodega/inventario")
                .retrieve()
                .toEntity(String.class);
    }

    @GetMapping("/inventario/{productoId}")
    public Mono<ResponseEntity<String>> obtenerStock(@PathVariable Long productoId) {
        return webClientBuilder.build()
                .get()
                .uri(config.getBodegaUrl() + "/api/bodega/inventario/" + productoId)
                .retrieve()
                .toEntity(String.class);
    }

    @GetMapping("/alertas")
    public Mono<ResponseEntity<String>> obtenerAlertas() {
        return webClientBuilder.build()
                .get()
                .uri(config.getBodegaUrl() + "/api/bodega/alertas")
                .retrieve()
                .toEntity(String.class);
    }
}