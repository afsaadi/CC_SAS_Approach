package application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.cell.CheckBoxListCell;

public class MultipleSelectionComboBox extends ComboBox<String> {

    public MultipleSelectionComboBox() {
        setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty selected = new SimpleBooleanProperty(false);
            CheckBox checkBox = new CheckBox(item);
            checkBox.selectedProperty().bindBidirectional(selected);
            return selected;
        }));
        setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(getSelectedItems());
                }
            }
        });
    }

    private String getSelectedItems() {
        String selectedItem = getSelectionModel().getSelectedItem();
        return selectedItem != null ? selectedItem : "";
    }
}
