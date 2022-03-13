import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

public class OpenFile extends JFrame {
    private JPanel MainPanel;
    private JLabel FMRE_LBL;
    private JButton OPN_FILE_BTN;
    private JProgressBar PDF_PB;

    public OpenFile() {
        OPN_FILE_BTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    JFileChooser fc = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos PDF", "pdf", "pdf");
                    fc.setFileFilter(filter);
                    fc.setCurrentDirectory(new File(System.getProperty("user.home")));
                    int result = fc.showOpenDialog(getParent());
                    if (result == JFileChooser.APPROVE_OPTION) {
                        // user selects a file
                        File selectedFile = fc.getSelectedFile();
                        System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                        File file = new File(selectedFile.toString());
                        PDDocument document = PDDocument.load(file);

                        AccessPermission ap = new AccessPermission();
                        ap.setCanExtractContent(false);
                        ap.setCanModify(false);
                        ap.setCanExtractForAccessibility(false);
                        ap.canExtractContent();
                        ap.canModify();
                        ap.canExtractForAccessibility();

                        StandardProtectionPolicy spp = new StandardProtectionPolicy("<you_secure_password_here>",null, ap);
                        spp.setEncryptionKeyLength(128);
                        spp.setPermissions(ap);
                        document.protect(spp);
                        System.out.println("PDF Document encrypted Successfully.");
                        document.save(selectedFile.getPath());
                        document.close();
                        JOptionPane.showMessageDialog(OPN_FILE_BTN, "Archivo encriptado.");
                    }
                }catch (Exception err){
                    System.out.println(err.getMessage());
                    JOptionPane.showMessageDialog(OPN_FILE_BTN, "Ocurrió un error: \n" + err.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        OpenFile of = new OpenFile();
        of.setContentPane(of.MainPanel);
        of.setTitle("FMRE PDF ENCRYPTOR");
        of.setSize(400,200);
        of.setVisible(true);
        of.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
