package app

import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafxml.core.FXMLLoader
import repository.{FoodRepository, UserRepository}
import scalafx.scene.image.Image
import java.util.ResourceBundle
import scalafx.Includes._

object Main extends JFXApp3:
  override def start(): Unit =
    FoodRepository.setup()
    UserRepository.setup()

    val resources = ResourceBundle.getBundle("messages")
    val loader = new FXMLLoader(getClass.getResource("/login.fxml"), resources)
    val root = loader.load[scalafx.scene.Parent]

    stage = new JFXApp3.PrimaryStage:
      title = resources.getString("login.title")
      scene = new Scene(root)
      icons += new Image(getClass.getResource("/images/login-icon.jpg").toExternalForm)
    stage.show()
