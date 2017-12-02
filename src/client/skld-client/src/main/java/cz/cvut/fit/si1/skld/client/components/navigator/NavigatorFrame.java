package cz.cvut.fit.si1.skld.client.components.navigator;

import cz.cvut.fit.si1.skld.client.*;
import cz.cvut.fit.si1.skld.client.components.navigator.navigator_bar.NavigatorBarFragment;
import cz.cvut.fit.si1.skld.client.components.navigator.navigator_bar.NavigatorBarFragmentHandler;
import cz.cvut.fit.si1.skld.client.components.session_bar.SessionBarFragment;

public abstract class NavigatorFrame extends Frame {
    private NavigatorFrameHandler handler;

    private NavigatorBarFragment navigatorBarFragment;
    private Screen content;
    private SessionBarFragment sessionBarFragment;

    public NavigatorFrame(Passable source) {
        super(source);
    }

    @Override
    public Handler makeHandler() {
        this.handler = new NavigatorFrameHandler();
        handler.setOwner(this);
        return handler;
    }

    @Override
    public void changeContent(Screen screen) {
        content = screen;
        handler.setContent(screen);
    }

    @Override
    public void notify(UI source, NotifyType notifyType) {
        if (notifyType == NotifyType.LOGOUT) {
            getSource().pass(this, PassResult.LOGOUT);
        } else if (source == navigatorBarFragment & notifyType == NotifyType.USER_ACTION) {
            getSource().pass(this, PassResult.CANCELED);
        }
    }

    public void setNavigatorBarFragment(NavigatorBarFragment navigatorBarFragment) {
        this.navigatorBarFragment = navigatorBarFragment;
    }

    public void setSessionBarFragment(SessionBarFragment sessionBarFragment) {
        this.sessionBarFragment = sessionBarFragment;
    }
}