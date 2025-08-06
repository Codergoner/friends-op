package view

import scalafx.stage.{Modality, Stage}
import scalafx.scene.Scene
import javafx.fxml.FXMLLoader
import model.FoodItem
import scalafx.scene.image.Image
import java.util.ResourceBundle

object AddFoodDialog:
  def show(onAdd: FoodItem => Unit): Unit =
    val loader = new FXMLLoader(
      getClass.getResource("/view/AddFoodDialog.fxml"),
      ResourceBundle.getBundle("i18n.messages")
    )
    val root = loader.load[javafx.scene.layout.VBox]()
    val controller = loader.getController[AddFoodDialogController]
    val stage = new Stage:
      initModality(Modality.ApplicationModal)
      title = loader.getResources.getString("addFood.title")
      scene = new Scene(root)
      icons += new Image(getClass.getResource("/images/login-icon.jpg").toString)
    controller.dialogStage = stage
    controller.onSave = onAdd
    stage.showAndWait()

