package com.lumina.bodega.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class InventarioTest {

    private Inventario inventario;

    @BeforeEach
    void setUp() {
        inventario = Inventario.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Producto Test")
                .stock(50)
                .stockMinimo(10)
                .ubicacionBodega("A1")
                .build();
    }

    @Test
    void isStockCritico_DebeRetornarTrue_CuandoStockEsIgualAlMinimo() {
        inventario.setStock(10);
        inventario.setStockMinimo(10);

        boolean resultado = inventario.isStockCritico();

        assertThat(resultado).isTrue();
    }

    @Test
    void isStockCritico_DebeRetornarTrue_CuandoStockEsMenorAlMinimo() {
        inventario.setStock(5);
        inventario.setStockMinimo(10);

        boolean resultado = inventario.isStockCritico();

        assertThat(resultado).isTrue();
    }

    @Test
    void isStockCritico_DebeRetornarFalse_CuandoStockEsMayorAlMinimo() {
        inventario.setStock(50);
        inventario.setStockMinimo(10);

        boolean resultado = inventario.isStockCritico();

        assertThat(resultado).isFalse();
    }

    @Test
    void onUpdate_DebeEstablecerFechaActualizacion_CuandoSeLlama() {
        inventario.onUpdate();

        assertThat(inventario.getFechaActualizacion()).isNotNull();
        assertThat(inventario.getFechaActualizacion()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void onUpdate_DebeEstablecerStockMinimoDefault_CuandoEsNull() {
        inventario.setStockMinimo(null);

        inventario.onUpdate();

        assertThat(inventario.getStockMinimo()).isEqualTo(10);
    }

    @Test
    void onUpdate_NoDebeModificarStockMinimo_CuandoYaTieneValor() {
        inventario.setStockMinimo(15);

        inventario.onUpdate();

        assertThat(inventario.getStockMinimo()).isEqualTo(15);
    }

    @Test
    void builder_DebeCrearInventarioConTodosLosCampos() {
        Inventario inventarioConstruido = Inventario.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Producto Test")
                .stock(50)
                .stockMinimo(10)
                .ubicacionBodega("A1")
                .fechaActualizacion(LocalDateTime.now())
                .build();

        assertThat(inventarioConstruido.getId()).isEqualTo(1L);
        assertThat(inventarioConstruido.getProductoId()).isEqualTo(100L);
        assertThat(inventarioConstruido.getNombreProducto()).isEqualTo("Producto Test");
        assertThat(inventarioConstruido.getStock()).isEqualTo(50);
        assertThat(inventarioConstruido.getStockMinimo()).isEqualTo(10);
        assertThat(inventarioConstruido.getUbicacionBodega()).isEqualTo("A1");
        assertThat(inventarioConstruido.getFechaActualizacion()).isNotNull();
    }

    @Test
    void settersAndGetters_DebenFuncionarCorrectamente() {
        inventario.setId(2L);
        inventario.setProductoId(200L);
        inventario.setNombreProducto("Producto Actualizado");
        inventario.setStock(100);
        inventario.setStockMinimo(20);
        inventario.setUbicacionBodega("B2");
        LocalDateTime fecha = LocalDateTime.now();
        inventario.setFechaActualizacion(fecha);

        assertThat(inventario.getId()).isEqualTo(2L);
        assertThat(inventario.getProductoId()).isEqualTo(200L);
        assertThat(inventario.getNombreProducto()).isEqualTo("Producto Actualizado");
        assertThat(inventario.getStock()).isEqualTo(100);
        assertThat(inventario.getStockMinimo()).isEqualTo(20);
        assertThat(inventario.getUbicacionBodega()).isEqualTo("B2");
        assertThat(inventario.getFechaActualizacion()).isEqualTo(fecha);
    }
}
