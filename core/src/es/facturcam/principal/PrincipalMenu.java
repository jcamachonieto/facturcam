package es.facturcam.principal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import es.facturcam.panels.BillsPanel;
import es.facturcam.panels.ClientsPanel;
import es.facturcam.panels.ConfigurationPanel;

public class PrincipalMenu extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected JFrame content;

	/* Declaro los JMenu */
	private JMenuItem menuClients, menuBills, menuConfiguration;

	public PrincipalMenu(JFrame content) {
		this.menuClients = new JMenuItem("Clientes");
		this.menuBills = new JMenuItem("Facturas");
		this.menuConfiguration = new JMenuItem("Configuración");
		
		this.content = content;

		ActionListener menuClientsA = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PrincipalMenu.this.content.setContentPane(new ClientsPanel());		
				PrincipalMenu.this.content.revalidate();
			}
		};
		this.menuClients.addActionListener(menuClientsA);
		this.add(this.menuClients);

		ActionListener menuBillsA = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PrincipalMenu.this.content.setContentPane(new BillsPanel());		
				PrincipalMenu.this.content.revalidate();
			}
		};
		this.menuBills.addActionListener(menuBillsA);
		this.add(this.menuBills);

		this.add(this.menuConfiguration);
		ActionListener menuConfigurationA = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PrincipalMenu.this.content.setContentPane(new ConfigurationPanel());		
				PrincipalMenu.this.content.revalidate();
			}
		};
		this.menuConfiguration.addActionListener(menuConfigurationA);
	}

}
