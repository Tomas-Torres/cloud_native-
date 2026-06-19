package com.lumina.bodega.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class InventarioDtoTest {

    @Test
    void builder_DebeCrearInventarioDtoConTodosLosCampos() {
        InventarioDto dto = InventarioDto.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Laptop HP")
                .stock(50)
                .stockMinimo(10)
                .ubicacionBodega("Bodega A")
                .fechaActualizacion(LocalDateTime.now())
                .stockCritico(false)
                .build();

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getProductoId()).isEqualTo(100L);
        assertThat(dto.getNombreProducto()).isEqualTo("Laptop HP");
        assertThat(dto.getStock()).isEqualTo(50);
        assertThat(dto.getStockMinimo()).isEqualTo(10);
        assertThat(dto.getUbicacionBodega()).isEqualTo("Bodega A");
        assertThat(dto.isStockCritico()).isFalse();
    }

    @Test
    void noArgsConstructor_DebeCrearInventarioDtoVacio() {
        InventarioDto dto = new InventarioDto();

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isNull();
        assertThat(dto.getProductoId()).isNull();
        assertThat(dto.getNombreProducto()).isNull();
        assertThat(dto.getStock()).isNull();
        assertThat(dto.getStockMinimo()).isNull();
        assertThat(dto.getUbicacionBodega()).isNull();
        assertThat(dto.getFechaActualizacion()).isNull();
    }

    @Test
    void allArgsConstructor_DebeCrearInventarioDtoConTodosLosCampos() {
        LocalDateTime now = LocalDateTime.now();
        InventarioDto dto = new InventarioDto(
                1L,
                100L,
                "Laptop HP",
                50,
                10,
                "Bodega A",
                now,
                false
        );

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getProductoId()).isEqualTo(100L);
        assertThat(dto.getNombreProducto()).isEqualTo("Laptop HP");
        assertThat(dto.getStock()).isEqualTo(50);
        assertThat(dto.getStockMinimo()).isEqualTo(10);
        assertThat(dto.getUbicacionBodega()).isEqualTo("Bodega A");
        assertThat(dto.getFechaActualizacion()).isEqualTo(now);
        assertThat(dto.isStockCritico()).isFalse();
    }

    @Test
    void settersAndGetters_DebenFuncionarCorrectamente() {
        InventarioDto dto = new InventarioDto();
        LocalDateTime now = LocalDateTime.now();

        dto.setId(1L);
        dto.setProductoId(100L);
        dto.setNombreProducto("Laptop HP");
        dto.setStock(50);
        dto.setStockMinimo(10);
        dto.setUbicacionBodega("Bodega A");
        dto.setFechaActualizacion(now);
        dto.setStockCritico(true);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getProductoId()).isEqualTo(100L);
        assertThat(dto.getNombreProducto()).isEqualTo("Laptop HP");
        assertThat(dto.getStock()).isEqualTo(50);
        assertThat(dto.getStockMinimo()).isEqualTo(10);
        assertThat(dto.getUbicacionBodega()).isEqualTo("Bodega A");
        assertThat(dto.getFechaActualizacion()).isEqualTo(now);
        assertThat(dto.isStockCritico()).isTrue();
    }

    @Test
    void builder_ConStockCriticoTrue_DebeCrearDtoCorrectamente() {
        InventarioDto dto = InventarioDto.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Laptop HP")
                .stock(5)
                .stockMinimo(10)
                .ubicacionBodega("Bodega A")
                .stockCritico(true)
                .build();

        assertThat(dto).isNotNull();
        assertThat(dto.isStockCritico()).isTrue();
    }

    @Test
    void builder_ConUbicacionBodegaNula_DebeCrearDtoCorrectamente() {
        InventarioDto dto = InventarioDto.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Laptop HP")
                .stock(50)
                .stockMinimo(10)
                .ubicacionBodega(null)
                .stockCritico(false)
                .build();

        assertThat(dto).isNotNull();
        assertThat(dto.getUbicacionBodega()).isNull();
    }
}
