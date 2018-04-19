package RealizarPedido;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 * FXML Controller class
 *
 * @author Alan Yoset García C
 */
public class PedidoController implements Initializable {

  @FXML public TableView<Mueble> tablaPedido;
  @FXML private Button botonEliminar;
  private ConsultarProductoController origen;
  public final ObservableList<Mueble> datosProductos = FXCollections.observableArrayList();

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    TableColumn productoCol = new TableColumn("Producto");
    productoCol.setPrefWidth(200);
    productoCol.setCellValueFactory(new PropertyValueFactory<>("nombreMueble"));

    TableColumn cantidadCol = new TableColumn("Cantidad");
    cantidadCol.setPrefWidth(200);
    cantidadCol.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

    TableColumn precioCol = new TableColumn("Precio");
    precioCol.setPrefWidth(200);
    precioCol.setCellValueFactory(new PropertyValueFactory<>("precio"));
    tablaPedido.getColumns().addAll(productoCol,cantidadCol,precioCol);
    
    botonEliminar.setOnAction(event -> {
      eliminarTabla();
    });
    
    tablaPedido.getSelectionModel().selectedItemProperty().addListener(event -> {
      try {
        cargarMueble();
        if (datosProductos.size() > 0) {
          origen.habilitarBotonFinalizar();
        } else {
          origen.deshabilitarBotonFinalizar();
        }
      } catch (Exception ex) {
        System.out.println("Se detuvo la excepción");
      }
    });
  }  
  
  public void eliminarTabla() {
    Mueble seleccionado = tablaPedido.getSelectionModel().getSelectedItem();
    for (int i = 0; i < datosProductos.size(); i++) {
      if (datosProductos.get(i).getIdMueble() == seleccionado.getIdMueble()) {
        datosProductos.remove(i);
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(null);
        alerta.setContentText("Se elimino el producto del pedido");
        alerta.show();
      }
    }
    origen.agregarMueble(seleccionado);
  }
  
  public void agregarProductos(Mueble mueble){
    datosProductos.add(mueble);
    tablaPedido.setItems(datosProductos);
  }
  
  public void setOrigen(ConsultarProductoController origen){
    this.origen = origen; 
  }
  
  public void cargarMueble(){
    if (datosProductos.size() > 0) {
      Mueble seleccion = tablaPedido.getSelectionModel().getSelectedItem();
      origen.txProducto.setText(seleccion.getNombreMueble());
      origen.txRazon.setText(seleccion.getProveedor());
      origen.txPrecio.setText(seleccion.getPrecio() + "");
      origen.txCantidad.setText(seleccion.getCantidad() + "");
      origen.txMaterial.setText(seleccion.getTipo());
    } else {
      origen.txCantidad.clear();
      origen.txPrecio.clear();
      origen.txProducto.clear();
      origen.txRazon.clear();
      origen.txMaterial.clear();
    }
  }
  
  public void editarCantidad(int cantidad){
    Mueble seleccion = tablaPedido.getSelectionModel().getSelectedItem();
    if (cantidad != seleccion.getCantidad()) {
      tablaPedido.getSelectionModel().getSelectedItem().setCantidad(cantidad);
      tablaPedido.refresh();
      Alert alerta = new Alert(Alert.AlertType.INFORMATION);
      alerta.setTitle("Error");
      alerta.setHeaderText(null);
      alerta.setContentText("Se actualizo la cantidad");
      alerta.show();
    } else {
      Alert alerta = new Alert(Alert.AlertType.INFORMATION);
      alerta.setTitle("Error");
      alerta.setHeaderText(null);
      alerta.setContentText("No se modifico la cantidad");
      alerta.show();
    }
  }
}
