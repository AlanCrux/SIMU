package RealizarPedido;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alan Yoset García C
 */
public class BaseDatos {

  private Connection conecta = null;
  private Statement s;
  ResultSet rs = null;

  public List<Mueble> consultarMuebles() throws SQLException {
    List<Mueble> lista = new ArrayList<>();
    conecta = new Conexion().connection();
    String sQuery = "select mueble.idMueble,mueble.nombreMueble,proveedor.idProveedor,proveedor.razonSocial,mueble.stock,mueble.precio,tipo.tipo from mueble,proveedor,tipo where mueble.proveedor_idProveedor=proveedor.idProveedor and "
        + "mueble.stock<=mueble.stockMinimo and mueble.Tipo_idTipo=tipo.idTipo;";

    try {
      s = conecta.createStatement();
      rs = s.executeQuery(sQuery);

      while (rs != null && rs.next()) {
        int id = Integer.parseInt(String.valueOf(rs.getInt("idMueble")));
        String nombreMueble = rs.getString("nombreMueble");
        int idProveedor = Integer.parseInt(String.valueOf(rs.getInt("idProveedor")));
        String proveedor = rs.getString("razonSocial");
        int stock = Integer.parseInt(String.valueOf(rs.getInt("stock")));
        double precio = Double.parseDouble(String.valueOf(rs.getInt("precio")));
        String tipo = rs.getString("tipo");
        lista.add(new Mueble(id,nombreMueble, proveedor, stock, precio,idProveedor,tipo));
      }

    } catch (SQLException e) {
      System.err.print("Error: " + e.getMessage() + "\n" + e.getErrorCode());
    } finally {
      conecta.close();
    }
    return lista;
  }
  
}
