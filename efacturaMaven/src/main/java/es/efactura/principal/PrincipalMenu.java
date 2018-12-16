package es.efactura.principal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.efactura.client.panels.ClientPanel;

@Component
public class PrincipalMenu extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	ClientPanel clientPanel;

	protected JFrame content;

	/* Declaro los JMenu */
	private JMenuItem menuClients, menuBills, menuConfiguration;

	public void initialize(JFrame content) {
		this.menuClients = new JMenuItem("Clientes");
		this.menuBills = new JMenuItem("Facturas");
		this.menuConfiguration = new JMenuItem("Configuraci�n");

		this.content = content;

		clientPanel.setVisible(false);

		ActionListener menuClientsA = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clientPanel.setVisible(true);
				clientPanel.initialize();
				PrincipalMenu.this.content.setContentPane(clientPanel);
				PrincipalMenu.this.content.revalidate();
			}
		};
		this.menuClients.addActionListener(menuClientsA);
		this.add(this.menuClients);

		ActionListener menuBillsA = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// PrincipalMenu.this.content.setContentPane(new BillsPanel());
				PrincipalMenu.this.content.revalidate();
			}
		};
		this.menuBills.addActionListener(menuBillsA);
		this.add(this.menuBills);

		this.add(this.menuConfiguration);
		ActionListener menuConfigurationA = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// PrincipalMenu.this.content.setContentPane(new ConfigurationPanel());
				PrincipalMenu.this.content.revalidate();
			}
		};
		this.menuConfiguration.addActionListener(menuConfigurationA);
	}

}
