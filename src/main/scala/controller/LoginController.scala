// src/main/scala/controller/LoginController.scala
package controller

import javafx.fxml.FXML
import model.{Admin, RegularUser, User}
import scalafx.scene.control.{Alert, Label, PasswordField, TextField}
import scalafx.stage.Stage
import scalafx.scene.paint.Color
import view.{AdminDashboard, UserDashboard}

/** Controller for the login view loaded from FXML. */
class LoginController:

  @FXML private var usernameField: TextField = _
  @FXML private var passwordField: PasswordField = _
  @FXML private var statusLabel: Label = _

  // Hardcoded users (later we can load from DB or file)
  private val users: List[User] = List(
    Admin("admin", "admin123"),
    RegularUser("user", "user123")
  )

  /** Triggered when the login button is pressed. Performs validation and navigation. */
  @FXML def handleLogin(): Unit =
    val username = usernameField.text.value
    val password = passwordField.text.value

    if username.isEmpty || password.isEmpty then
      new Alert(Alert.AlertType.Warning):
        title = "Missing Credentials"
        headerText = "Please enter username and password"
      .showAndWait()
    else
      authenticate(username, password) match
        case Some(Admin(_, _)) =>
          statusLabel.textFill = Color.Green
          statusLabel.text = s"Welcome, Admin"
          usernameField.scene().window.asInstanceOf[Stage].close()
          AdminDashboard.show()
        case Some(RegularUser(_, _)) =>
          statusLabel.textFill = Color.Blue
          statusLabel.text = s"Welcome, User"
          usernameField.scene().window.asInstanceOf[Stage].close()
          new UserDashboard().show()
        case None =>
          new Alert(Alert.AlertType.Error):
            title = "Login Failed"
            headerText = "Invalid username or password"
          .showAndWait()

  def authenticate(username: String, password: String): Option[User] =
    users.find {
      case Admin(u, p) => u == username && p == password
      case RegularUser(u, p) => u == username && p == password
    }