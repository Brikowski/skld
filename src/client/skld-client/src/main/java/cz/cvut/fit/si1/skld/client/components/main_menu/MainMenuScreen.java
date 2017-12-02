package cz.cvut.fit.si1.skld.client.components.main_menu;

import cz.cvut.fit.si1.skld.client.*;
import cz.cvut.fit.si1.skld.client.components.add_product_type_screen.AddProductTypeFrame;
import cz.cvut.fit.si1.skld.client.components.session_bar.SessionBarFragment;

public class MainMenuScreen extends Screen {
    private SessionBarFragment sessionBarFragment;

    public MainMenuScreen(Passable source) {
        super(source);
    }

    @Override
    public Handler makeHandler() {
        MainMenuScreenHandler handler = new MainMenuScreenHandler();
        handler.setOwner(this);
        return handler;
    }

    @Override
    public void notify(UI source, NotifyType notifyType) {
        switch (notifyType) {
            case LOGOUT:
                getSource().pass(this, PassResult.LOGOUT);
                break;
        }
    }

    @Override
    public void pass(UI source, PassResult result) {
        getSource().changeContent(this);
        switch (result) {
            case LOGOUT:
                getSource().pass(this, PassResult.LOGOUT);
                break;
        }
    }

    public void setSessionBarFragment(SessionBarFragment sessionBarFragment) {
        this.sessionBarFragment = sessionBarFragment;
    }

    public void addProductType() {
        AddProductTypeFrame frame = new AddProductTypeFrame(this);
        frame.follow();
    }
}