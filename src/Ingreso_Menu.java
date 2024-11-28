import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Usuario {
    String usuario;
    String clave;
    int perfil;
    String nombre;

    Usuario(String usuario, String clave, int perfil, String nombre) {
        this.usuario = usuario;
        this.clave = clave;
        this.perfil = perfil;
        this.nombre = nombre;
    }
}

public class Ingreso_Menu {
    private static Map<String, Usuario> usuarios = new HashMap<>();
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarUsuarios();
        mostrarMenuPrincipal();
    }

    private static void inicializarUsuarios() {
        usuarios.put("ggrossman", new Usuario("ggrossman", "ggrossman2024", 1, "Gabriela Grossman"));
        usuarios.put("jvasquez", new Usuario("jvasquez", "jvasquez2024", 2, "Jhon Vasquez"));
        usuarios.put("jsuclupe", new Usuario("jsuclupe", "jsuclupe2024", 2, "Angel Suclupe"));
        usuarios.put("hzapata", new Usuario("hzapata", "hzapata2024", 3, "Humberto Zapata"));
    }

    private static void limpiarPantalla() {
        try {
            // Retardo de 1000 milisegundos (1 segundos)
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.getMessage();
        }
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private static void mostrarMenuPrincipal() {
        while (true) {
            limpiarPantalla();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║          ASP Control SRL             ║");
            System.out.println("║   Ingreso al sistema de Productos    ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║ 1. Ingresar                          ║");
            System.out.println("║ 2. Salir del Sistema                 ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Selecciona una opción: ");
            int opcion = 0;
            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Clear buffer
            }catch (Exception e){
                scanner.nextLine(); // Limpia el buffer en caso de error
            }


            if (opcion == 1) {
                Usuario usuarioAutenticado = autenticar();
                if (usuarioAutenticado != null) {
                    mostrarMenuSistema(usuarioAutenticado);
                } else {
                    System.out.println("Número de intentos fallidos excedido. Saliendo del sistema.");
                    return;
                }
            } else if (opcion == 2) {
                System.out.println("Saliendo del sistema...");
                return;
            } else {
                System.out.println("Opción no válida, por favor intente de nuevo.");
            }
        }
    }

    private static Usuario autenticar() {
        int intentos = 0;
        while (intentos < 3) {
            limpiarPantalla();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║          ASP Control SRL             ║");
            System.out.println("║  Ingreso de Credenciales al Sistema  ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("     Usuario:  ");
            String usuario = scanner.nextLine();
            System.out.print("     Clave  :  ");
            String clave = scanner.nextLine();


            if (usuarios.containsKey(usuario) && usuarios.get(usuario).clave.equals(clave)) {
                System.out.println("Autenticación exitosa. Bienvenido, " + usuarios.get(usuario).nombre + ".");
                return usuarios.get(usuario);
            } else {
                System.out.println("Credenciales incorrectas. Intentos restantes: " + (2 - intentos));
                intentos++;
            }
        }
        return null;
    }

    private static void mostrarMenuSistema(Usuario usuarioAutenticado) {
        Funciones funciones = new Funciones();
        while (true) {
            limpiarPantalla();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║          ASP Control SRL             ║");
            System.out.println("║   Opciones del sistema de Productos  ║");
            System.out.println("╠══════════════════════════════════════╣");

            if (usuarioAutenticado.perfil == 1) {
                System.out.println("║ 1. Registro de productos             ║");
                System.out.println("║ 2. Salida de productos               ║");
                System.out.println("║ 3. Consulta de productos             ║");
                System.out.println("║ 4. Alertas de productos              ║");
                System.out.println("║ 5. Reporte de productos              ║");
                System.out.println("║ 6. Salir del menú                    ║");
            } else if (usuarioAutenticado.perfil == 2) {
                System.out.println("║ 1. Registro de productos             ║");
                System.out.println("║ 2. Salida de productos               ║");
                System.out.println("║ 3. Consulta de productos             ║");
                System.out.println("║ 6. Salir del menú                    ║");
            } else if (usuarioAutenticado.perfil == 3) {
                System.out.println("║ 4. Alertas de productos              ║");
                System.out.println("║ 5. Reporte de productos              ║");
                System.out.println("║ 6. Salir del menú                    ║");
            }

            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Selecciona una opción: ");
            try {
                int opcion = scanner.nextInt();
               scanner.nextLine(); // Limpia el buffer

                if (opcion == 6) {
                    System.out.println("Saliendo del sistema...");
                    return;
                }

                File file = new File("inventario.xlsx");
                if (!file.exists()) {

                    System.out.println("El archivo inventario.xlsx no existe.");
                    return;
                }


                switch (opcion) {
                    case 1:
                        funciones.Insercion(file,scanner);
                        break;
                    case 2:
                        funciones.Retiro(file,scanner);
                        System.out.println("Presiona Enter para continuar...");
                        scanner.nextLine();
                        break;
                    case 3:
                        funciones.Consulta(file);
                        System.out.println("Presiona Enter para continuar...");
                        scanner.nextLine();
                        break;
                    case 4:
                        int umbral = 10;
                        funciones.alertaDeStock(file,umbral);
                        System.out.println("Presiona Enter para continuar...");
                        scanner.nextLine();
                        break;
                    case 5:
                        funciones.Reporte(file);
                        System.out.println("Presiona Enter para continuar...");
                        scanner.nextLine();
                        break;
                    default:
                        System.out.println("Opción no encontrada");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Entrada inválida. Por favor, intenta de nuevo.");
                scanner.nextLine(); // Limpia el buffer en caso de error
            }
        }
    }

}
