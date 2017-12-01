package cz.cvut.fit.si1.skld.client.components.main_menu;

import cz.cvut.fit.si1.skld.client.Handler;
import cz.cvut.fit.si1.skld.client.components.session_bar.SessionBarFragment;
import cz.cvut.fit.si1.skld.client.resources.FXMLFragment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class MainMenuScreenHandler extends Handler {
    private MainMenuScreen owner;

    @FXML
    private HBox sessionBar;

    public MainMenuScreenHandler() {
        super(FXMLFragment.MAIN_MENU_SCREEN);
    }

    public void setOwner(MainMenuScreen owner) {
        this.owner = owner;
    }

    @FXML
    protected void initialize() {
        SessionBarFragment sessionBarFragment = new SessionBarFragment(owner);
        owner.setSessionBarFragment(sessionBarFragment);
        sessionBar.getChildren().setAll((HBox)sessionBarFragment.getRoot());
    }

    @FXML
    private void handleAddProductTypeButtonAction(ActionEvent event) {
        owner.addProductType();
    }

}
