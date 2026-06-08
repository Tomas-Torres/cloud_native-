package lumina.bff.controller;

import lumina.bff.config.MicroservicesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryProxyController {

    private final WebClient.Builder webClientBuilder;
    private final MicroservicesConfig config;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<String>> obtenerDelivery(@PathVariable Long id) {
        return webClientBuilder.build()
                .get()
                .uri(config.getDeliveryUrl() + "/api/delivery/" + id)
                .retrieve()
                .toEntity(String.class);
    }

    @GetMapping("/orden/{ordenId}")
    public Mono<ResponseEntity<String>> obtenerDeliveryPorOrden(@PathVariable String ordenId) {
        return webClientBuilder.build()
                .get()
                .uri(config.getDeliveryUrl() + "/api/delivery/orden/" + ordenId)
                .retrieve()
                .toEntity(String.class);
    }

    @PatchMapping("/{id}/estado")
    public Mono<ResponseEntity<String>> actualizarEstado(@PathVariable Long id, @RequestBody String body) {
        return webClientBuilder.build()
                .patch()
                .uri(config.getDeliveryUrl() + "/api/delivery/" + id + "/estado")
                .bodyValue(body)
                .header("Content-Type", "application/json")
                .retrieve()
                .toEntity(String.class);
    }
}
