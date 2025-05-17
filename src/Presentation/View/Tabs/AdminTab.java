package Presentation.View.Tabs;

import Business.EventType;
import Presentation.Controllers.EventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

import java.util.ArrayList;
import java.util.List;

public class AdminTab extends JPanel {
    private EventListener listener;
    private JPanel addPanel;
    private JPanel deletePanel;
    private List<File> lastFilesUpload;
    private List<String> cryptoNames = new ArrayList<>();


    public AdminTab() {
        cryptoNames.add("one");
        cryptoNames.add("two");
        cryptoNames.add("three");
        cryptoNames.add("four");
        cryptoNames.add("five");
        configureTab();
        listener = null;
    }

    public void handleFilesDrop(List<File> files) {
        lastFilesUpload = files;
        notifyEvent();
    }

    // solo se tira un tipo de evento, no hace falta pasarle eventType, además, solo puede haber un subscriptor
    public void subscribe(EventListener newListener) {
        listener = newListener;
    }

    public void unsubscribe() {
        listener = null;
    }

    private void notifyEvent() {
        if (listener != null) {
            listener.update(EventType.FILES_DROPPED);
        }
    }

    public List<File> getFilesDropped() {
        return lastFilesUpload;
        // no serà null porque solo se llama cuando es notificado el listener
    }

    private void configureTab() {
        this.setLayout(new GridLayout(2,1));
        setBackground(new Color(70, 129, 137));
        configureAddPanel();
        add(addPanel, BorderLayout.NORTH);
        deletePanel = new JPanel();
        deletePanel.setBackground(new Color(70, 129, 137));
        add(deletePanel, BorderLayout.SOUTH);
    }

    private void configureAddPanel() {
        addPanel = new JPanel();
        addPanel.setBackground(new Color(120, 129, 0));
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));

    }
}
