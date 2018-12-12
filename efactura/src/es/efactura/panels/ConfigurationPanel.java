package es.efactura.panels;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ConfigurationPanel extends JPanel {

	public ConfigurationPanel()  {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout(0, 0));
		setBackground(Color.orange);
	}
	
}
