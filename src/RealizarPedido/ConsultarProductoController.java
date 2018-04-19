package RealizarPedido;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author esmeralda
 */
public class ConsultarProductoController implements Initializable {

  @FXML
  private TableView<Mueble> tablaProductos = new TableView<Mueble>();
  @FXML
  private JFXButton botonOk;
  @FXML
  private JFXButton botonAgregar;
  @FXML
  private JFXButton hamburgerMenu;
  @FXML
  private JFXDrawer drawerVenta;
  @FXML
  public TextField txProducto;
  @FXML
  public TextField txRazon;
  @FXML
  public TextField txPrecio;
  @FXML
  public TextField txCantidad;
  @FXML
  private TextField cBusqueda;
  @FXML
  private JFXButton botonFinalizar;
  private boolean pedidoActivo; 
  @FXML
  private Label avisoCuadro;
  @FXML
  public TextField txMaterial;


  private ObservableList<Mueble> datosProductos = FXCollections.observableArrayList();
  ObservableList<Mueble> listaRespaldo = FXCollections.observableArrayList();
  private PedidoController controller = new PedidoController();

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    TableColumn productoCol = new TableColumn("Producto");
    productoCol.setPrefWidth(200);
    productoCol.setCellValueFactory(new PropertyValueFactory<>("nombreMueble"));

    TableColumn proveedorCol = new TableColumn("Proveedor");
    proveedorCol.setPrefWidth(200);
    proveedorCol.setCellValueFactory(new PropertyValueFactory<>("proveedor"));

    TableColumn stockCol = new TableColumn("Stock");
    stockCol.setPrefWidth(200);
    stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
    
    tablaProductos.getColumns().addAll(productoCol,proveedorCol,stockCol);
    
    try {
      desplegarMuebles();
    } catch (SQLException ex) {
      Logger.getLogger(ConsultarProductoController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    tablaProductos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Mueble>() {
      @Override
      public void changed(ObservableValue<? extends Mueble> observable, Mueble oldValue, Mueble newValue) {
        try{
          cargarMueble();
          txCantidad.clear();
        } catch(Exception ex){
          System.out.println("Se detuvo la excepción");
        }
      }
    });
    
    tablaProductos.getSelectionModel().selectFirst();
    botonOk.setOnAction(event -> {
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
      Stage stage = (Stage) botonOk.getScene().getWindow();
      stage.close();
    } catch (IOException e) {
      System.out.println("Error de entrada y salida");
    }
    });
    
    botonAgregar.setOnAction(event -> {
      if (Integer.parseInt(txCantidad.getText()) != 0) {
        if (pedidoActivo) {
          if (controller.datosProductos.size() > 0) {
            controller.editarCantidad(Integer.parseInt(txCantidad.getText()));
          } else {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("No hay mueble seleccionado");
            alerta.show();
          }

        } else {
          if (datosProductos.size() > 0) {
            agregarAlPedido();
          } else {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("No hay mueble seleccionado");
            alerta.show();
          }
        }
      } else {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText("No puedes guardar cero");
        alerta.show();
      }
    });
    
