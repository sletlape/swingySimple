package controller.Interfacing;

import enums.EHeroClass;
import view.gui.MenuPanel;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class MenuController {
    private final MenuPanel menuPanel;
    private final GUIController guiController;

    public MenuController(GUIController guiController, MenuPanel menuPanel) {
        this.menuPanel = menuPanel;
        this.guiController = guiController;
        addAllListeners();
    }

    ///TODO: fix the flow here...
    private AbstractAction newHeroListener = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            guiController.showNewAvatarPanel();
        }
    };

    private EHeroClass gettingAvatarChoice() {
        EHeroClass avatarType = EHeroClass.Lincoln;
        return avatarType;
    }

    private AbstractAction fetchDatabaseListener = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            guiController.showOldAvatarPanel();

        }
    };

    private void addAllListeners() {
        menuPanel.addBtnNewHeroListener(newHeroListener);
        menuPanel.addBtnDatabaseListener(fetchDatabaseListener);
    }
}
