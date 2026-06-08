package com.lumina.bodega.service;

import com.lumina.bodega.entity.AlertaStock;
import com.lumina.bodega.entity.Inventario;
import com.lumina.bodega.repository.AlertaStockRepository;
import com.lumina.bodega.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BodegaServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private AlertaStockRepository alertaStockRepository;

    @InjectMocks
    private BodegaService bodegaService;

    private Inventario inventario;
    private AlertaStock alertaStock;

    @BeforeEach
    void setUp() {
        inventario = Inventario.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Producto Test")
                .stock(50)
                .stockMinimo(10)
                .build();

        alertaStock = AlertaStock.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Producto Test")
                .stockActual(5)
                .stockMinimo(10)
                .resuelta(false)
                .build();
    }

    @Test
    void listarInventario_DebeRetornarListaVacia_CuandoNoHayRegistros() {
        when(inventarioRepository.findAll()).thenReturn(Collections.emptyList());

        List<Inventario> resultado = bodegaService.listarInventario();

        assertThat(resultado).isEmpty();
        verify(inventarioRepository).findAll();
    }

    @Test
    void listarInventario_DebeRetornarListaConRegistros_CuandoExistenInventarios() {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findAll()).thenReturn(inventarios);

        List<Inventario> resultado = bodegaService.listarInventario();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getProductoId()).isEqualTo(100L);
        verify(inventarioRepository).findAll();
    }

    @Test
    void crearInventario_DebeCrearExitosamente_CuandoInventarioEsValido() {
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        Inventario resultado = bodegaService.crearInventario(inventario);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getProductoId()).isEqualTo(100L);
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void obtenerStock_DebeRetornarInventario_CuandoExisteRegistro() {
        when(inventarioRepository.findByProductoId(100L)).thenReturn(Optional.of(inventario));

        Inventario resultado = bodegaService.obtenerStock(100L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getProductoId()).isEqualTo(100L);
        verify(inventarioRepository).findByProductoId(100L);
    }

    @Test
    void obtenerStock_DebeLanzarExcepcion_CuandoNoExisteRegistro() {
        when(inventarioRepository.findByProductoId(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bodegaService.obtenerStock(100L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No hay registro de inventario para producto: 100");

        verify(inventarioRepository).findByProductoId(100L);
    }

    @Test
    void actualizarStock_DebeAgregarStockSinGenerarAlerta_CuandoStockEsMayorAlMinimo() {
        when(inventarioRepository.findByProductoId(100L)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        Inventario resultado = bodegaService.actualizarStock(100L, 20);

        assertThat(resultado.getStock()).isEqualTo(70);
        verify(alertaStockRepository, never()).findByProductoIdAndResueltaFalse(anyLong());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void actualizarStock_DebeAgregarStockYGenerarAlerta_CuandoStockEsCritico() {
        Inventario inventarioCritico = Inventario.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Producto Test")
                .stock(5)
                .stockMinimo(10)
                .build();

        when(inventarioRepository.findByProductoId(100L)).thenReturn(Optional.of(inventarioCritico));
        when(alertaStockRepository.findByProductoIdAndResueltaFalse(100L)).thenReturn(Collections.emptyList());
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioCritico);
        when(alertaStockRepository.save(any(AlertaStock.class))).thenReturn(alertaStock);

        Inventario resultado = bodegaService.actualizarStock(100L, 2);

        assertThat(resultado.getStock()).isEqualTo(7);
        verify(alertaStockRepository).findByProductoIdAndResueltaFalse(100L);
        verify(alertaStockRepository).save(any(AlertaStock.class));
        verify(inventarioRepository).save(inventarioCritico);
    }

    @Test
    void actualizarStock_DebeAgregarStockYResolverAlertas_CuandoStockNoEsCritico() {
        Inventario inventarioConAlerta = Inventario.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Producto Test")
                .stock(5)
                .stockMinimo(10)
                .build();

        when(inventarioRepository.findByProductoId(100L)).thenReturn(Optional.of(inventarioConAlerta));
        when(alertaStockRepository.findByProductoIdAndResueltaFalse(100L)).thenReturn(Arrays.asList(alertaStock));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioConAlerta);

        Inventario resultado = bodegaService.actualizarStock(100L, 20);

        assertThat(resultado.getStock()).isEqualTo(25);
        verify(alertaStockRepository).findByProductoIdAndResueltaFalse(100L);
        verify(alertaStockRepository).save(alertaStock);
        verify(inventarioRepository).save(inventarioConAlerta);
    }

    @Test
    void descontarStock_DebeDescontarExitosamente_CuandoStockEsSuficiente() {
        when(inventarioRepository.findByProductoId(100L)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        Inventario resultado = bodegaService.descontarStock(100L, 10);

        assertThat(resultado.getStock()).isEqualTo(40);
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void descontarStock_DebeLanzarExcepcion_CuandoStockEsInsuficiente() {
        when(inventarioRepository.findByProductoId(100L)).thenReturn(Optional.of(inventario));

        assertThatThrownBy(() -> bodegaService.descontarStock(100L, 60))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Stock insuficiente para producto: 100");

        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void descontarStock_DebeGenerarAlerta_CuandoStockEsCritico() {
        Inventario inventarioCritico = Inventario.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Producto Test")
                .stock(15)
                .stockMinimo(10)
                .build();

        when(inventarioRepository.findByProductoId(100L)).thenReturn(Optional.of(inventarioCritico));
        when(alertaStockRepository.findByProductoIdAndResueltaFalse(100L)).thenReturn(Collections.emptyList());
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioCritico);
        when(alertaStockRepository.save(any(AlertaStock.class))).thenReturn(alertaStock);

        Inventario resultado = bodegaService.descontarStock(100L, 10);

        assertThat(resultado.getStock()).isEqualTo(5);
        verify(alertaStockRepository).findByProductoIdAndResueltaFalse(100L);
        verify(alertaStockRepository).save(any(AlertaStock.class));
        verify(inventarioRepository).save(inventarioCritico);
    }

    @Test
    void descontarStock_DebeResolverAlertas_CuandoStockNoEsCritico() {
        Inventario inventarioConAlerta = Inventario.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Producto Test")
                .stock(5)
                .stockMinimo(10)
                .build();

        when(inventarioRepository.findByProductoId(100L)).thenReturn(Optional.of(inventarioConAlerta));
        when(alertaStockRepository.findByProductoIdAndResueltaFalse(100L)).thenReturn(Arrays.asList(alertaStock));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioConAlerta);

        Inventario resultado = bodegaService.descontarStock(100L, -5);

        assertThat(resultado.getStock()).isEqualTo(10);
        verify(alertaStockRepository).findByProductoIdAndResueltaFalse(100L);
        verify(alertaStockRepository).save(alertaStock);
        verify(inventarioRepository).save(inventarioConAlerta);
    }

    @Test
    void obtenerAlertas_DebeRetornarListaVacia_CuandoNoHayAlertasActivas() {
        when(alertaStockRepository.findByResueltaFalseOrderByFechaCreacionDesc())
                .thenReturn(Collections.emptyList());

        List<AlertaStock> resultado = bodegaService.obtenerAlertas();

        assertThat(resultado).isEmpty();
        verify(alertaStockRepository).findByResueltaFalseOrderByFechaCreacionDesc();
    }

    @Test
    void obtenerAlertas_DebeRetornarListaConAlertas_CuandoExistenAlertasActivas() {
        List<AlertaStock> alertas = Arrays.asList(alertaStock);
        when(alertaStockRepository.findByResueltaFalseOrderByFechaCreacionDesc())
                .thenReturn(alertas);

        List<AlertaStock> resultado = bodegaService.obtenerAlertas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getProductoId()).isEqualTo(100L);
        verify(alertaStockRepository).findByResueltaFalseOrderByFechaCreacionDesc();
    }
}
