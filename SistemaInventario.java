import java.util.*;
import java.io.*;


public class SistemaInventario {
    private final BinaryTree<String, Producto> arbolSKU;
    private final BinaryTree<String, Producto> arbolNombre;

    public SistemaInventario() {
        arbolSKU = new BinaryTree<>();
        arbolNombre = new BinaryTree<>();
    }

    public void agregarProducto(Producto producto) {
        arbolSKU.insertar(producto.getSku(), producto);
        arbolNombre.insertar(producto.getNombre().toLowerCase(), producto);
    }

    public Producto buscarPorSKU(String sku) {
        return arbolSKU.buscar(sku);
    }

    public Producto buscarPorNombre(String nombre) {
        return arbolNombre.buscar(nombre.toLowerCase());
    }

    public List<Producto> listarProductosPorSKU() {
        return arbolSKU.inOrder();
    }

    public List<Producto> listarProductosPorNombre() {
        return arbolNombre.inOrder();
    }

    public boolean editarProducto(String sku, String nuevaDesc, Map<String, Integer> nuevasTallas) {
        Producto p = buscarPorSKU(sku);
        if (p == null) return false;
        
        if (nuevaDesc != null) p.editarDescripcion(nuevaDesc);
        if (nuevasTallas != null) {
            p.getTallas().clear();
            nuevasTallas.forEach(p::agregarTalla);
        }
        return true;
    }

/**
 * Cargamos productos desde un archivo CSV  
 * @param archivo Ruta del archivo CSV a cargar 
 * @throws IOException Si hay error al leer el archivo
 */
public void cargarDesdeCSV(String archivo) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        br.readLine(); // Saltamos la primera línea del encabezado para ignorarla
        
        while ((linea = br.readLine()) != null) { 
            String[] datos = linea.split(",");
            if (datos.length >= 3) {  // Mínimo tiene que tener: SKU, Nombre, Descripción
                Producto producto = new Producto(
                    datos[0].trim(), 
                    datos[1].trim(), 
                    datos[2].trim()
                );
                
                // Procesar tallas si existen (en la columna 4)
                if (datos.length > 3) {
                    String[] tallas = datos[3].split("\\|");
                    for (String tallaInfo : tallas) {
                        String[] partes = tallaInfo.split(":");
                        if (partes.length == 2) {  // Formato correcto: "talla:cantidad"
                            producto.agregarTalla(
                                partes[0].trim(), 
                                Integer.parseInt(partes[1].trim())
                            );
                        }
                    }
                }
                
                agregarProducto(producto);  // Lo añade al sistema
            }
        }
    }
}
    public void exportarACSV(String archivo) throws IOException {
        try (PrintWriter pw = new PrintWriter(archivo)) {
            pw.println("SKU,Nombre,Descripción,Cantidad por talla");
            listarProductosPorSKU().forEach(p -> {
                String tallas = p.getTallas().entrySet().stream()
                    .map(e -> e.getKey() + ":" + e.getValue())
                    .reduce((a, b) -> a + "|" + b).orElse("");
                pw.println(p.getSku() + "," + p.getNombre() + "," + p.getDescripcion() + "," + tallas);
            });
        }
    }
}