import com.poiji.bind.Poiji;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Funciones {
    public void Insercion(File archivo, Scanner sc) {
        try (FileInputStream fis = new FileInputStream(archivo);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Productos");
            if (sheet == null) {
                sheet = workbook.createSheet("Productos");
            }

            int lastRowNum = sheet.getLastRowNum();
            List<Producto> productos = new ArrayList<>();
            String fechaFormat, codigo = "", productName = "";
            Date fecha = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormat = formato.format(fecha);

            // Cargar productos existentes del archivo
            List<Producto> productosE = Poiji.fromExcel(archivo, Producto.class);

            // Solicitar código del producto
            do {
                if (codigo.length() > 5) {
                    System.out.println("El codigo debe tener 5 caracteres");
                }
                System.out.print("Por favor ingrese el código del producto:");
                codigo = sc.nextLine();
            } while (codigo.length() > 5 || codigo.isEmpty());

            boolean existe = false;
            int cantidad;
            int saldo = 0;
            for (Producto producto : productosE) {

                if (codigo.equals(producto.getCodigo())) {
                    existe = true;
                    saldo = producto.getSaldo();
                    productName = producto.getProducto();
                }
            }


            if (existe) {

                cantidad = leerNumero("Por favor ingrese la cantidad a ingresar:", sc);
                saldo += cantidad; // Se suma la cantidad al saldo existente
            } else {
                // El producto no existe, pedimos tanto el nombre como la cantidad
                do {
                    if (productName.length() > 12) {
                        System.out.println("El Producto debe tener como máximo 12 caracteres");
                    }
                    System.out.println("Por favor ingrese el nombre del producto:");
                    productName = sc.nextLine();
                } while (productName.length() > 12 || productName.isEmpty());

                cantidad = leerNumero("Por favor ingrese la cantidad a ingresar:", sc);
                saldo = cantidad; // Si el producto no existe, el saldo es igual a la cantidad ingresada
            }


            sc.nextLine(); // Limpia el buffer después de leer el número

            // Crear el producto a insertar
            productos.add(new Producto(fechaFormat, "i", codigo, productName, cantidad, saldo));

            // Escribir los datos en el archivo Excel
            try (FileOutputStream fos = new FileOutputStream(archivo)) {
                for (Producto producto : productos) {
                    Row row = sheet.createRow(lastRowNum + 1);
                    row.createCell(0).setCellValue(producto.getFecha());
                    row.createCell(1).setCellValue(producto.getTransaccion());
                    row.createCell(2).setCellValue(producto.getCodigo());
                    row.createCell(3).setCellValue(producto.getProducto());
                    row.createCell(4).setCellValue(producto.getCantidad());
                    row.createCell(5).setCellValue(producto.getSaldo());
                    lastRowNum++;
                }

                workbook.write(fos);
                System.out.println("Datos añadidos con éxito!");
                System.out.println("Presiona Enter para continuar...");
                sc.nextLine();
            }

        } catch (IOException e) {
            System.err.println("Ocurrió un error al manipular el archivo Excel: " + e.getMessage());
        }
    }

    public void Consulta(File archivo) {
        try {
            List<Producto> productos = Poiji.fromExcel(archivo, Producto.class);
//            System.out.println("Productos cargados correctamente.");
            Scanner sc = new Scanner(System.in);
            System.out.println("Por favor ingrese el código a buscar:");
            String code = sc.nextLine();

            productos.stream().filter(producto -> code.equals(producto.getCodigo())).reduce((first, second) -> second).ifPresentOrElse(producto -> {
                System.out.println("+-----------+-------------+------------+--------------+--------+");
                System.out.println("|   Fecha   | Transacción |   Código   |   Producto   |  Saldo |");
                System.out.println("+-----------+-------------+------------+--------------+--------+");
                System.out.printf("| %-10s | %-10s | %-10s | %-12s | %-5s |\n", producto.getFecha(), producto.getTransaccion(), producto.getCodigo(), producto.getProducto(), producto.getSaldo());
            }, () -> System.out.println("No se encontró el producto"));


        } catch (Exception e) {
            System.out.println("Error al cargar el archivo Excel: " + e.getMessage());
        }


    }
    public void Retiro(File archivo, Scanner sc) {
        try (FileInputStream fis = new FileInputStream(archivo);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Productos");
            if (sheet == null) {
                sheet = workbook.createSheet("Productos");
            }

            int lastRowNum = sheet.getLastRowNum();
            List<Producto> productos = new ArrayList<>();
            String fechaFormat, codigo = "", productName = "";
            Date fecha = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormat = formato.format(fecha);

            // Cargar productos existentes del archivo
            List<Producto> productosE = Poiji.fromExcel(archivo, Producto.class);

            // Solicitar código del producto
            do {
                if (codigo.length() > 5) {
                    System.out.println("El código debe tener 5 caracteres.");
                }
                System.out.print("Por favor ingrese el código del producto: ");
                codigo = sc.nextLine();
            } while (codigo.length() > 5 || codigo.isEmpty());

            // Buscar el último producto con ese código
            List<Producto> productoEncontrado = new ArrayList<>();
            for (Producto producto : productosE) {
                if (codigo.equals(producto.getCodigo())) {
                    productoEncontrado.add(producto); // Se actualiza al último producto con el código
                }
            }
                int cantidadRetiro;
            System.out.println(productoEncontrado);
int size = 0;
            if (productoEncontrado.size()>0) {
                // Si el producto existe, verificar el saldo
                size = productoEncontrado.size();
                int saldo = productoEncontrado.get(size-1).getSaldo();
                System.out.println("Saldo disponible: " + saldo);

                do {
                    cantidadRetiro = leerNumero("Por favor ingrese la cantidad a retirar: ", sc);
                    if (cantidadRetiro > saldo) {
                        System.out.println("La cantidad a retirar no puede ser mayor al saldo disponible.");
                    }
                } while (cantidadRetiro > saldo);

                // Realizar el retiro y actualizar el saldo
                saldo -= cantidadRetiro;


                // Escribir los datos en el archivo Excel
                try (FileOutputStream fos = new FileOutputStream(archivo)) {
                    // Escribir los productos actualizados en el archivo Excel

                        Row row = sheet.createRow(lastRowNum + 1);
                        row.createCell(0).setCellValue(fechaFormat);
                        row.createCell(1).setCellValue("s");
                        row.createCell(2).setCellValue(productoEncontrado.get(size-1).getCodigo());
                        row.createCell(3).setCellValue(productoEncontrado.get(size-1).getProducto());
                        row.createCell(4).setCellValue(cantidadRetiro);
                        row.createCell(5).setCellValue(saldo);
                        lastRowNum++;


                    workbook.write(fos);
                    System.out.println("Datos actualizados con éxito!");
                    System.out.println("Presiona Enter para continuar...");
                    sc.nextLine();
                }
            } else {
                System.out.println("Producto con el código ingresado no encontrado.");
            }

        } catch (IOException e) {
            System.err.println("Ocurrió un error al manipular el archivo Excel: " + e.getMessage());
        }
    }

    public void Reporte(File archivo) {
        List<Producto> productos = Poiji.fromExcel(archivo, Producto.class);
        System.out.println("+-----------+-------------+------------+--------------+----------+-------+");
        System.out.println("|   Fecha   | Transacción |   Código   |   Producto   | Cantidad | Saldo |");
        System.out.println("+-----------+-------------+------------+--------------+----------+-------+");
        productos.forEach(producto -> {
            System.out.printf("| %-9s | %-11s | %-10s | %-12s | %-10s | %-5s |\n", producto.getFecha(), producto.getTransaccion(), producto.getCodigo(), producto.getProducto(), producto.getCantidad(), producto.getSaldo());
        });
    }

    // Método para alerta de stock
    public void alertaDeStock(File archivo, int umbral) {
        try {
            // Leer los productos desde el archivo Excel
            List<Producto> productos = Poiji.fromExcel(archivo, Producto.class);

            // Mapa para almacenar el último producto encontrado por código
            Map<String, Producto> productosBajoStock = new HashMap<>();

            // Verificar si los productos tienen saldo bajo el umbral
            for (Producto producto : productos) {
                if (producto.getSaldo() < umbral) {
                    // Si el código ya existe, sobrescribir el producto con el saldo más bajo
                    productosBajoStock.put(producto.getCodigo(), producto);
                }
            }

            // Verificar si hay productos con saldo bajo el umbral
            if (productosBajoStock.isEmpty()) {
                System.out.println("No hay productos con saldo por debajo del umbral especificado.");
            } else {
                System.out.println("Últimos productos con saldo bajo el umbral (" + umbral + "):");
                // Recorrer los productos y mostrar el último encontrado por código
                System.out.println("+-----------+--------------+--------+");
                System.out.println("|  Código   |   Producto   |  Saldo |");
                System.out.println("+-----------+--------------+--------+");
                for (Producto producto : productosBajoStock.values()) {

                    System.out.printf("| %-9s | %-12s | %-6s |\n",  producto.getCodigo(), producto.getProducto(), producto.getSaldo());
//                    System.out.println(producto);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al verificar el stock: " + e.getMessage());
        }
    }

    private int leerNumero(String mensaje, Scanner sc) {
        int numero;
        while (true) {
            System.out.println(mensaje);
            if (sc.hasNextInt()) {
                numero = sc.nextInt();
//                sc.nextLine(); // Consumir el salto de línea
                return numero;
            } else {
                System.out.println("Por favor ingrese un número válido.");
                sc.next(); // Limpiar entrada inválida
            }
        }
    }


}
