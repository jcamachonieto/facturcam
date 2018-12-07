package es.facturcam.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import es.facturcam.table.ClientsTable;

public class ClientsPanel extends JPanel {

	public ClientsPanel() {
		super(new GridLayout(1, 0));

		JTable table = new JTable(new ClientsTable());

		table.setPreferredScrollableViewportSize(new Dimension(500, 70));

		table.setFillsViewportHeight(true);

		// Create the scroll pane and add the table to it.

		JScrollPane scrollPane = new JScrollPane(table);

		// Add the scroll pane to this panel.

		add(scrollPane);
	}

}
