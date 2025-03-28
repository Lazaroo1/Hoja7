import java.util.*;
import java.io.*;

/**
 * Clase principal con el menú del programa
 * Aquí está toda la magia pa que el usuario juegue con el inventario  
 */
public class Main {

    public static void main(String[] args) {
        SistemaInventario sistema = new SistemaInventario();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("TIENDA DE ROPA");
        System.out.println("-------------------------------------------------------");
        
        cargarDatosIniciales(sistema, scanner);
        
        int opcion;
        do {
            mostrarMenu();
            opcion = leerOpcion(scanner);
            procesarOpcion(opcion, sistema, scanner);
        } while (opcion != 0);
        
        scanner.close();
    }

    private static void cargarDatosIniciales(SistemaInventario sistema, Scanner scanner) {
        System.out.print("\n¿Quieres cargar productos desde un CSV? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            System.out.print("Nombre del archivo (nombre.csv): ");
            String archivo = scanner.nextLine().trim();
            try {
                sistema.cargarDesdeCSV(archivo);
                System.out.println("datos cargados");
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n **MENU**");
        System.out.println("1.  Añadir producto");
        System.out.println("2. Buscar por SKU");
        System.out.println("3. Buscar por nombre");
        System.out.println("4. Listar por SKU");
        System.out.println("5.  Listar por nombre");
        System.out.println("6. Editar producto");
        System.out.println("7.  Exportar a CSV");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
    }

    private static int leerOpcion(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void procesarOpcion(int opcion, SistemaInventario sistema, Scanner scanner) {
        switch (opcion) {
            case 1 -> agregarProducto(sistema, scanner);
            case 2 -> buscarPorSKU(sistema, scanner);
            case 3 -> buscarPorNombre(sistema, scanner);
            case 4 -> listarProductos(sistema, true);
            case 5 -> listarProductos(sistema, false);
            case 6 -> editarProducto(sistema, scanner);
            case 7 -> exportarACSV(sistema, scanner);
            case 0 -> System.out.println("\n saliendo");
            default -> System.out.println("\n opcion no valida");
        }
    }

    private static void agregarProducto(SistemaInventario sistema, Scanner scanner) {
        System.out.println("\n **AÑADIR PRODUCTO**");
        
        System.out.print("SKU: ");
        String sku = scanner.nextLine().trim();
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine().trim();
        
        Producto producto = new Producto(sku, nombre, descripcion);
        
        System.out.print("Tallas (ej: S:10|M:15|L:5): ");
        String[] tallas = scanner.nextLine().split("\\|");
        
        for (String tallaInfo : tallas) {
            String[] partes = tallaInfo.split(":");
            if (partes.length == 2) {
                producto.agregarTalla(partes[0].trim(), Integer.parseInt(partes[1].trim()));
            }
        }
        
        sistema.agregarProducto(producto);
        System.out.println("\n el producto se añadió correctamente");
    }

    private static void buscarPorSKU(SistemaInventario sistema, Scanner scanner) {
        System.out.println("\n **BUSCAR POR SKU**");
        System.out.print("SKU a buscar: ");
        String sku = scanner.nextLine().trim();
        
        Producto producto = sistema.buscarPorSKU(sku);
        if (producto != null) {
            System.out.println("\n **ENCONTRADO**");
            System.out.println(producto);
        } else {
            System.out.println("\n No existe ese SKU");
        }
    }

    private static void buscarPorNombre(SistemaInventario sistema, Scanner scanner) {
        System.out.println("\n *BUSCAR POR NOMBRE**");
        System.out.print("Nombre a buscar: ");
        String nombre = scanner.nextLine().trim().toLowerCase();
        
        Producto producto = sistema.buscarPorNombre(nombre);
        if (producto != null) {
            System.out.println("\n**ENCONTRADO**");
            System.out.println(producto);
        } else {
            System.out.println("\n No hay productos con ese nombre");
        }
    }

    private static void listarProductos(SistemaInventario sistema, boolean porSKU) {
        System.out.println("\n **LISTA DE PRODUCTOS**");
        List<Producto> productos = porSKU ? sistema.listarProductosPorSKU() : sistema.listarProductosPorNombre();
        
        if (productos.isEmpty()) {
            System.out.println("No hay nada, agrega productos primero");
        } else {
            productos.forEach(p -> {
                System.out.println(p);
                System.out.println("-----------------------");
            });
        }
    }

    private static void editarProducto(SistemaInventario sistema, Scanner scanner) {
        System.out.println("\n**EDITAR PRODUCTO*");
        System.out.print("SKU del producto a editar: ");
        String sku = scanner.nextLine().trim();
        
        Producto producto = sistema.buscarPorSKU(sku);
        if (producto == null) {
            System.out.println("\n SKU no encontrado");
            return;
        }
        
        System.out.println("\n Producto actual:");
        System.out.println(producto);
        
        System.out.print("\n Nueva descripción (deja vacío para no cambiar): ");
        String nuevaDesc = scanner.nextLine().trim();
        if (!nuevaDesc.isEmpty()) {
            producto.editarDescripcion(nuevaDesc);
        }
        
        System.out.print("Nuevas tallas (p.ej: S:5|M:10, deja vacío para no cambiar): ");
        String nuevasTallasStr = scanner.nextLine().trim();
        if (!nuevasTallasStr.isEmpty()) {
            Map<String, Integer> nuevasTallas = new HashMap<>();
            String[] tallas = nuevasTallasStr.split("\\|");
            for (String tallaInfo : tallas) {
                String[] partes = tallaInfo.split(":");
                if (partes.length == 2) {
                    nuevasTallas.put(partes[0].trim(), Integer.parseInt(partes[1].trim()));
                }
            }
            sistema.editarProducto(sku, nuevaDesc.isEmpty() ? null : nuevaDesc, nuevasTallas);
        }
        
        System.out.println("\n producto actualizado");
    }

    private static void exportarACSV(SistemaInventario sistema, Scanner scanner) {
        System.out.println("\n **EXPORTAR A CSV**");
        System.out.print("Nombre del archivo (nombre.csv): ");
        String archivo = scanner.nextLine().trim();
        
        try {
            sistema.exportarACSV(archivo);
            System.out.println("Inventario guardado");
        } catch (IOException e) {
            System.out.println(" Error al exportar: " + e.getMessage());
        }
    }
}