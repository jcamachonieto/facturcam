package es.facturcam.principal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class PrincipalMenu extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* Declaro los JMenu */
	private JMenuItem menuClients, menuBills, menuConfiguration;

	public PrincipalMenu() {
		this.menuClients = new JMenuItem("Clientes");
		this.menuBills = new JMenuItem("Facturas");
		this.menuConfiguration = new JMenuItem("Configuración");

		ActionListener menuClientsA = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"Presionaste el JMenuItem Acerca de");
			}
		};
		this.menuClients.addActionListener(menuClientsA);
		this.add(this.menuClients);

		this.add(this.menuBills);
		ActionListener menuBillsA = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		};
		this.menuBills.addActionListener(menuBillsA);

		this.add(this.menuConfiguration);
		ActionListener menuConfigurationA = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		};
		this.menuConfiguration.addActionListener(menuConfigurationA);
	}

}
