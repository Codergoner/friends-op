package view

import scalafx.scene.control.{TextField, ComboBox, DatePicker, Button, Alert}
import scalafx.stage.Stage
import scalafx.Includes._
import javafx.fxml.{FXML}
import model.FoodItem
import java.time.LocalDate
import java.util.ResourceBundle

class AddFoodDialogController:
  @FXML private var nameField: TextField = _
  @FXML private var caloriesField: TextField = _
  @FXML private var proteinField: TextField = _
  @FXML private var fatField: TextField = _
  @FXML private var carbsField: TextField = _
  @FXML private var categoryBox: ComboBox[String] = _
  @FXML private var datePicker: DatePicker = _
  @FXML private var addButton: Button = _
  @FXML private var resources: ResourceBundle = _

  var dialogStage: Stage = _
  var onSave: FoodItem => Unit = _

  @FXML def initialize(): Unit =
    categoryBox.items = javafx.collections.FXCollections.observableArrayList(
      "Fruits", "Vegetables", "Meat", "Dairy", "Grains"
    )
    datePicker.setValue(LocalDate.now())
    addButton.disable <== nameField.text.isEmpty

  @FXML def handleSave(): Unit =
    val name = nameField.text.value.trim
    val caloriesText = caloriesField.text.value.trim
    val proteinText = proteinField.text.value.trim
    val fatText = fatField.text.value.trim
    val carbsText = carbsField.text.value.trim
    val category = categoryBox.value.value
    val date = datePicker.value.value

    if name.isEmpty || caloriesText.isEmpty || proteinText.isEmpty ||
       fatText.isEmpty || carbsText.isEmpty || category == null then
      new Alert(Alert.AlertType.Error) {
        title = resources.getString("addFood.title")
        headerText = null
        contentText = resources.getString("error.allFields")
      }.showAndWait()
    else if !caloriesText.forall(_.isDigit) || !proteinText.forall(_.isDigit) ||
            !fatText.forall(_.isDigit) || !carbsText.forall(_.isDigit) then
      new Alert(Alert.AlertType.Error) {
        title = resources.getString("addFood.title")
        headerText = null
        contentText = resources.getString("error.numbers")
      }.showAndWait()
    else
      val item = FoodItem(0, name, caloriesText.toInt, proteinText.toDouble,
        fatText.toDouble, carbsText.toDouble, category, date)
      onSave(item)
      dialogStage.close()

  @FXML def handleCancel(): Unit =
    dialogStage.close()

