package controller

import scalafx.scene.control.{TextField, ComboBox, Label, Alert}
import scalafx.scene.control.Alert.AlertType
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml
import model.FoodItem
import scalafx.Includes._

@sfxml
class AddFoodDialogController(
    private val nameField: TextField,
    private val caloriesField: TextField,
    private val proteinField: TextField,
    private val fatField: TextField,
    private val carbsField: TextField,
    private val categoryBox: ComboBox[String],
    private val errorLabel: Label
):

  var dialogStage: Stage = _
  var onAdd: FoodItem => Unit = _

  def handleAdd(): Unit =
    val name = nameField.text.value.trim
    val caloriesText = caloriesField.text.value.trim
    val proteinText = proteinField.text.value.trim
    val fatText = fatField.text.value.trim
    val carbsText = carbsField.text.value.trim
    val category = Option(categoryBox.value.value).getOrElse("")

    if name.isEmpty || caloriesText.isEmpty || proteinText.isEmpty || fatText.isEmpty || carbsText.isEmpty || category.isEmpty then
      errorLabel.text = "All fields must be filled."
    else if !List(caloriesText, proteinText, fatText, carbsText).forall(_.forall(ch => ch.isDigit || ch == '.')) then
      errorLabel.text = "Numeric fields must contain numbers."
    else
      val item = FoodItem(0, name, caloriesText.toInt, proteinText.toDouble, fatText.toDouble, carbsText.toDouble, category)
      if onAdd != null then onAdd(item)
      new Alert(AlertType.Information):
        title = "Success"
        headerText = "Food Added"
        contentText = s"$name has been added."
      .showAndWait()
      dialogStage.close()

  def handleCancel(): Unit =
    dialogStage.close()
