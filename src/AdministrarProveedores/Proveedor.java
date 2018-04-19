package AdministrarProveedores;

/**
 *
 * @author yoset
 */
public class Proveedor {
  private int idProveedor;
  private String razonSocial;
  private String telefonoPrincipal;
  private String telefonoSecundario;
  private String domicilioPro;
  private int cpPro;
  private String ciudadPro;

  public Proveedor() {
  }

  public Proveedor(int idProveedor, String razonSocial, String telefonoPrincipal, String telefonoSecundario, String domicilioPro, int cpPro, String ciudadPro) {
    this.idProveedor = idProveedor;
    this.razonSocial = razonSocial;
    this.telefonoPrincipal = telefonoPrincipal;
    this.telefonoSecundario = telefonoSecundario;
    this.domicilioPro = domicilioPro;
    this.cpPro = cpPro;
    this.ciudadPro = ciudadPro;
  }

  public int getIdProveedor() {
    return idProveedor;
  }

  public void setIdProveedor(int idProveedor) {
    this.idProveedor = idProveedor;
  }

  public String getRazonSocial() {
    return razonSocial;
  }

  public void setRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
  }

  public String getTelefonoPrincipal() {
    return telefonoPrincipal;
  }

  public void setTelefonoPrincipal(String telefonoPrincipal) {
    this.telefonoPrincipal = telefonoPrincipal;
  }

  public String getTelefonoSecundario() {
    return telefonoSecundario;
  }

  public void setTelefonoSecundario(String telefonoSecundario) {
    this.telefonoSecundario = telefonoSecundario;
  }

  public String getDomicilioPro() {
    return domicilioPro;
  }

  public void setDomicilioPro(String domicilioPro) {
    this.domicilioPro = domicilioPro;
  }

  public int getCpPro() {
    return cpPro;
  }

  public void setCpPro(int cpPro) {
    this.cpPro = cpPro;
  }

  public String getCiudadPro() {
    return ciudadPro;
  }

  public void setCiudadPro(String ciudadPro) {
    this.ciudadPro = ciudadPro;
  }

  @Override
  public String toString() {
    return razonSocial;
  }
}
