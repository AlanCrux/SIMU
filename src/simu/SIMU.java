package simu;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Alan Yoset García C
 */
public class SIMU extends Application {
  private Stage stagePrincipal;
  private AnchorPane rootPane;
  
  @Override
  public void start(Stage stagePrincipal) throws Exception {
    this.stagePrincipal = stagePrincipal;
    mostrarVentanaPrincipal();

  }

  public void mostrarVentanaPrincipal() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
      rootPane = (AnchorPane) loader.load();
      Scene scene = new Scene(rootPane);
      stagePrincipal.setTitle("Ventana Principal");
      stagePrincipal.setScene(scene);
      simu.MainMenuController controller = loader.getController();
      controller.setOrigen(stagePrincipal);
      stagePrincipal.show();
    } catch (IOException e) {
      System.out.println("Error de entrada y salida");
    }
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
  
}
