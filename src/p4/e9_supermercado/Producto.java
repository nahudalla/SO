package p4.e9_supermercado;

import p4.e7_Readers_Writers.RWLock;

public class Producto {
    private long stock;
    private double precio;

    private final long identificador;

    private final RWLock lock = new RWLock();

    public Producto(long identificador) {
        this.identificador = identificador;
    }

    public RWLock getLock() {
        return this.lock;
    }

    public void setPrecio(double precio) {
        this.lock.acquire_write();

        this.setPrecio_unsafe(precio);

        this.lock.release_write();
    }

    public void setPrecio_unsafe(double precio) {
        this.precio = precio;
    }

    public double getPrecio_unsafe() {
        return this.precio;
    }

    public double getPrecio() {
        this.lock.acquire_read();

        double precio = this.precio;

        this.lock.release_read();

        return precio;
    }

    public long incrementar_stock() {
        return this.incrementar_stock(1);
    }

    public long incrementar_stock(long cant) {
        this.lock.acquire_write();

        this.stock += cant;

        long stock = this.stock;

        this.lock.release_write();

        return stock;
    }

    public long decrementar_stock() {
        return this.decrementar_stock(1);
    }

    public long decrementar_stock(long cant) {
        this.lock.acquire_write();

        this.stock -= cant;

        long stock = this.stock;

        this.lock.release_write();

        return stock;
    }

    public void setStock(long cant) {
        this.lock.acquire_write();

        this.setStock_unsafe(cant);

        this.lock.release_write();
    }

    public void setStock_unsafe(long cant) {
        this.stock = cant;
    }

    public long getStock() {
        this.lock.acquire_read();

        long stock = this.stock;

        this.lock.release_read();

        return stock;
    }

    public long getStock_unsafe() {
        return this.stock;
    }

    public long getIdentificador() {
        return this.identificador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Producto producto = (Producto) o;

        return identificador == producto.identificador;
    }

    @Override
    public int hashCode() {
        return (int) (identificador ^ (identificador >>> 32));
    }
}
