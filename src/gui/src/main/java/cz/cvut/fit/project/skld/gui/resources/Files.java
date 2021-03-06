package cz.cvut.fit.project.skld.gui.resources;

import cz.cvut.fit.project.skld.gui.FXMLFragmentType;

/**
 * Obsahuje nazvy (pripadne cesty) k souborum, typicky resource souborum
 */
public class Files {
    /**
     * Vraci FXML soubor definujici GUI pro prislusny fragment
     * @param fragment Typ fragmentu
     * @return FXML soubor pro prislusny typu fragmentu
     */
    public static String getFXMLFilePath(FXMLFragmentType fragment) {
        String fileName;

        switch (fragment) {
            case LOGIN_SCREEN:
                fileName = "/fxml/LoginScreen.fxml";
                break;
            case MAIN_MENU_SCREEN:
                fileName = "/fxml/MainMenuScreen.fxml";
                break;
            case SESSION_BAR_FRAGMENT:
                fileName = "/fxml/SessionBarFragment.fxml";
                break;
            case NAVIGATOR_FRAME:
                fileName = "/fxml/NavigatorFrame.fxml";
                break;
            case NAVIGATOR_BAR_FRAGMENT:
                fileName = "/fxml/NavigatorBarFragment.fxml";
                break;
            case ADD_PRODUCT_TYPE_SCREEN:
                fileName = "/fxml/AddProductTypeScreen.fxml";
                break;
            case EDIT_PRODUCT_TYPE_FRAGMENT:
                fileName = "/fxml/EditProductTypeFragment.fxml";
                break;
            case CHANGE_PRODUCT_TYPE_SCREEN:
                fileName = "/fxml/ChangeProductTypeScreen.fxml";
                break;
            case FIND_PRODUCT_TYPE_FRAGMENT:
                fileName = "/fxml/FindProductTypeFragment.fxml";
                break;
            case NEW_ORDER_IN_SCREEN:
                fileName = "/fxml/NewOrderInScreen.fxml";
                break;
            case EDIT_ORDER_IN_FRAGMENT:
                fileName = "/fxml/EditOrderInFragment.fxml";
                break;
            case ORDER_IN_PRODUCT_FRAGMENT:
                fileName = "/fxml/OrderInProductFragment.fxml";
                break;
            case ADD_PRODUCT_TO_ORDER_SCREEN:
                fileName = "/fxml/AddProductToOrderScreen.fxml";
                break;
            case CHANGE_ORDER_IN_SCREEN:
                fileName = "/fxml/ChangeOrderInScreen.fxml";
                break;
            case FIND_ORDER_IN_FRAGMENT:
                fileName = "/fxml/FindOrderInFragment.fxml";
                break;
            default:
                throw new RuntimeException("FXML file path not added");
        }

        return fileName;
    }

}
