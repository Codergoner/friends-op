package app

import scalafx.application.JFXApp3
import scalafx.fxml.FXMLLoader
import scalafx.scene.{Parent, Scene}
import scalafx.scene.image.Image
import repository.FoodRepository

/**
 * Application entry point. Loads the login view from FXML and initializes the database.
 */
object Main extends JFXApp3:

  override def start(): Unit =
    // Initialize persistent storage
    FoodRepository.setup()

    // Load the login interface defined in FXML
    val root: Parent = FXMLLoader.load(getClass.getResource("/login.fxml"))

    stage = new JFXApp3.PrimaryStage:
      title = "Login"
      icons += new Image(getClass.getResource("/images/login-icon.jpg").toExternalForm)
      scene = new Scene(root, 400, 300)

