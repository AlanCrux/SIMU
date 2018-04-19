package AdministrarProveedores;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author yoset
 */
public class GraphicAdministrarProveedoresController implements Initializable {

  @FXML
  private ListView<Proveedor> listaProveedores;
  @FXML
  private TextField cBusqueda;
  @FXML
  private Button botonAgregar;
  @FXML
  private Button botonEditar;
  @FXML
  private Button botonEliminar;
  @FXML
  private TextField cClave;
  @FXML
  private TextField cRazon;
  @FXML
  private TextField cTelUno;
  @FXML
  private TextField cCalle;
  @FXML
  private TextField cCiudad;
  @FXML
  private TextField cCp;
  @FXML
  private TextField cTelDos;
  @FXML
  private ListView<Proveedor> listaProductos;
  @FXML
  private JFXButton botonAtras;
  private final ObservableList<Proveedor> virtualProveedor = FXCollections.observableArrayList();

  //BANDERAS
  private boolean bandAgregar;
  private boolean bandEditar;

  public GraphicAdministrarProveedoresController() {
    llenarListView();
  }
  
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    
    listaProveedores.setItems(virtualProveedor);
    listaProveedores.getSelectionModel().select(0);
    cargarProveedor();
    
    listaProveedores.getSelectionModel().selectedItemProperty().addListener(event -> {
      try {
        if (virtualProveedor.isEmpty()) {
          vaciarCuadros(); 
        } else {
          cargarProveedor();
        }
      } catch (Exception ex) {
        System.out.println("No pasa nada");
      }
    });
    
    botonAgregar.setOnAction(event -> {
      if (bandAgregar) {
        boolean existe = false;
        try {
          existe = verificarSiExiste();
        } catch (SQLException ex) {
          System.err.println("Error método verificarSiExiste()");
        }
        if (existe) {
          genAlert("Información duplicada", "Ya existe un proveedor con datos identicos en la base de datos");
        } else {
          bandAgregar = false;
          protegerCuadros();
          try {
            agregarNuevoLista();
          } catch (SQLException ex) {
            System.err.println("Error metodo agregarNuevoLista()");
          }
          virtualProveedor.remove(virtualProveedor.size() - 1);
          llenarListView();
          listaProveedores.getSelectionModel().selectLast();
          botonAgregar.setText("Agregar");
          botonEditar.setDisable(false);
          cBusqueda.setDisable(false);
          listaProveedores.setDisable(false);
        }

      } else {
        bandAgregar = true;
        botonEditar.setDisable(true);
        cBusqueda.setDisable(true);
        listaProveedores.setDisable(true);
        addNew();
        cRazon.requestFocus();
      }
    });
    
