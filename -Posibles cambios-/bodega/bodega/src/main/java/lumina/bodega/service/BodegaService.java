package lumina.bodega.service;

import lumina.bodega.entity.AlertaStock;
import lumina.bodega.entity.Inventario;
import lumina.bodega.repository.AlertaStockRepository;
import lumina.bodega.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BodegaService {

    private final InventarioRepository inventarioRepository;
    private final AlertaStockRepository alertaStockRepository;

    public List<Inventario> listarInventario() {
        return inventarioRepository.findAll();
    }

    public Inventario obtenerStock(Long productoId) {
        return inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException("No hay registro de inventario para producto: " + productoId));
    }

    @Transactional
    public Inventario actualizarStock(Long productoId, Integer cantidad) {
        Inventario inventario = obtenerStock(productoId);
        inventario.setStock(inventario.getStock() + cantidad);

        if (inventario.isStockCritico()) {
            generarAlerta(inventario);
        }

        return inventarioRepository.save(inventario);
    }

    @Transactional
    public Inventario descontarStock(Long productoId, Integer cantidad) {
        Inventario inventario = obtenerStock(productoId);

        if (inventario.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente para producto: " + productoId);
        }

        inventario.setStock(inventario.getStock() - cantidad);

        if (inventario.isStockCritico()) {
            generarAlerta(inventario);
        }

        return inventarioRepository.save(inventario);
    }

    public List<AlertaStock> obtenerAlertas() {
        return alertaStockRepository.findByResueltaFalseOrderByFechaCreacionDesc();
    }

    private void generarAlerta(Inventario inventario) {
        AlertaStock alerta = AlertaStock.builder()
                .productoId(inventario.getProductoId())
                .nombreProducto(inventario.getNombreProducto())
                .stockActual(inventario.getStock())
                .stockMinimo(inventario.getStockMinimo())
                .build();
        alertaStockRepository.save(alerta);
        log.warn("ALERTA: Stock crítico para producto {} - Stock actual: {}, Mínimo: {}",
                inventario.getNombreProducto(), inventario.getStock(), inventario.getStockMinimo());
    }
}
