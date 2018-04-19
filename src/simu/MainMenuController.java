package simu;

import RealizarPedido.ConsultarProductoController;
import AdministrarProveedores.GraphicAdministrarProveedoresController;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Alan Yoset García C
 */
public class MainMenuController implements Initializable {

  @FXML
  JFXButton botonProveedores;
  @FXML
  private JFXButton botonPedido;
  @FXML
  private JFXButton botonSalir;
  
  private Stage origen; 


  /**
   * Initializes the controller class.
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    botonProveedores.setOnAction(event -> {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdministrarProveedores/GraphicAdministrarProveedores.fxml"));
        GraphicAdministrarProveedoresController controller = loader.getController();
        AnchorPane ventanaDos = (AnchorPane) loader.load();
        Scene scene = new Scene(ventanaDos);
        Stage ventana = new Stage();
        ventana.setTitle("Control de proveedores");
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setScene(scene);
        ventana.show();
        ventana.setResizable(false);
        origen.close();
      } catch (IOException ex) {
        Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
      }
    });
    
    botonPedido.setOnAction(event -> {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RealizarPedido/ConsultarProducto.fxml"));
        ConsultarProductoController controller = loader.getController();
        AnchorPane ventanaDos = (AnchorPane) loader.load();
        Scene scene = new Scene(ventanaDos);
        Stage ventana = new Stage();
        ventana.setTitle("Nuevo pedido");
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setScene(scene);
        ventana.show();
        ventana.setResizable(false);
        origen.close();
      } catch (IOException ex) {
        Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
      }
    });
    
    botonSalir.setOnAction(event -> {
      Platform.exit();
    });
  }  
  
  public void setOrigen(Stage origen){
    this.origen = origen;
  }
}
