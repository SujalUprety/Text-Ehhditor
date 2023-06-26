package TextEditor;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


/**
 * Class JTextPane which only stores the line numbers
*/
public class MyTextPaneLineNumbers extends JTextPane{

    private StyledDocument styledDoc;
    
    private SimpleAttributeSet selectedLineAttributeSet;
    private SimpleAttributeSet notSelectedLineAttributeSet;
    
    private Document doc;
    
    private static MyTextPaneLineNumbers instance;
    
    public MyTextPaneLineNumbers() {

        instance = this;

        styledDoc = getStyledDocument();
        selectedLineAttributeSet = new SimpleAttributeSet();
        notSelectedLineAttributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(selectedLineAttributeSet, Color.white);
        StyleConstants.setLineSpacing(selectedLineAttributeSet, 0.1249f);
        
        
        StyleConstants.setForeground(notSelectedLineAttributeSet, Color.gray);
        StyleConstants.setLineSpacing(notSelectedLineAttributeSet, 0.1249f);

    
        doc = getDocument();
    }

    /**
     * To highlight the current line where the caret is at
     * 
    */
    public void ChangeCurrentLineColor(int lineNumber) {
        try {
            String text = doc.getText(0, doc.getLength());
            int index = text.indexOf(Integer.toString(lineNumber));

            if(index >= 0)  setCaretPosition(index);
            
            styledDoc.setCharacterAttributes(0, index, notSelectedLineAttributeSet,true);
            
            styledDoc.setCharacterAttributes(index + Integer.toString(lineNumber).length(), text.length(), notSelectedLineAttributeSet, true);

            styledDoc.setCharacterAttributes(index, Integer.toString(lineNumber).length(), selectedLineAttributeSet, true);
            
        }   
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * As the margin of the JTextPane changes on
     * every realod on UI, so to set the margin constant
     * on every UI reaload<p>
     * Call this method after reloading UI so to make the
     * margin same as before
    */
    public static void SetMarginOnUIChange() {
        instance.setMargin(new InsetsUIResource(1, 0, 2, 2));
    }
    
}
