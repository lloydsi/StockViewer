package sample;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
/**
 * Created by sian- on 11/03/2017.
 */
public class MessageBox {
    String message;
    String title;

        public static void show (String message, String title) {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle(title);
            stage.setMinWidth(400);
            stage.setMinHeight(200);

            Label lbl = new Label();
            lbl.setText(message);

            Button btnOK = new Button();
            btnOK.setText("OK");
            btnOK.setOnAction(e -> stage.close());

            VBox pane = new VBox(20);
            pane.getChildren().addAll(lbl, btnOK);
            pane.setAlignment(Pos.TOP_CENTER);

            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.showAndWait();



        }

    }


