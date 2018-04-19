package RealizarPedido;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Alan Yoset García C
 */
public class Mueble implements Comparable<Mueble>{
    private int idMueble; 
    private String nombreMueble;
    private int stock;
    private int stockMinimo;
    private String descripcion;
    private double precio;
    private int idTipo; 
    private String proveedor;
    private int cantidad;
    private int idProveedor; 
    private String tipo; 


  public Mueble(int idMueble, String nombreMueble, String proveedor, int stock, double precio, int idProveedor, String tipo) {
    this.idMueble = idMueble;
    this.nombreMueble = nombreMueble;
    this.stock = stock;
    this.proveedor = proveedor;
    this.precio = precio;
    this.idProveedor = idProveedor;
    this.tipo = tipo; 
  }

  public int getIdMueble() {
    return idMueble;
  }

  public void setIdMueble(int idMueble) {
    this.idMueble = idMueble;
  }
  
  
  public String getNombreMueble() {
    return nombreMueble;
  }

  public void setNombreMueble(String nombreMueble) {
    this.nombreMueble = nombreMueble;
  }

  public int getStock() {
    return stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }

  public int getStockMinimo() {
    return stockMinimo;
  }

  public void setStockMinimo(int stockMinimo) {
    this.stockMinimo = stockMinimo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public double getPrecio() {
    return precio;
  }

  public void setPrecio(double precio) {
    this.precio = precio;
  }

  public int getIdTipo() {
    return idTipo;
  }

  public void setIdTipo(int idTipo) {
    this.idTipo = idTipo;
  }

  public String getProveedor() {
    return proveedor;
  }

  public void setProveedor(String proveedor) {
    this.proveedor = proveedor;
  }

  public int getCantidad() {
    return cantidad;
  }

  public void setCantidad(int cantidad) {
    this.cantidad = cantidad;
  } 

  public int getIdProveedor() {
    return idProveedor;
  }

  public void setIdProveedor(int idProveedor) {
    this.idProveedor = idProveedor;
  }

  @Override
  public int compareTo(Mueble o) {
    if (idProveedor < o.idProveedor) {
      return -1;
    }
    if (idProveedor > o.idProveedor) {
      return 1;
    }
    return 0;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }
  

  @Override
  public String toString() {
    return nombreMueble + "                          " + precio + "                          " + proveedor + "                               " + cantidad;
  }
  
  
}
