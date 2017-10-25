package p4.e9_supermercado;

import p4.e7_Readers_Writers.RWLock;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Supermercado {
    final private RWLock lock = new RWLock();

    private Map<Long, Producto> productos = new HashMap<>();
    private Map<Long, ProductoCanasta> canasta = new HashMap<>();

    public void agregarProducto(Producto producto) {
        this.lock.acquire_write();

        this.productos.put(producto.getIdentificador(), producto);

        this.lock.release_write();
    }

    public void eliminarProducto(long identifier) {
        this.lock.acquire_write();

        this.productos.remove(identifier);

        this.lock.release_write();
    }

    public void agregarProductoACanasta(long identifier, long stockMinimo) {
        this.lock.acquire_write();

        if(this.productos.containsKey(identifier)) {
            ProductoCanasta productoCanasta = new ProductoCanasta();
            productoCanasta.fecha_agregado = Date.from(Instant.now());
            productoCanasta.stock_minimo = stockMinimo;
            this.canasta.put(identifier, productoCanasta);
        }

        this.lock.release_write();
    }

    public void eliminarProductoDeCanasta(long identifier) {
        this.lock.acquire_write();

        this.canasta.remove(identifier);

        this.lock.release_write();
    }

    public ProductoFacturado facturarProducto(long identifier) {
        Producto producto;
        ProductoFacturado productoFacturado = null;

        this.lock.acquire_read();

        ProductoCanasta productoCanasta = this.canasta.get(identifier);
        if(productoCanasta == null) {
            producto = this.productos.get(identifier);
            if(producto != null) {
                productoFacturado = new ProductoFacturado(producto, producto.getPrecio(), false);
            }
        }

        this.lock.release_read();

        if(productoFacturado != null)
            return productoFacturado;

        if(productoCanasta != null) {
            this.lock.acquire_write();

            producto = this.productos.get(identifier);

            if(producto == null) {
                this.lock.release_write();
                return null;
            }

            if(productoCanasta.fecha_agregado.compareTo(Date.from(Instant.now().minus(1, ChronoUnit.WEEKS))) < 0) {
                this.canasta.remove(identifier);
            }

            if(this.canasta.containsKey(identifier)) {
                long nuevoStock = producto.decrementar_stock();

                if (nuevoStock <= (productoCanasta.stock_minimo - 1)) {
                    this.canasta.remove(identifier);
                }

                if(nuevoStock >= 0) {
                    productoFacturado = new ProductoFacturado(producto, producto.getPrecio() * 0.9, true);
                }
            } else {
                productoFacturado = new ProductoFacturado(producto, producto.getPrecio(), false);
            }

            this.lock.release_write();
        }

        return productoFacturado;
    }

    public class ProductoFacturado {
        private final Producto producto;
        private final double precioFinal;
        private final boolean enOferta;

        public ProductoFacturado(Producto producto, double precioFinal, boolean enOferta) {
            this.producto = producto;
            this.precioFinal = precioFinal;
            this.enOferta = enOferta;
        }

        public Producto getProducto() {
            return producto;
        }

        public double getPrecioFinal() {
            return precioFinal;
        }

        public boolean estaEnOferta() {
            return enOferta;
        }
    }

    private class ProductoCanasta {
        public Date fecha_agregado;
        public long stock_minimo;
    }
}
