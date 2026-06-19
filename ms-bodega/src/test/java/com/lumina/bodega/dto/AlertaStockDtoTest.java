package com.lumina.bodega.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AlertaStockDtoTest {

    @Test
    void builder_DebeCrearAlertaStockDtoConTodosLosCampos() {
        AlertaStockDto dto = AlertaStockDto.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Laptop HP")
                .stockActual(5)
                .stockMinimo(10)
                .resuelta(false)
                .fechaCreacion(LocalDateTime.now())
                .build();

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getProductoId()).isEqualTo(100L);
        assertThat(dto.getNombreProducto()).isEqualTo("Laptop HP");
        assertThat(dto.getStockActual()).isEqualTo(5);
        assertThat(dto.getStockMinimo()).isEqualTo(10);
        assertThat(dto.getResuelta()).isFalse();
    }

    @Test
    void noArgsConstructor_DebeCrearAlertaStockDtoVacio() {
        AlertaStockDto dto = new AlertaStockDto();

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isNull();
        assertThat(dto.getProductoId()).isNull();
        assertThat(dto.getNombreProducto()).isNull();
        assertThat(dto.getStockActual()).isNull();
        assertThat(dto.getStockMinimo()).isNull();
        assertThat(dto.getResuelta()).isNull();
        assertThat(dto.getFechaCreacion()).isNull();
    }

    @Test
    void allArgsConstructor_DebeCrearAlertaStockDtoConTodosLosCampos() {
        LocalDateTime now = LocalDateTime.now();
        AlertaStockDto dto = new AlertaStockDto(
                1L,
                100L,
                "Laptop HP",
                5,
                10,
                false,
                now
        );

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getProductoId()).isEqualTo(100L);
        assertThat(dto.getNombreProducto()).isEqualTo("Laptop HP");
        assertThat(dto.getStockActual()).isEqualTo(5);
        assertThat(dto.getStockMinimo()).isEqualTo(10);
        assertThat(dto.getResuelta()).isFalse();
        assertThat(dto.getFechaCreacion()).isEqualTo(now);
    }

    @Test
    void settersAndGetters_DebenFuncionarCorrectamente() {
        AlertaStockDto dto = new AlertaStockDto();
        LocalDateTime now = LocalDateTime.now();

        dto.setId(1L);
        dto.setProductoId(100L);
        dto.setNombreProducto("Laptop HP");
        dto.setStockActual(5);
        dto.setStockMinimo(10);
        dto.setResuelta(false);
        dto.setFechaCreacion(now);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getProductoId()).isEqualTo(100L);
        assertThat(dto.getNombreProducto()).isEqualTo("Laptop HP");
        assertThat(dto.getStockActual()).isEqualTo(5);
        assertThat(dto.getStockMinimo()).isEqualTo(10);
        assertThat(dto.getResuelta()).isFalse();
        assertThat(dto.getFechaCreacion()).isEqualTo(now);
    }

    @Test
    void builder_ConResueltaTrue_DebeCrearDtoCorrectamente() {
        AlertaStockDto dto = AlertaStockDto.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Laptop HP")
                .stockActual(5)
                .stockMinimo(10)
                .resuelta(true)
                .build();

        assertThat(dto).isNotNull();
        assertThat(dto.getResuelta()).isTrue();
    }

    @Test
    void setters_ConResueltaTrue_DebeActualizarCorrectamente() {
        AlertaStockDto dto = new AlertaStockDto();
        dto.setResuelta(true);

        assertThat(dto.getResuelta()).isTrue();
    }

    @Test
    void builder_ConNombreProductoNulo_DebeCrearDtoCorrectamente() {
        AlertaStockDto dto = AlertaStockDto.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto(null)
                .stockActual(5)
                .stockMinimo(10)
                .resuelta(false)
                .build();

        assertThat(dto).isNotNull();
        assertThat(dto.getNombreProducto()).isNull();
    }
}
