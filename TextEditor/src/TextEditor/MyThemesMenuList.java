package TextEditor;

import java.awt.event.*;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes;
import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes.FlatIJLookAndFeelInfo;


/**
 * Class JCheckBoxMenuItem with additional features
 * required to make the list of the themes which will 
 * come under Themes menu.
*/
public class MyThemesMenuList extends JCheckBoxMenuItem implements ActionListener {
    
    private String menuItemName;

    private static JFrame currentFrame;

    public MyThemesMenuList(String menuItemName) {
        super(menuItemName);
        this.menuItemName = menuItemName;

        
        addActionListener(this);
    }

    /**
     * Just to set which is the current frame that this 
     * menu list is on
    */
    public static void SetCurrentFrame(JFrame frame) {
        currentFrame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        for(FlatIJLookAndFeelInfo setLookAndFeel: FlatAllIJThemes.INFOS) {
            if(setLookAndFeel.getName().equals(menuItemName)) {
                try{
                    UIManager.setLookAndFeel(setLookAndFeel.getClassName());
                    SwingUtilities.updateComponentTreeUI(currentFrame);
                    MyTextPaneLineNumbers.SetMarginOnUIChange(); 
                }   catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

        for(MyThemesMenuList checkMenu : MyThemesMenu.themes) {
            if(checkMenu.menuItemName.equals(menuItemName)) continue;
            checkMenu.setSelected(false);
        }
    }

}