    botonFinalizar.setOnAction(event -> {
      try {
        finalizarPedido();
        agregarProveedoresPedido();
        agregarProductosPedido();
        imprimePedido();
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(null);
        alerta.setContentText("Se genero tu pedido");
        alerta.show();
        controller.datosProductos.clear();
        txCantidad.clear();
      } catch (SQLException ex) {
        Logger.getLogger(ConsultarProductoController.class.getName()).log(Level.SEVERE, null, ex);
      } catch (FileNotFoundException ex) {
        Logger.getLogger(ConsultarProductoController.class.getName()).log(Level.SEVERE, null, ex);
      } catch (DocumentException ex) {
        Logger.getLogger(ConsultarProductoController.class.getName()).log(Level.SEVERE, null, ex);
      }
    });

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Pedido.fxml"));
      Pane pane = (Pane) loader.load();
      drawerVenta.setSidePane(pane);
      controller = loader.getController();
      controller.setOrigen(this);
    } catch (IOException ex) {
      Logger.getLogger(ConsultarProductoController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    drawerVenta.addEventFilter(MouseEvent.MOUSE_PRESSED, (e) -> {
      if (!drawerVenta.isShown()) {
        drawerVenta.toBack();
      }
    });
    
    hamburgerMenu.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
      if (drawerVenta.isShown()) {
        drawerVenta.close();
        botonAgregar.setStyle("-fx-background-color: #32CD32");
        botonAgregar.setText("Agregar");
        pedidoActivo = false; 
        tablaProductos.getSelectionModel().selectFirst();
        cargarMueble();
        txCantidad.clear();
        hamburgerMenu.setText("Ver pedido");
        hamburgerMenu.setStyle("-fx-background-color: #A9A9A9");
        cBusqueda.setDisable(false);
      } else {
        drawerVenta.open();
        drawerVenta.toFront();
        botonAgregar.setStyle("-fx-background-color: #FFA500");
        botonAgregar.setText("Actualizar");
        pedidoActivo = true; 
        controller.tablaPedido.getSelectionModel().selectFirst();
        controller.cargarMueble();
        hamburgerMenu.setText("Regresar");
        hamburgerMenu.setStyle("-fx-background-color: #00CED1");
        cBusqueda.setDisable(true);
      }
    });
    
    cBusqueda.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

        try {
          metodoDeBusqueda();
          tablaProductos.getSelectionModel().select(0);
        } catch (Exception ex) {
          Logger.getLogger(ConsultarProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
    
    txCantidad.textProperty().addListener(event -> {
      try {
        impideLetras(txCantidad);
        if (txCantidad.getText().isEmpty() || txCantidad.getText().length() > 8) {
          botonAgregar.setDisable(true);
        } else if (Integer.parseInt(txCantidad.getText()) > 0){
          botonAgregar.setDisable(false);
        }
      } catch (Exception ex) {
        System.out.println("No se puede detener la entrada de letras");
      }
    });
  }

  public void desplegarMuebles() throws SQLException {
    BaseDatos base = new BaseDatos();
    List<Mueble> muebles = base.consultarMuebles();
    for (int i = 0; i < muebles.size(); i++) {
      datosProductos.add(muebles.get(i));
      listaRespaldo.add(muebles.get(i));
    } 
    tablaProductos.setItems(datosProductos);
  }
  
  public void agregarMueble(Mueble mueble){
    datosProductos.add(mueble);
    listaRespaldo.add(mueble);
    tablaProductos.setItems(datosProductos);
  }
  
  public void agregarAlPedido() {
    try {
      Mueble seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
      seleccionado.setCantidad(Integer.parseInt(txCantidad.getText()));
      controller.agregarProductos(seleccionado);
      System.out.println("Se agrego");
      eliminarDeLista(seleccionado);
      eliminarDeListaRespaldo(seleccionado);
    } catch (Exception e) {
      Alert alerta = new Alert(Alert.AlertType.INFORMATION);
      alerta.setTitle("Error");
      alerta.setHeaderText(null);
      alerta.setContentText("Por favor, indica la cantidad");
      alerta.show();
    }
  }
  
  public void eliminarDeLista(Mueble seleccionado){
    for (int i = 0; i < datosProductos.size(); i++) {
      if (datosProductos.get(i).getIdMueble() == seleccionado.getIdMueble()) {
        datosProductos.remove(i);
        listaRespaldo.remove(i);
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(null);
        alerta.setContentText("Se agrego el producto al pedido");
        alerta.show();
      }
    }
    tablaProductos.setItems(datosProductos);
  }
  
  public void eliminarDeListaRespaldo(Mueble seleccionado){
    for (int i = 0; i < listaRespaldo.size(); i++) {
      if (listaRespaldo.get(i).getIdMueble() == seleccionado.getIdMueble()) {
        listaRespaldo.remove(i);
      }
    }
  }
  
  public void cargarMueble(){
    if (datosProductos.size() > 0) {
      Mueble seleccion = tablaProductos.getSelectionModel().getSelectedItem();
      txProducto.setText(seleccion.getNombreMueble());
      txRazon.setText(seleccion.getProveedor());
      txPrecio.setText(seleccion.getPrecio() + "");
      txMaterial.setText(seleccion.getTipo());
    } else {
      txCantidad.clear();
      txPrecio.clear();
      txProducto.clear();
      txRazon.clear();
      txMaterial.clear();
    }
  }
      
  public void metodoDeBusqueda(){
    datosProductos.clear();
    String criterio = cBusqueda.getText();
    for (int i = 0; i < listaRespaldo.size(); i++) {
      if (listaRespaldo.get(i).getProveedor().contains(criterio) || listaRespaldo.get(i).getNombreMueble().contains(criterio)) {
        datosProductos.add(listaRespaldo.get(i));
      }
    }
  }
  
  public int finalizarPedido() throws SQLException{
    Connection con = null;
    Statement sentencia = null;
    Date ahora = new Date();
    SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
    String fecha = formateador.format(ahora);
    String operacion = "INSERT INTO pedido(fechaPedido) VALUES('"+fecha+"');";

    try {
      con = new AdministrarProveedores.Conexion().connection();
      sentencia = con.createStatement();
      return sentencia.executeUpdate(operacion);
    } catch (SQLException e) {
      System.err.print("Error: " + e.getMessage() + "\n" + e.getErrorCode());
    } finally {
      con.close();
    }
    return 0;
  }
  
  public int agregarProveedoresPedido() throws SQLException{
    Connection con = null;
    Statement sentencia = null;
    int idPedido = getIdPedido();
    ArrayList<Integer> proveedores = getListaProveedores();
    String operacion = "";
    
    for (int i = 0; i < proveedores.size(); i++) {
      operacion = "INSERT INTO proveedor_has_pedido(proveedor_idProveedor,pedido_idPedido) VALUES(" + proveedores.get(i) + "," + idPedido + ");";
      try {
        con = new AdministrarProveedores.Conexion().connection();
        sentencia = con.createStatement();
        return sentencia.executeUpdate(operacion);
      } catch (SQLException e) {
        System.err.print("Error: " + e.getMessage() + "\n" + e.getErrorCode());
      } finally {
        con.close();
      }
    }
    return 0;
  }
  
  public int agregarProductosPedido() throws SQLException{
    Connection con = null;
    Statement sentencia = null;
    int idPedido = getIdPedido();
    String operacion = "";
    
    for (int i = 0; i < controller.datosProductos.size(); i++) {
      operacion = "INSERT INTO detallepedido(pedido_idPedido,mueble_idMueble,cantidad,costo) VALUES(" 
          + idPedido + ","+ controller.datosProductos.get(i).getIdMueble()
          + "," + controller.datosProductos.get(i).getCantidad()
          + ","+ controller.datosProductos.get(i).getPrecio()+ ");";
      try {
        con = new AdministrarProveedores.Conexion().connection();
        sentencia = con.createStatement();
        return sentencia.executeUpdate(operacion);
      } catch (SQLException e) {
        System.err.print("Error: " + e.getMessage() + "\n" + e.getErrorCode());
      } finally {
        con.close();
      }
    }
    return 0;
  }
  
  public int getIdPedido() throws SQLException {
    int id = 0;
    Statement s;
    Connection con = new Conexion().connection();
    ResultSet rs = null;
    String sQuery = "SELECT MAX(idPedido) AS id FROM pedido";

    try {
      s = con.createStatement();
      rs = s.executeQuery(sQuery);

      while (rs != null && rs.next()) {
        id = Integer.parseInt(String.valueOf(rs.getInt("id")));
      }
    } catch (SQLException e) {
      System.err.print("Error: " + e.getMessage() + "\n" + e.getErrorCode());
    } finally {
      con.close();
    }
    return id;
  }
  
  public ArrayList getListaProveedores(){
    ArrayList<Integer> idProveedores = new ArrayList<>();
    boolean repetido = false; 
    for (int i = 0; i < controller.datosProductos.size(); i++) {
      for (int j = 0; j < idProveedores.size(); j++) {
        if(controller.datosProductos.get(i).getIdProveedor() == idProveedores.get(j)){
          repetido = true;
        }
      }
      if (!repetido) {
        idProveedores.add(controller.datosProductos.get(i).getIdProveedor());
      }
      repetido = false;
    }
    return idProveedores;
  }

  public void imprimePedido() throws FileNotFoundException, DocumentException, SQLException {
    Date ahora = new Date();
    SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
    String fecha = formateador.format(ahora);
    ObservableList<Mueble> data = controller.datosProductos.sorted();
    int pedido = getIdPedido();
    String nombreArchivo = "pedido-"+pedido;
    FileOutputStream archivo = new FileOutputStream("C:\\Users\\Alan Yoset García C\\Desktop\\"+nombreArchivo+".pdf");
    Document documento = new Document();
    PdfWriter.getInstance(documento, archivo);
    documento.open();
    documento.add(new Paragraph("SIMU"));
    documento.add(new Paragraph("PEDIDO NÚMERO: "+pedido));
    documento.add(new Paragraph("\n FECHA:"+fecha));
    documento.add(new Paragraph("\n"));
    documento.add(new Paragraph("\n"));
    documento.add(new Paragraph("MUEBLE                   | PRECIO                     | PROVEEDOR                | CANTIDAD"));
    documento.add(new Paragraph("\n"));
    documento.add(new Paragraph("\n"));
    for (int i = 0; i < data.size(); i++) {
      documento.add(new Paragraph(data.get(i).toString()));
      documento.add(new Paragraph("----------------------------------------------------------------------------------------------------------"));
    }
    documento.close();
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
  
  public void habilitarBotonFinalizar(){
    botonFinalizar.setDisable(false);
  }
  
  public void deshabilitarBotonFinalizar(){
    botonFinalizar.setDisable(true);
  }
}

