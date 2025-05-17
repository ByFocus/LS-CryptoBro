package Presentation.Controllers;

import Business.EventType;
import Presentation.View.Tabs.AdminTab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminTabController implements ActionListener, EventListener {
    private AdminTab adminTab;

    public AdminTabController() {
        adminTab = new AdminTab();
        adminTab.subscribe(this);
    }

    @Override
    public void update(EventType context) {
        switch (context) {
            case FILES_DROPPED:

                break;

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private void handleFiles() {

    }
}
