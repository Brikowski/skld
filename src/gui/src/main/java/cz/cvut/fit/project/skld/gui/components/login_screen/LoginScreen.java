package cz.cvut.fit.project.skld.gui.components.login_screen;

import cz.cvut.fit.project.skld.client.exceptions.APIException;
import cz.cvut.fit.project.skld.client.exceptions.UnauthorizedException;
import cz.cvut.fit.project.skld.client.http.SkldHttpClient;
import cz.cvut.fit.project.skld.gui.*;
import cz.cvut.fit.project.skld.gui.resources.Config;
import cz.cvut.fit.project.skld.gui.resources.Texts;
import cz.cvut.fit.project.skld.gui.util.FXUtil;
import cz.cvut.fit.project.skld.*;
import cz.cvut.fit.project.skld.gui.components.main_menu.MainMenuScreen;

import java.io.IOException;
import java.net.ConnectException;

/**
 * Obrazovka pro prihlaseni uzivatele.
 */
public class LoginScreen extends Screen {
    LoginScreenHandler handler;

    /**
     * Konstruktor.
     * @param source Rodicovsky objekt
     */
    public LoginScreen(Passable source) {
        super(source);
    }

    /**
     * Factory metoda pro tvoreni handleru.
     * Smi byt zavolana behem zivota fragmentu pouze jednou.
     * Diky tomu muze mit kazdy fragment (implementovany zvlastni tridou dedenou z Fragment) vlastni handler (implementovany zvlastni tridou dedenou z Handler).
     * @return Nove vytvoreny handler
     */
    @Override
    public Handler makeHandler() {
        this.handler = new LoginScreenHandler();
        handler.setOwner(this);
        return handler;
    }

    /**
     * Vrati rizeni obrazovce.
     * @param source Dcerinny objekt
     * @param result Vysledek provadeni operaci dcerinneho objektu
     */
    @Override
    public void pass(UI source, PassResult result) {
        getSource().changeContent(this);
    }

    /**
     * Zavolano po zadani hesla uzivatelem.
     * Pokusi se uzivatele prihlasit a pri neuspechu jej upozorni na nespravne heslo.
     * Po uspesnem prihlaseni uzivatele presune do hlavniho menu.
     * @param password Zadane heslo
     */
    public void onPasswordSubmit(String password) {
        SkldHttpClient httpClient = null;

        try {
             httpClient = SkldHttpClient.getClientForPIN(Config.getInstance().getServerUrl(), password);
        } catch (UnauthorizedException e) {
            handler.onInvalidPassword();
            return;
        } catch (IOException | APIException e) {
            FXUtil.displayAlert(Texts.Alerts.CONNECTION_ERROR_ALERT_TITLE, Texts.Alerts.CONNECTION_ERROR_ALERT_TEXT);
            return;
        }

        getApp().setClient(httpClient);

        MainMenuScreen screen = new MainMenuScreen(this);
        screen.follow();
    }
}
