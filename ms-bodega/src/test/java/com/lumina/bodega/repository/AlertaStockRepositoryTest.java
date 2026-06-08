package com.lumina.bodega.repository;

import com.lumina.bodega.entity.AlertaStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AlertaStockRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AlertaStockRepository alertaStockRepository;

    private AlertaStock alerta1;
    private AlertaStock alerta2;
    private AlertaStock alertaResuelta;

    @BeforeEach
    void setUp() {
        LocalDateTime fecha1 = LocalDateTime.now().minusHours(2);
        LocalDateTime fecha2 = LocalDateTime.now().minusHours(1);

        alerta1 = AlertaStock.builder()
                .productoId(100L)
                .nombreProducto("Producto A")
                .stockActual(5)
                .stockMinimo(10)
                .resuelta(false)
                .fechaCreacion(fecha1)
                .build();

        alerta2 = AlertaStock.builder()
                .productoId(200L)
                .nombreProducto("Producto B")
                .stockActual(3)
                .stockMinimo(15)
                .resuelta(false)
                .fechaCreacion(fecha2)
                .build();

        alertaResuelta = AlertaStock.builder()
                .productoId(300L)
                .nombreProducto("Producto C")
                .stockActual(2)
                .stockMinimo(10)
                .resuelta(true)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    @Test
    void findByResueltaFalseOrderByFechaCreacionDesc_DebeRetornarAlertasActivasOrdenadas() {
        entityManager.persist(alerta1);
        entityManager.persist(alerta2);
        entityManager.persist(alertaResuelta);
        entityManager.flush();

        List<AlertaStock> resultado = alertaStockRepository.findByResueltaFalseOrderByFechaCreacionDesc();

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(AlertaStock::getResuelta)
                .containsOnly(false);
        assertThat(resultado).extracting(AlertaStock::getProductoId)
                .containsExactly(200L, 100L);
    }

    @Test
    void findByResueltaFalseOrderByFechaCreacionDesc_DebeRetornarListaVacia_CuandoNoHayAlertasActivas() {
        entityManager.persist(alertaResuelta);
        entityManager.flush();

        List<AlertaStock> resultado = alertaStockRepository.findByResueltaFalseOrderByFechaCreacionDesc();

        assertThat(resultado).isEmpty();
    }

    @Test
    void findByProductoIdAndResueltaFalse_DebeRetornarAlertasDelProducto_CuandoExisten() {
        entityManager.persist(alerta1);
        entityManager.persist(alerta2);
        entityManager.flush();

        List<AlertaStock> resultado = alertaStockRepository.findByProductoIdAndResueltaFalse(100L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getProductoId()).isEqualTo(100L);
        assertThat(resultado.get(0).getResuelta()).isFalse();
    }

    @Test
    void findByProductoIdAndResueltaFalse_DebeRetornarListaVacia_CuandoNoHayAlertasDelProducto() {
        entityManager.persist(alerta1);
        entityManager.flush();

        List<AlertaStock> resultado = alertaStockRepository.findByProductoIdAndResueltaFalse(999L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void findByProductoIdAndResueltaFalse_DebeRetornarListaVacia_CuandoAlertaEstaResuelta() {
        entityManager.persist(alertaResuelta);
        entityManager.flush();

        List<AlertaStock> resultado = alertaStockRepository.findByProductoIdAndResueltaFalse(300L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void findAll_DebeRetornarTodasLasAlertas() {
        entityManager.persist(alerta1);
        entityManager.persist(alerta2);
        entityManager.persist(alertaResuelta);
        entityManager.flush();

        List<AlertaStock> resultado = alertaStockRepository.findAll();

        assertThat(resultado).hasSize(3);
        assertThat(resultado).extracting(AlertaStock::getProductoId)
                .containsExactlyInAnyOrder(100L, 200L, 300L);
    }

    @Test
    void save_DebePersistirAlerta_CuandoEsNueva() {
        AlertaStock guardada = alertaStockRepository.save(alerta1);

        assertThat(guardada).isNotNull();
        assertThat(guardada.getId()).isNotNull();
        assertThat(guardada.getProductoId()).isEqualTo(100L);
        assertThat(guardada.getFechaCreacion()).isNotNull();
        assertThat(guardada.getResuelta()).isFalse();
    }

    @Test
    void save_DebeActualizarAlerta_CuandoYaExiste() {
        AlertaStock persistida = entityManager.persist(alerta1);
        entityManager.flush();

        persistida.setStockActual(20);
        persistida.setResuelta(true);

        AlertaStock actualizada = alertaStockRepository.save(persistida);

        assertThat(actualizada.getStockActual()).isEqualTo(20);
        assertThat(actualizada.getResuelta()).isTrue();
    }

    @Test
    void deleteById_DebeEliminarAlerta_CuandoExiste() {
        AlertaStock persistida = entityManager.persist(alerta1);
        entityManager.flush();

        alertaStockRepository.deleteById(persistida.getId());

        var resultado = alertaStockRepository.findById(persistida.getId());
        assertThat(resultado).isEmpty();
    }

    @Test
    void count_DebeRetornarCantidadCorrecta_DeAlertas() {
        entityManager.persist(alerta1);
        entityManager.persist(alerta2);
        entityManager.persist(alertaResuelta);
        entityManager.flush();

        long cantidad = alertaStockRepository.count();

        assertThat(cantidad).isEqualTo(3);
    }
}
