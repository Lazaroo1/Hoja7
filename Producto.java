import java.util.*;


public class Producto implements Comparable<Producto> {
    private String sku;
    private String nombre;
    private String descripcion;
    private Map<String, Integer> tallas;

    public Producto(String sku, String nombre, String descripcion) {
        this.sku = sku;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tallas = new TreeMap<>();
    }

    public String getSku() { return sku; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Map<String, Integer> getTallas() { return new HashMap<>(tallas); }

    public void agregarTalla(String talla, int cantidad) {
        tallas.put(talla, cantidad);
    }

    public void editarDescripcion(String nuevaDescripcion) {
        this.descripcion = nuevaDescripcion;
    }

    @Override
    public int compareTo(Producto otro) {
        return this.sku.compareTo(otro.sku);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SKU: ").append(sku).append("\n");
        sb.append("Nombre: ").append(nombre).append("\n");
        sb.append("Descripción: ").append(descripcion).append("\n");
        sb.append("Tallas disponibles:\n");
        tallas.forEach((talla, cant) -> sb.append("  ").append(talla).append(": ").append(cant).append("\n"));
        return sb.toString();
    }
}