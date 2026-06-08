package com.lumina.bodega.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AlertaStockTest {

    private AlertaStock alertaStock;

    @BeforeEach
    void setUp() {
        alertaStock = AlertaStock.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Producto Test")
                .stockActual(5)
                .stockMinimo(10)
                .resuelta(false)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    @Test
    void onCreate_DebeEstablecerFechaCreacion_CuandoSeLlama() {
        alertaStock.setFechaCreacion(null);

        alertaStock.onCreate();

        assertThat(alertaStock.getFechaCreacion()).isNotNull();
        assertThat(alertaStock.getFechaCreacion()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void onCreate_DebeEstablecerResueltaDefault_CuandoEsNull() {
        alertaStock.setResuelta(null);

        alertaStock.onCreate();

        assertThat(alertaStock.getResuelta()).isFalse();
    }

    @Test
    void onCreate_NoDebeModificarResuelta_CuandoYaTieneValor() {
        alertaStock.setResuelta(true);

        alertaStock.onCreate();

        assertThat(alertaStock.getResuelta()).isTrue();
    }

    @Test
    void builder_DebeCrearAlertaStockConTodosLosCampos() {
        AlertaStock alertaConstruida = AlertaStock.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Producto Test")
                .stockActual(5)
                .stockMinimo(10)
                .resuelta(false)
                .fechaCreacion(LocalDateTime.now())
                .build();

        assertThat(alertaConstruida.getId()).isEqualTo(1L);
        assertThat(alertaConstruida.getProductoId()).isEqualTo(100L);
        assertThat(alertaConstruida.getNombreProducto()).isEqualTo("Producto Test");
        assertThat(alertaConstruida.getStockActual()).isEqualTo(5);
        assertThat(alertaConstruida.getStockMinimo()).isEqualTo(10);
        assertThat(alertaConstruida.getResuelta()).isFalse();
        assertThat(alertaConstruida.getFechaCreacion()).isNotNull();
    }

    @Test
    void settersAndGetters_DebenFuncionarCorrectamente() {
        alertaStock.setId(2L);
        alertaStock.setProductoId(200L);
        alertaStock.setNombreProducto("Producto Actualizado");
        alertaStock.setStockActual(3);
        alertaStock.setStockMinimo(15);
        alertaStock.setResuelta(true);
        LocalDateTime fecha = LocalDateTime.now();
        alertaStock.setFechaCreacion(fecha);

        assertThat(alertaStock.getId()).isEqualTo(2L);
        assertThat(alertaStock.getProductoId()).isEqualTo(200L);
        assertThat(alertaStock.getNombreProducto()).isEqualTo("Producto Actualizado");
        assertThat(alertaStock.getStockActual()).isEqualTo(3);
        assertThat(alertaStock.getStockMinimo()).isEqualTo(15);
        assertThat(alertaStock.getResuelta()).isTrue();
        assertThat(alertaStock.getFechaCreacion()).isEqualTo(fecha);
    }

    @Test
    void constructorSinArgumentos_DebeCrearInstanciaVacia() {
        AlertaStock alertaVacia = new AlertaStock();

        assertThat(alertaVacia).isNotNull();
        assertThat(alertaVacia.getId()).isNull();
        assertThat(alertaVacia.getProductoId()).isNull();
        assertThat(alertaVacia.getNombreProducto()).isNull();
        assertThat(alertaVacia.getStockActual()).isNull();
        assertThat(alertaVacia.getStockMinimo()).isNull();
        assertThat(alertaVacia.getResuelta()).isNull();
        assertThat(alertaVacia.getFechaCreacion()).isNull();
    }

    @Test
    void constructorConTodosLosArgumentos_DebeCrearInstanciaCompleta() {
        LocalDateTime fecha = LocalDateTime.now();
        AlertaStock alertaCompleta = new AlertaStock(
                1L,
                100L,
                "Producto Test",
                5,
                10,
                false,
                fecha
        );

        assertThat(alertaCompleta.getId()).isEqualTo(1L);
        assertThat(alertaCompleta.getProductoId()).isEqualTo(100L);
        assertThat(alertaCompleta.getNombreProducto()).isEqualTo("Producto Test");
        assertThat(alertaCompleta.getStockActual()).isEqualTo(5);
        assertThat(alertaCompleta.getStockMinimo()).isEqualTo(10);
        assertThat(alertaCompleta.getResuelta()).isFalse();
        assertThat(alertaCompleta.getFechaCreacion()).isEqualTo(fecha);
    }
}
