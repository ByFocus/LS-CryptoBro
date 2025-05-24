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
 * A transfer handler that enables drag-and-drop functionality specifically for files.
 * It detects file drops onto components, and if the component belongs to an {@link AdminTab},
 * it forwards the list of dropped files to that tab for processing.
 */
public class FileDrooperHandler extends TransferHandler {

    /**
     * Checks whether the transfer operation includes files that can be imported.
     *
     * @param support the transfer support object containing data formats
     * @return true if files are being dragged, false otherwise
     */
    @Override
    public boolean canImport(TransferSupport support) {
        for (DataFlavor flavor : support.getDataFlavors()) {
            if (flavor.isFlavorJavaFileListType()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Imports the data if it consists of a list of files and passes them to the {@link AdminTab}.
     *
     * @param support the transfer support context
     * @return true if the data was successfully imported and handled, false otherwise
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean importData(TransferHandler.TransferSupport support) {
        if (!this.canImport(support)) {
            return false;
        }

        List<File> files;
        try {
            files = (List<File>) support.getTransferable()
                    .getTransferData(DataFlavor.javaFileListFlavor);
        } catch (UnsupportedFlavorException | IOException ex) {
            return false;
        }

        Component component = support.getComponent();
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