    botonEliminar.setOnAction((event) -> {
      if (virtualProveedor.isEmpty()) {
        genAlert("Error", "No hay elementos que eliminar");
      } else {
        if (bandAgregar) {
          bandAgregar = false;
          botonAgregar.setText("Agregar");
          botonAgregar.setDisable(false);
          botonEditar.setDisable(false);
          cBusqueda.setDisable(false);
          listaProveedores.setDisable(false);
          Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
          confirmacion.setTitle("Confirmación");
          confirmacion.setHeaderText(null);
          confirmacion.setContentText("Confirma que deseas eliminar el proveedor");
          ButtonType btEliminar = new ButtonType("Eliminar");
          ButtonType btCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

          confirmacion.getButtonTypes().setAll(btEliminar, btCancelar);
          Optional<ButtonType> eleccion = confirmacion.showAndWait();

          if (eleccion.get() == btEliminar) {
            try {
              eliminar();
              genAlert("SIMU - Delete", "Eliminación exitosa");
              llenarListView();
              
              listaProveedores.getSelectionModel().select(0);
              protegerCuadros();
            } catch (SQLException ex) {
              Logger.getLogger(GraphicAdministrarProveedoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        } else {
          Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
          confirmacion.setTitle("Confirmación");
          confirmacion.setHeaderText(null);
          confirmacion.setContentText("Confirma que deseas eliminar el proveedor");
          ButtonType btEliminar = new ButtonType("Eliminar");
          ButtonType btCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

          confirmacion.getButtonTypes().setAll(btEliminar, btCancelar);
          Optional<ButtonType> eleccion = confirmacion.showAndWait();

          if (eleccion.get() == btEliminar) {
            try {
              eliminar();
              genAlert("SIMU - Delete", "Eliminación exitosa");
              llenarListView();
              listaProveedores.setDisable(false);
              listaProveedores.getSelectionModel().select(0);
              protegerCuadros();
            } catch (SQLException ex) {
              Logger.getLogger(GraphicAdministrarProveedoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }
      }
    });

    botonEditar.setOnAction(event -> {
      if (virtualProveedor.isEmpty()) {
        System.out.println("No hay nada");
      } else {
        if (bandEditar) {
          bandEditar = false;
          try {
            editar();
            genAlert("SIMU - Edit", "Se actualizaron los datos");
          } catch (SQLException ex) {
            Logger.getLogger(GraphicAdministrarProveedoresController.class.getName()).log(Level.SEVERE, null, ex);
          }
          botonEditar.setText("Editar");
          protegerCuadros();
          llenarListView();

          listaProveedores.getSelectionModel().select(0);
          botonAgregar.setDisable(false);
          cBusqueda.setDisable(false);
          listaProveedores.setDisable(false);
          botonEliminar.setDisable(false);
        } else {
          bandEditar = true;
          desprotegerCuadros();
          botonEditar.setText("Refresh");
          botonAgregar.setDisable(true);
          botonEliminar.setDisable(true);
          cBusqueda.setDisable(true);
          listaProveedores.setDisable(true);
        }
      }
    });
    
    botonAtras.setOnAction(event -> {
      Stage stagePrincipal = new Stage();
      AnchorPane rootPane;
      try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/simu/MainMenu.fxml"));
      rootPane = (AnchorPane) loader.load();
      Scene scene = new Scene(rootPane);
      stagePrincipal.setTitle("Ventana Principal");
      stagePrincipal.setScene(scene);
      simu.MainMenuController controller = loader.getController();
      controller.setOrigen(stagePrincipal);
      stagePrincipal.show();
      Stage stage = (Stage) botonAtras.getScene().getWindow();
      stage.close();
      cuadrosVacios();
    } catch (IOException e) {
      System.out.println("Error de entrada y salida");
    }
    });
    
    cRazon.textProperty().addListener(event -> {
      try {
        impideNumeros(cRazon);
        if (bandAgregar) {
            if (cuadrosVacios()) {
              botonAgregar.setDisable(true);
            } else {
              botonAgregar.setDisable(false);
            }
          } else if(bandEditar) {
            if (cuadrosVacios()) {
              botonEditar.setDisable(true);
            } else {
              botonEditar.setDisable(false);
            }
          }
      } catch (Exception e) {
        System.out.println("No hay error");
      }
    });
    
    cTelUno.textProperty().addListener(event -> {
      try {
        impideLetras(cTelUno);
        if (bandAgregar) {
            if (cuadrosVacios()) {
              botonAgregar.setDisable(true);
            } else {
              botonAgregar.setDisable(false);
            }
          } else if(bandEditar) {
            if (cuadrosVacios()) {
              botonEditar.setDisable(true);
            } else {
              botonEditar.setDisable(false);
            }
          }
      } catch (Exception e) {
        System.out.println("No hay error");
      }
    });
    
    cTelDos.textProperty().addListener(event -> {
      try {
        impideLetras(cTelDos);
        if (bandAgregar) {
            if (cuadrosVacios()) {
              botonAgregar.setDisable(true);
            } else {
              botonAgregar.setDisable(false);
            }
          } else if(bandEditar) {
            if (cuadrosVacios()) {
              botonEditar.setDisable(true);
            } else {
              botonEditar.setDisable(false);
            }
          }
      } catch (Exception e) {
        System.out.println("No hay error");
      }
    });
    
    cCiudad.textProperty().addListener(event -> {
      try {
          impideNumeros(cCiudad);
        if (bandAgregar) {
            if (cuadrosVacios()) {
              botonAgregar.setDisable(true);
            } else {
              botonAgregar.setDisable(false);
            }
          } else if(bandEditar) {
            if (cuadrosVacios()) {
              botonEditar.setDisable(true);
            } else {
              botonEditar.setDisable(false);
            }
          }
      } catch (Exception e) {
        System.out.println("No hay error");
      }
    });
    
    cCp.textProperty().addListener(event -> {
      try {
        if (cCp.isEditable() == true) {
          impideLetras(cCp);
          if (bandAgregar) {
            if (cuadrosVacios()) {
              botonAgregar.setDisable(true);
            } else {
              botonAgregar.setDisable(false);
            }
          } else if(bandEditar) {
            if (cuadrosVacios()) {
              botonEditar.setDisable(true);
            } else {
              botonEditar.setDisable(false);
            }
          }
        } else {
          pintarCuadros();
          System.out.println("entro else");
        }

      } catch (Exception e) {
        System.out.println("No hay error");
      }
    });
    
    cCalle.textProperty().addListener(event -> {
      if (cuadrosVacios()) {
        botonAgregar.setDisable(true);
      } else {
        botonAgregar.setDisable(false);
      }
    });
    
    cBusqueda.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (bandAgregar || bandEditar) {
          genAlert("Acción no permitida", "Primero termina la operacion actual");
          cBusqueda.setText("");
        } else {
          try {
            metodoDeBusqueda();
          } catch (SQLException ex) {
            Logger.getLogger(GraphicAdministrarProveedoresController.class.getName()).log(Level.SEVERE, null, ex);
          }
          try{
            listaProductos.getSelectionModel().select(0);
          } catch (Exception es){
            System.err.println("Error al seleccionar de la lista");
          }
          
        }
      }
    });
    
  }

  public void llenarListView() {
    List<Proveedor> consultaProveedor = null;
    
    try{
      consultaProveedor = listaProveedores();
    } catch(SQLException ex){
      Logger.getLogger(GraphicAdministrarProveedoresController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    virtualProveedor.clear();
    for (int i = 0; i < consultaProveedor.size(); i++) {
      virtualProveedor.add(consultaProveedor.get(i));
    }
  }

  private List<Proveedor> listaProveedores() throws SQLException {
    List<Proveedor> lista = new ArrayList<Proveedor>();
    Statement s;
    Connection conecta = new Conexion().connection();
    ResultSet rs = null;
    String sQuery = "select idProveedor,razonSocial,telefonoPrincipal,telefonoSecundario,domicilioPro,cpPro,ciudadPro from Proveedor;";

    try {
      s = conecta.createStatement();
      rs = s.executeQuery(sQuery);

      while (rs != null && rs.next()) {
        int id = Integer.parseInt(String.valueOf(rs.getInt("idProveedor")));
        String razonSocial = rs.getString("razonSocial");
        String telefonoPrincipal = rs.getString("telefonoPrincipal");
        String telefonoSecundario = rs.getString("telefonoSecundario");
        String domicilioPro = rs.getString("domicilioPro");
        int cpPro = Integer.parseInt(rs.getString("cpPro"));
        String ciudadPro = rs.getString("ciudadPro");
        
        lista.add(new Proveedor(id,razonSocial,telefonoPrincipal,telefonoSecundario,domicilioPro,cpPro,ciudadPro));
      }

    } catch (SQLException e) {
      System.err.print("Error: " + e.getMessage() + "\n" + e.getErrorCode());
    } finally {
      conecta.close();
    }
    return lista;
  }
  
  public void cargarProveedor(){
    Proveedor seleccion = listaProveedores.getSelectionModel().getSelectedItem();
    cRazon.setText(seleccion.getRazonSocial());
    cTelUno.setText(seleccion.getTelefonoPrincipal());
    cTelDos.setText(seleccion.getTelefonoSecundario());
    cCalle.setText(seleccion.getDomicilioPro());
    cCiudad.setText(seleccion.getCiudadPro());
    cCp.setText(seleccion.getCpPro()+"");
  }
  
  public void protegerCuadros(){
    cRazon.setEditable(false);
    cTelUno.setEditable(false);
    cTelDos.setEditable(false);
    cCalle.setEditable(false);
    cCiudad.setEditable(false);
    cCp.setEditable(false);
    
    cRazon.setStyle("-fx-border-color: #DCDCDC;");
    cTelUno.setStyle("-fx-border-color: #DCDCDC;");
    cTelDos.setStyle("-fx-border-color: #DCDCDC;");
    cCalle.setStyle("-fx-border-color: #DCDCDC;");
    cCiudad.setStyle("-fx-border-color: #DCDCDC;");
    cCp.setStyle("-fx-border-color: #DCDCDC;");
  }
  
  public void desprotegerCuadros(){
    cRazon.setEditable(true);
    cTelUno.setEditable(true);
    cTelDos.setEditable(true);
    cCalle.setEditable(true);
    cCiudad.setEditable(true);
    cCp.setEditable(true);
    
    cRazon.setStyle("-fx-border-color: #00FFFF;");
    cTelUno.setStyle("-fx-border-color: #00FFFF;");
    cTelDos.setStyle("-fx-border-color: #00FFFF;");
    cCalle.setStyle("-fx-border-color: #00FFFF;");
    cCiudad.setStyle("-fx-border-color: #00FFFF;");
    cCp.setStyle("-fx-border-color: #00FFFF;");
  }
  
  public void addNew(){
    Proveedor nueva = new Proveedor();
    nueva.setRazonSocial("Nuevo Proveedor");
    virtualProveedor.add(nueva);
    listaProveedores.getSelectionModel().select(virtualProveedor.size()-1);
    
    desprotegerCuadros();
    cRazon.setText("");
    cTelUno.setText("");
    cTelDos.setText("");
    cCalle.setText("");
    cCiudad.setText("");
    cCp.setText("");
    
    botonAgregar.setText("Guardar");
  }
  
  public int agregarNuevoLista() throws SQLException {
    Connection con = null;
    Statement sentencia = null;
    String operacion = "INSERT INTO Proveedor(razonSocial, telefonoPrincipal, telefonoSecundario, "
        + "domicilioPro, cpPro, ciudadPro) VALUES ('" + cRazon.getText() + "','" + cTelUno.getText() + "','"+ 
        cTelDos.getText() + "','" + cCalle.getText() + "'," + Integer.parseInt(cCp.getText()) + ",'" + cCiudad.getText()+"');";

    try {
      con = new Conexion().connection();
      sentencia = con.createStatement();
      return sentencia.executeUpdate(operacion);
    } catch (SQLException e) {
      System.err.print("Error: " + e.getMessage() + "\n" + e.getErrorCode());
    } finally {
      con.close();
    }
    return 0;
  }

  public boolean cuadrosVacios(){
    try {
      if (cRazon.getText().isEmpty() || cTelUno.getText().isEmpty() || cCalle.getText().isEmpty() || cCiudad.getText().isEmpty()) {
        return true;
      }

      if (cCp.getText().length() != 5) {
        cCp.setStyle("-fx-border-color: #FF0000");
        return true;
      } else {
        cCp.setStyle("-fx-border-color: #00FFFF;");
      }
      
      if (cTelUno.getText().length() < 7 || cTelUno.getText().length() >10) {
        cTelUno.setStyle("-fx-border-color: #FF0000");
        return true;
      } else {
        cTelUno.setStyle("-fx-border-color: #00FFFF;");
      }
      
      if ((cTelDos.getText().length() < 7 || cTelDos.getText().length() >10) && (!(cTelDos.getText().isEmpty()))) {
        cTelDos.setStyle("-fx-border-color: #FF0000");
        return true;
      } else {
        cTelDos.setStyle("-fx-border-color: #00FFFF;");
      }
      
    } catch (Exception ex){
      System.err.println("error cuadros");
    }
    
    return false; 
  }
  
  public void genAlert(String cabeza, String cuerpo) {
    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
    alerta.setTitle(cabeza);
    alerta.setHeaderText(null);
    alerta.setContentText(cuerpo);
    alerta.show();
  }
  
  public int eliminar() throws SQLException {
    Connection con = null; 
    Statement sentencia = null; 
    Proveedor seleccion = listaProveedores.getSelectionModel().getSelectedItem();
    String operacion = "delete from Proveedor where idProveedor=" + seleccion.getIdProveedor() + ";";
    try {
      con = new Conexion().connection();
      sentencia = con.createStatement();
      return sentencia.executeUpdate(operacion);
    } catch (SQLException e) {
      System.err.print("Error: " + e.getMessage() + "\n" + e.getErrorCode());
    } finally {
      con.close();
    }
    return 0; 
  }
  
  public int editar() throws SQLException {
    Connection con = null;
    Statement sentencia = null;
    Proveedor seleccion = listaProveedores.getSelectionModel().getSelectedItem();

    String operacion = "UPDATE Proveedor SET "
        + "razonSocial='" + cRazon.getText() + "',"
        + "telefonoPrincipal='" + cTelUno.getText() + "',"
        + "telefonoSecundario='" + cTelDos.getText() + "',"
        + "domicilioPro='" + cCalle.getText() + "',"
        + "cpPro='" + cCp.getText() + "',"
        + "ciudadPro='" + cCiudad.getText() + "' where idProveedor=" + seleccion.getIdProveedor() + ";";

    try {
      con = new Conexion().connection();
      sentencia = con.createStatement();
      return sentencia.executeUpdate(operacion);
    } catch (SQLException e) {
      System.err.print("Error: " + e.getMessage() + "\n" + e.getErrorCode());
    } finally {
      con.close();
    }
    return 0;
  }
  
  public void impideNumeros(TextField tapPaterno) {
    String str = tapPaterno.getText();
    char[] fuente = str.toCharArray();
    char[] resultado = new char[fuente.length];
    int j = 0;
    boolean error = false;

    for (int i = 0; i < fuente.length; i++) {
      if (fuente[i] == 'A' || fuente[i] == 'B' || fuente[i] == 'C' || fuente[i] == 'D' || fuente[i] == 'E' || fuente[i] == 'F' || fuente[i] == 'G' || fuente[i] == 'H' || fuente[i] == 'I' || fuente[i] == 'J' || fuente[i] == 'K' || fuente[i] == 'L' || fuente[i] == 'M' || fuente[i] == 'N' || fuente[i] == 'O' || fuente[i] == 'P' || fuente[i] == 'Q' || fuente[i] == 'R' || fuente[i] == 'S' || fuente[i] == 'T' || fuente[i] == 'U' || fuente[i] == 'V' || fuente[i] == 'W' || fuente[i] == 'X' || fuente[i] == 'Y' || fuente[i] == 'Z' || fuente[i] == 'a' || fuente[i] == 'b' || fuente[i] == 'c' || fuente[i] == 'd' || fuente[i] == 'e' || fuente[i] == 'f' || fuente[i] == 'g' || fuente[i] == 'h' || fuente[i] == 'i' || fuente[i] == 'j' || fuente[i] == 'k' || fuente[i] == 'l' || fuente[i] == 'm' || fuente[i] == 'n' || fuente[i] == 'o' || fuente[i] == 'p' || fuente[i] == 'q' || fuente[i] == 'r' || fuente[i] == 's' || fuente[i] == 't' || fuente[i] == 'u' || fuente[i] == 'v' || fuente[i] == 'w' || fuente[i] == 'x' || fuente[i] == 'y' || fuente[i] == 'z' || fuente[i] == ' ') {
        resultado[j++] = fuente[i];
      } else {
        error = true;
        java.awt.Toolkit.getDefaultToolkit().beep();
      }
    }

    if (error) {
      tapPaterno.setText("");
      tapPaterno.setText(new String(resultado, 0, j));
    }
  }
  
  private void impideLetras(TextField tcp) {
    String str = tcp.getText();
    char[] fuente = str.toCharArray();
    char[] resultado = new char[fuente.length];
    int j = 0;
    boolean error = false;

    for (int i = 0; i < fuente.length; i++) {
      if (fuente[i] == '0' || fuente[i] == '1' || fuente[i] == '2' || fuente[i] == '3' || fuente[i] == '4' || fuente[i] == '5' || fuente[i] == '6' || fuente[i] == '7' || fuente[i] == '8' || fuente[i] == '9') {
        resultado[j++] = fuente[i];
      } else {
        error = true;
        java.awt.Toolkit.getDefaultToolkit().beep();
      }
    }

    if (error) {
      tcp.setText("");
      tcp.setText(new String(resultado, 0, j));
    }
  }
  
  public void metodoDeBusqueda() throws SQLException{
    String criterio = cBusqueda.getText();
    virtualProveedor.clear();
    
    List<Proveedor> lista = new ArrayList<Proveedor>();
    Statement s;
    Connection conecta = new Conexion().connection();
    ResultSet rs = null;
    String sQuery = "select idProveedor,razonSocial,telefonoPrincipal,telefonoSecundario,domicilioPro,cpPro,ciudadPro from Proveedor where idProveedor LIKE '%"+criterio+"%' OR razonSocial LIKE '%"+criterio+"%';";

    try {
      s = conecta.createStatement();
      rs = s.executeQuery(sQuery);

      while (rs != null && rs.next()) {
        int id = Integer.parseInt(String.valueOf(rs.getInt("idProveedor")));
        String razonSocial = rs.getString("razonSocial");
        String telefonoPrincipal = rs.getString("telefonoPrincipal");
        String telefonoSecundario = rs.getString("telefonoSecundario");
        String domicilioPro = rs.getString("domicilioPro");
        int cpPro = Integer.parseInt(rs.getString("cpPro"));
        String ciudadPro = rs.getString("ciudadPro");
        
        lista.add(new Proveedor(id,razonSocial,telefonoPrincipal,telefonoSecundario,domicilioPro,cpPro,ciudadPro));
      }

    } catch (SQLException e) {
      System.err.print("Error: " + e.getMessage() + "\n" + e.getErrorCode());
    } finally {
      conecta.close();
    }
    virtualProveedor.clear();
    virtualProveedor.setAll(lista);
    
  }
  
  public boolean verificarSiExiste() throws SQLException {
    Statement s;
    Connection conecta = new Conexion().connection();
    ResultSet rs = null;
     String sQuery = "select idProveedor,razonSocial,telefonoPrincipal,telefonoSecundario,domicilioPro,cpPro,ciudadPro "
         + "from Proveedor "
         + "where razonSocial='"+cRazon.getText()+"' AND "
         + "telefonoPrincipal='"+cTelUno.getText()+"' AND "
         + "telefonoSecundario='"+cTelDos.getText()+"' AND "
         + "domicilioPro='"+cCalle.getText()+"' AND "
         + "cpPro="+cCp.getText()+" AND "
         + "ciudadPro='"+cCiudad.getText()+"';";

    try {
      s = conecta.createStatement();
      rs = s.executeQuery(sQuery);

      while (rs != null && rs.next()) {
        if (rs.getString("razonSocial") != "") {
          return true;
        }
      }

    } catch (SQLException e) {
      System.err.print("Error: " + e.getMessage() + "\n" + e.getErrorCode());
    } finally {
      conecta.close();
    }
    return false; 
  }
  
  public void vaciarCuadros(){
    cClave.clear();
    cRazon.clear();
    cTelUno.clear();
    cTelDos.clear();
    cCalle.clear();
    cCiudad.clear();
    cCp.clear();
  }
  
  public void pintarCuadros(){
    cRazon.setStyle("-fx-border-color: #DCDCDC;");
    cTelUno.setStyle("-fx-border-color: #DCDCDC;");
    cTelDos.setStyle("-fx-border-color: #DCDCDC;");
    cCalle.setStyle("-fx-border-color: #DCDCDC;");
    cCiudad.setStyle("-fx-border-color: #DCDCDC;");
    cCp.setStyle("-fx-border-color: #DCDCDC;");
  }
  
}
