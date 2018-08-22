package controller.Interfacing;

import enums.EDirection;
import view.gui.WorldPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class WorldPanelController {
    WorldPanel worldPanel;
    GUIController guiController;

    public WorldPanelController(GUIController guiController, WorldPanel worldPanel) {
        this.worldPanel = worldPanel;
        this.guiController = guiController;

        guiController.updateUserInterface();
        addAllListeners();
    }

    private void addAllListeners() {
        worldPanel.addOnUpListener(onUpListener);
        worldPanel.addOnDownListener(onDownListener);
        worldPanel.addOnLeftListener(onLeftListener);
        worldPanel.addOnRightListener(onRightListener);

    }

    AbstractAction onDownListener = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            guiController.move(EDirection.DOWN);
            System.out.println("D\n");

        }
    };

    AbstractAction onUpListener = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            guiController.move(EDirection.UP);
            System.out.println("Up\n");
        }
    };

    AbstractAction onLeftListener = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            guiController.move(EDirection.LEFT);
        }
    };

    AbstractAction onRightListener = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            guiController.move(EDirection.RIGHT);
        }
    };

}
