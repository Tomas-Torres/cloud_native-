package lumina.productos.service;

import lumina.productos.entity.Marca;
import lumina.productos.entity.Producto;
import lumina.productos.repository.MarcaRepository;
import lumina.productos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final MarcaRepository marcaRepository;

    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    public List<Producto> buscar(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Marca> listarMarcas() {
        return marcaRepository.findAll();
    }

    public Producto crear(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto datos) {
        Producto producto = obtenerPorId(id);
        producto.setNombre(datos.getNombre());
        producto.setDescripcion(datos.getDescripcion());
        producto.setPrecio(datos.getPrecio());
        producto.setImagenUrl(datos.getImagenUrl());
        producto.setCategoria(datos.getCategoria());
        producto.setMarca(datos.getMarca());
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        Producto producto = obtenerPorId(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }
}
