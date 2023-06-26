package TextEditor;

import javax.swing.JFrame;
import javax.swing.JMenu;

import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes;


/**
 * Class JMenu with additional features required to make 
 * Themes menu.
*/
public class MyThemesMenu extends JMenu {

    public static MyThemesMenuList[] themes;

    public MyThemesMenu(JFrame frame) {
        super("Themes");
        themes = new MyThemesMenuList[FlatAllIJThemes.INFOS.length];

        for(int i = 0; i < FlatAllIJThemes.INFOS.length; i++) {
            themes[i] = new MyThemesMenuList(FlatAllIJThemes.INFOS[i].getName());
            add(themes[i]);
        }

        MyThemesMenuList.SetCurrentFrame(frame);
    }
    
}