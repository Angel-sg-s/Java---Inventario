import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelSheet;
import java.util.Objects;

@ExcelSheet("Productos")
public class Producto {
    @ExcelCellName("Fecha")
    private String Fecha;

    @ExcelCellName("Transaccion")
    private String Transaccion;

    @ExcelCellName("Codigo")
    private String Codigo;

    @ExcelCellName("Producto")
    private String Producto;

    @ExcelCellName("Cantidad")
    private int Cantidad;

    @ExcelCellName("Saldo")
    private int Saldo;

    // Constructor
    public Producto(String fecha, String transacion, String codigo, String producto, int cantidad, int saldo) {
        this.Fecha = fecha;
        this.Transaccion = transacion;
        this.Codigo = codigo;
        this.Producto = producto;
        this.Cantidad = cantidad;
        this.Saldo = saldo;
    }

    // Constructor sin parámetros
    public Producto() {}

    // Getters y setters
    public String getFecha() {
        return this.Fecha;
    }

    public void setFecha(String fecha) {
        this.Fecha = fecha;
    }

    public String getTransaccion() {
        return Transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.Transaccion = transaccion;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        this.Codigo = codigo;
    }

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String producto) {
        this.Producto = producto;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public int getSaldo() {
        return Saldo;
    }
    public void setSaldo (int saldo){
        this.Saldo = saldo;
    }

    // Sobrescribir equals y hashCode para comparar por Codigo
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Producto producto1 = (Producto) obj;
        return Objects.equals(Codigo, producto1.Codigo); // Compara por Codigo
    }

    @Override
    public int hashCode() {
        return Objects.hash(Codigo); // Usa Codigo para calcular el hash
    }

    @Override
    public String toString() {
        return "Producto [Fecha=" + Fecha + ", Transaccion=" + Transaccion + ", Codigo=" + Codigo + ", Producto=" + Producto + ", Cantidad=" + Cantidad + ", Saldo=" + Saldo + "]";
    }
}
