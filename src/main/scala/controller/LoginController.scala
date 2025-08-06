package controller

import scalafx.scene.control.{TextField, PasswordField, Label, Alert}
import scalafx.scene.control.Alert.AlertType
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml
import repository.UserRepository
import view.{AdminDashboard, UserDashboard}
import java.util.ResourceBundle

@sfxml
class LoginController(
    private val usernameField: TextField,
    private val passwordField: PasswordField,
    private val statusLabel: Label,
    private val resources: ResourceBundle
):

  def handleLogin(): Unit =
    val username = usernameField.text.value.trim
    val password = passwordField.text.value.trim

    if username.isEmpty || password.isEmpty then
      new Alert(AlertType.Warning):
        title = resources.getString("login.title")
        headerText = null
        contentText = resources.getString("login.empty")
      .showAndWait()
    else
      UserRepository.findUser(username, password) match
        case Some(user) =>
          val stage = usernameField.scene().window().asInstanceOf[Stage]
          stage.close()
          user.role match
            case "admin" => AdminDashboard.show()
            case _ => new UserDashboard().show()
        case None =>
          new Alert(AlertType.Error):
            title = resources.getString("login.title")
            headerText = null
            contentText = resources.getString("login.invalid")
          .showAndWait()
