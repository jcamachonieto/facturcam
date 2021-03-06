package es.efactura.principal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PrincipalFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected PrincipalMenu menu;
	
	protected JPanel content;

	/**
     * Create the frame.
     */
    public PrincipalFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, (int) dim.getWidth(), (int) dim.getHeight());
        // setBounds(100, 100, 800, 600);
        setLocationRelativeTo(null);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        
        setTitle("efactura");
        
        content = new JPanel();
        content.setBorder(new EmptyBorder(5, 5, 5, 5));
        content.setLayout(new BorderLayout(0, 0));
        content.setBackground(Color.green);
        setContentPane(content);
        
        menu = new PrincipalMenu(this);
        this.setJMenuBar(menu);
    }
}
