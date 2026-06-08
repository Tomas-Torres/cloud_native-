package com.lumina.bodega.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lumina.bodega.entity.AlertaStock;
import com.lumina.bodega.entity.Inventario;
import com.lumina.bodega.service.BodegaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BodegaController.class)
class BodegaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    void listarInventario_DebeRetornar200YLista_CuandoHayInventarios() throws Exception {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(bodegaService.listarInventario()).thenReturn(inventarios);

        mockMvc.perform(get("/api/bodega/inventario"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].productoId").value(100L))
                .andExpect(jsonPath("$[0].nombreProducto").value("Producto Test"));
    }

    @Test
    void listarInventario_DebeRetornar200YListaVacia_CuandoNoHayInventarios() throws Exception {
        when(bodegaService.listarInventario()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/bodega/inventario"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void obtenerStock_DebeRetornar200YInventario_CuandoExisteRegistro() throws Exception {
        when(bodegaService.obtenerStock(100L)).thenReturn(inventario);

        mockMvc.perform(get("/api/bodega/inventario/100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productoId").value(100L))
                .andExpect(jsonPath("$.nombreProducto").value("Producto Test"))
                .andExpect(jsonPath("$.stock").value(50));
    }

    @Test
    void obtenerStock_DebeRetornarError_CuandoNoExisteRegistro() throws Exception {
        when(bodegaService.obtenerStock(100L))
                .thenThrow(new RuntimeException("No hay registro de inventario para producto: 100"));

        mockMvc.perform(get("/api/bodega/inventario/100"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void crearInventario_DebeRetornar201YInventario_CuandoInventarioEsValido() throws Exception {
        when(bodegaService.crearInventario(any(Inventario.class))).thenReturn(inventario);

        mockMvc.perform(post("/api/bodega/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventario)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productoId").value(100L))
                .andExpect(jsonPath("$.nombreProducto").value("Producto Test"));
    }

    @Test
    void agregarStock_DebeRetornar200YInventarioActualizado_CuandoCantidadEsValida() throws Exception {
        Inventario inventarioActualizado = Inventario.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Producto Test")
                .stock(70)
                .stockMinimo(10)
                .build();

        when(bodegaService.actualizarStock(100L, 20)).thenReturn(inventarioActualizado);

        Map<String, Integer> body = Map.of("cantidad", 20);
        mockMvc.perform(patch("/api/bodega/inventario/100/agregar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productoId").value(100L))
                .andExpect(jsonPath("$.stock").value(70));
    }

    @Test
    void descontarStock_DebeRetornar200YInventarioActualizado_CuandoStockEsSuficiente() throws Exception {
        Inventario inventarioActualizado = Inventario.builder()
                .id(1L)
                .productoId(100L)
                .nombreProducto("Producto Test")
                .stock(40)
                .stockMinimo(10)
                .build();

        when(bodegaService.descontarStock(100L, 10)).thenReturn(inventarioActualizado);

        Map<String, Integer> body = Map.of("cantidad", 10);
        mockMvc.perform(patch("/api/bodega/inventario/100/descontar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productoId").value(100L))
                .andExpect(jsonPath("$.stock").value(40));
    }

    @Test
    void descontarStock_DebeRetornarError_CuandoStockEsInsuficiente() throws Exception {
        when(bodegaService.descontarStock(100L, 60))
                .thenThrow(new RuntimeException("Stock insuficiente para producto: 100"));

        Map<String, Integer> body = Map.of("cantidad", 60);
        mockMvc.perform(patch("/api/bodega/inventario/100/descontar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void obtenerAlertas_DebeRetornar200YListaDeAlertas_CuandoExistenAlertas() throws Exception {
        List<AlertaStock> alertas = Arrays.asList(alertaStock);
        when(bodegaService.obtenerAlertas()).thenReturn(alertas);

        mockMvc.perform(get("/api/bodega/alertas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].productoId").value(100L))
                .andExpect(jsonPath("$[0].nombreProducto").value("Producto Test"))
                .andExpect(jsonPath("$[0].resuelta").value(false));
    }

    @Test
    void obtenerAlertas_DebeRetornar200YListaVacia_CuandoNoHayAlertas() throws Exception {
        when(bodegaService.obtenerAlertas()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/bodega/alertas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }
}
