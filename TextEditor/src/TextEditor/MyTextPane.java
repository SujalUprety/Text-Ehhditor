package TextEditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JTextPane;
import javax.swing.text.Element;

/**
 * Class JTextPane with additional required feature
*/
public class MyTextPane extends JTextPane {
    public MyTextPane() {
        setOpaque(false);
    }

    /**
     * Returns the line number in which the caret is currently at
    */
    public int GetLineAtCaret() {
        int caretPosition = getCaretPosition();
        Element root = getDocument().getDefaultRootElement();
        
        return root.getElementIndex(caretPosition) + 1;
    }
    
    @Override
    protected void paintComponent(Graphics graphics) {
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        try {
            Rectangle rect = (Rectangle) modelToView2D(getCaretPosition());
            if (rect != null) {
                graphics.setColor(Color.darkGray);
                graphics.fillRect(0, rect.y, getWidth(), rect.height);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.paintComponent(graphics);
    }
    
    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
        super.repaint(tm, 0, 0, getWidth(), getHeight());
    }
}