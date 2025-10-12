package net.bewis09.bewisclient.process;

import org.jetbrains.annotations.NotNull;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class CopyImage {
    public static void main(String[] args) {
        System.exit(run(args));
    }

    private static int run(String[] args) {
        try {
            if (args.length == 0) {
                return 1;
            } else {
                BufferedImage image = ImageIO.read(new File(args[0]));
                if (image == null) {
                    return 1;
                } else {
                    Transferable transferable = new Transferable() {
                        @Override
                        public DataFlavor[] getTransferDataFlavors() {
                            return new DataFlavor[] { DataFlavor.imageFlavor };
                        }

                        @Override
                        public boolean isDataFlavorSupported(DataFlavor flavor) {
                            return DataFlavor.imageFlavor.equals(flavor);
                        }

                        @Override
                        public @NotNull Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                            if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
                            return image;
                        }
                    };
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transferable, null);
                    return 0;
                }
            }
        } catch (Exception e) {
            return 1;
        }
    }
}