package AdministrarProveedores;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author yoset
 */
public class ProyectoSIMU extends Application {

  @Override
  public void start(Stage primaryStage) {
    try {
      FXMLLoader inicio = new FXMLLoader(getClass().getResource("GraphicAdministrarProveedores.fxml"));

      Parent page = inicio.load();

      Scene scene = new Scene(page);
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (Exception e) {
    }

  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

}
