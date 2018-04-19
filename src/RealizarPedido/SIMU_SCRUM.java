package RealizarPedido;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author esmeralda
 */
public class SIMU_SCRUM extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader inicio = new FXMLLoader(getClass().getResource("ConsultarProducto.fxml"));
            Parent page = inicio.load();
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
          System.out.println("Error de entrada y salida");
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
