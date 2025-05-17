package Presentation.View.Tabs;

import Business.EventType;
import Presentation.Controllers.EventListener;
import Presentation.View.Handlers.FileDrooperHandler;
import Presentation.View.Panels.ImageBackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

import java.util.ArrayList;
import java.util.List;

public class AdminTab extends JPanel {
    private final String NUMB_FILES_TEXT_SING = " fichebro seleccionado";
    private final String NUMB_FILES_TEXT_PLR = " fichebros seleccionados";
    private final String DROP_FILES_TEXT = "Arrastra y suelta tus fichebros aquí";
    private final String ADD_CRYPTO_BUTTON_TEXT = "AÑADIR CRYPTO";

    public static final String ADD_CRYPTO_COMMAND = "ADD CRYPTO";


    private EventListener listener;

    private JPanel addPanel;
    private JLabel howManyFilesLabel;
    private JTextArea filesNamesArea;
    private JButton addButton;

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
        //this.setTransferHandler(new FileDrooperHandler());
    }

    public void handleFilesDrop(List<File> files) {
        lastFilesUpload = files;
        howManyFilesLabel.setText(files.size() + (files.size() == 1? NUMB_FILES_TEXT_SING: NUMB_FILES_TEXT_PLR));
        filesNamesArea.setText("");

        for (File file : files) {
            filesNamesArea.append(file.getName() + '\n');
        }
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
        this.setLayout(new GridBagLayout());
        setBackground(new Color(70, 129, 137));

        configureAddPanel();

        GridBagConstraints proportions = new GridBagConstraints();
        proportions.fill = GridBagConstraints.BOTH;
        proportions.gridx = 0;
        proportions.gridy = 0;
        proportions.weighty = 0.60;
        proportions.weightx = 1.0;

        add(addPanel, proportions);

        deletePanel = new JPanel();
        deletePanel.setBackground(new Color(70, 129, 137));

        proportions.gridy = 1;
        proportions.weighty = 0.40;
        add(deletePanel, proportions);
    }

    private void configureAddPanel() {
        addPanel = new JPanel();
        addPanel.setBackground(new Color(120, 129, 0));
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));

        JLabel dropCryptosLabel = new JLabel(DROP_FILES_TEXT);
        dropCryptosLabel.setFont(new Font ("Arial", Font.BOLD, 16));
        dropCryptosLabel.setForeground(Color.BLACK);
        dropCryptosLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel dropPanel = new JPanel();
        dropPanel.setBackground(new Color(255, 0, 0));
        dropPanel.setMinimumSize(new Dimension(100, 100));
        dropPanel.setPreferredSize(new Dimension(404, 200));
        dropPanel.setMaximumSize(new Dimension(404, 200));
        dropPanel.setBorder(BorderFactory.createLineBorder(new Color(10,10,70), 2));
        dropPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dropPanel.setLayout(new BorderLayout());

        filesNamesArea = new JTextArea();
        filesNamesArea.setEditable(false);
        filesNamesArea.setFocusable(false);
        filesNamesArea.setOpaque(false); // Makes background transparent
        filesNamesArea.setForeground(Color.BLACK); // Or any color that contrasts with your image
        filesNamesArea.setFont(new Font("Arial", Font.BOLD, 16));
        filesNamesArea.setLineWrap(true);
        filesNamesArea.setWrapStyleWord(true);
        filesNamesArea.setTransferHandler(new FileDrooperHandler() {});

        Image normalImage = new ImageIcon("imgs/dragndrop2.png").getImage();
        Image hoverImage = new ImageIcon("imgs/dragndrop.png").getImage();

        ImageBackgroundPanel backgroundPanel = new ImageBackgroundPanel(normalImage);
        filesNamesArea.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backgroundPanel.setImage(hoverImage);
                backgroundPanel.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backgroundPanel.setImage(normalImage);
                backgroundPanel.repaint();
            }
        });
        backgroundPanel.add(filesNamesArea, BorderLayout.CENTER);
        dropPanel.add(backgroundPanel, BorderLayout.CENTER);


        howManyFilesLabel = new JLabel("0" + NUMB_FILES_TEXT_PLR);
        howManyFilesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        howManyFilesLabel.setForeground(Color.BLACK);
        howManyFilesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        addButton = new JButton(ADD_CRYPTO_BUTTON_TEXT);
        addButton.setBackground(new Color(0, 51, 204)); // Dark blue background
        addButton.setForeground(Color.WHITE);
        addButton.setActionCommand(ADD_CRYPTO_COMMAND);

        JPanel addButtonPanel = new JPanel();
        addButtonPanel.add(addButton);
        addButtonPanel.setPreferredSize(new Dimension(120, 80));
        addButtonPanel.setOpaque(false);
        addButtonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,70,10));

        addPanel.add(getVerticalGlue(0, 7));
        addPanel.add(dropCryptosLabel);
        addPanel.add(getVerticalGlue(0, 2));
        addPanel.add(dropPanel);
        addPanel.add(getVerticalGlue(0, 2));
        addPanel.add(howManyFilesLabel);
        addPanel.add(getVerticalGlue(0, 2));
        addPanel.add(addButtonPanel);
        addPanel.add(Box.createVerticalGlue());


    }

    private Component getVerticalGlue(int width, int height){
        Component glue = Box.createVerticalGlue();
        glue.setMinimumSize(new Dimension(width, height));
        return glue;
    }

    public void registerController(ActionListener newListener) {
        addButton.addActionListener(newListener);

    }
    public void resetTab() {
        filesNamesArea.setText("");
    }
}
