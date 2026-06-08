package com.lumina.bodega.repository;

import com.lumina.bodega.entity.Inventario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class InventarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InventarioRepository inventarioRepository;

    private Inventario inventario1;
    private Inventario inventario2;
    private Inventario inventarioBajoStock;

    @BeforeEach
    void setUp() {
        inventario1 = Inventario.builder()
                .productoId(100L)
                .nombreProducto("Producto A")
                .stock(50)
                .stockMinimo(10)
                .ubicacionBodega("A1")
                .build();

        inventario2 = Inventario.builder()
                .productoId(200L)
                .nombreProducto("Producto B")
                .stock(30)
                .stockMinimo(15)
                .ubicacionBodega("B2")
                .build();

        inventarioBajoStock = Inventario.builder()
                .productoId(300L)
                .nombreProducto("Producto C")
                .stock(5)
                .stockMinimo(10)
                .ubicacionBodega("C3")
                .build();
    }

    @Test
    void findByProductoId_DebeRetornarOptionalConInventario_CuandoExiste() {
        entityManager.persist(inventario1);
        entityManager.flush();

        Optional<Inventario> resultado = inventarioRepository.findByProductoId(100L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getProductoId()).isEqualTo(100L);
        assertThat(resultado.get().getNombreProducto()).isEqualTo("Producto A");
    }

    @Test
    void findByProductoId_DebeRetornarOptionalVacio_CuandoNoExiste() {
        Optional<Inventario> resultado = inventarioRepository.findByProductoId(999L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void findByStockLessThanEqual_DebeRetornarInventariosConStockBajo_CuandoExisten() {
        entityManager.persist(inventario1);
        entityManager.persist(inventario2);
        entityManager.persist(inventarioBajoStock);
        entityManager.flush();

        List<Inventario> resultado = inventarioRepository.findByStockLessThanEqual(10);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getProductoId()).isEqualTo(300L);
        assertThat(resultado.get(0).getStock()).isEqualTo(5);
    }

    @Test
    void findByStockLessThanEqual_DebeRetornarListaVacia_CuandoNoHayStockBajo() {
        entityManager.persist(inventario1);
        entityManager.persist(inventario2);
        entityManager.flush();

        List<Inventario> resultado = inventarioRepository.findByStockLessThanEqual(5);

        assertThat(resultado).isEmpty();
    }

    @Test
    void findAll_DebeRetornarTodosLosInventarios() {
        entityManager.persist(inventario1);
        entityManager.persist(inventario2);
        entityManager.flush();

        List<Inventario> resultado = inventarioRepository.findAll();

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(Inventario::getProductoId)
                .containsExactlyInAnyOrder(100L, 200L);
    }

    @Test
    void save_DebePersistirInventario_CuandoEsNuevo() {
        Inventario guardado = inventarioRepository.save(inventario1);

        assertThat(guardado).isNotNull();
        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getProductoId()).isEqualTo(100L);
        assertThat(guardado.getFechaActualizacion()).isNotNull();
    }

    @Test
    void save_DebeActualizarInventario_CuandoYaExiste() {
        Inventario persistido = entityManager.persist(inventario1);
        entityManager.flush();

        persistido.setStock(100);
        persistido.setNombreProducto("Producto A Actualizado");

        Inventario actualizado = inventarioRepository.save(persistido);

        assertThat(actualizado.getStock()).isEqualTo(100);
        assertThat(actualizado.getNombreProducto()).isEqualTo("Producto A Actualizado");
    }

    @Test
    void deleteById_DebeEliminarInventario_CuandoExiste() {
        Inventario persistido = entityManager.persist(inventario1);
        entityManager.flush();

        inventarioRepository.deleteById(persistido.getId());

        Optional<Inventario> resultado = inventarioRepository.findById(persistido.getId());
        assertThat(resultado).isEmpty();
    }

    @Test
    void count_DebeRetornarCantidadCorrecta_DeInventarios() {
        entityManager.persist(inventario1);
        entityManager.persist(inventario2);
        entityManager.flush();

        long cantidad = inventarioRepository.count();

        assertThat(cantidad).isEqualTo(2);
    }
}
