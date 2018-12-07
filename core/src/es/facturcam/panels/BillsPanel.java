package es.facturcam.panels;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class BillsPanel extends JPanel {

	public BillsPanel()  {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout(0, 0));
		setBackground(Color.red);
	}
	
}
