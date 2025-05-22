package Presentation.View.Handlers;

import Presentation.View.Tabs.AdminTab;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The type File drooper handler.
 */
public class FileDrooperHandler extends TransferHandler {

    //ara només acceptem fitxers, no imatges o altres
    @Override
    public boolean canImport(TransferSupport support) {
        for (DataFlavor flavor : support.getDataFlavors()) {
            if (flavor.isFlavorJavaFileListType()){
                return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean importData(TransferHandler.TransferSupport support) {
        if (!this.canImport(support))
            return false;

        List<File> files;
        try {
            files = (List<File>) support.getTransferable()
                    .getTransferData(DataFlavor.javaFileListFlavor);
        } catch (UnsupportedFlavorException | IOException ex) {
            // should never happen (or JDK is buggy)
            return false;
        }

        // obtenim el component pare
        Component component = (JComponent) support.getComponent();
        while (component != null && !(component instanceof AdminTab)) {
            component = component.getParent();
        }
        if (component != null) {
            AdminTab tab = (AdminTab) component;
            tab.handleFilesDrop(files);
        }
        return true;
    }
}
