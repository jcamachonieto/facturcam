package es.efactura.client.dialog;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.efactura.client.model.ClientDataProvider;
import es.efactura.client.model.ClientDto;
import es.efactura.client.panels.ClientPanel;

@Component
public class ClientDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	ClientPanel parent;

	@Autowired
	private ClientDataProvider clientDataProvider;

	JTextField id, name, cif, address, location, province, postalCode, country, telephone, email;

	public void initialize(int width, int height) {
		setSize(width, height);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setLocationRelativeTo(null);

		id = new JTextField();
		id.setVisible(false);

		name = new JTextField();
		cif = new JTextField();
		address = new JTextField();
		location = new JTextField();
		province = new JTextField();
		postalCode = new JTextField();
		country = new JTextField();
		telephone = new JTextField();
		email = new JTextField();

		JPanel panel = new JPanel(new GridLayout(10, 10));

		panel.add(id);

		panel.add(new JLabel("Nombre fiscal:"));
		panel.add(name);

		panel.add(new JLabel("CIF/NIF:"));
		panel.add(cif);

		panel.add(new JLabel("Dirección:"));
		panel.add(address);
		
		panel.add(new JLabel("Localidad:"));
		panel.add(location);
		
		panel.add(new JLabel("Provincia:"));
		panel.add(province);
		
		panel.add(new JLabel("Código postal:"));
		panel.add(postalCode);
		
		panel.add(new JLabel("País:"));
		panel.add(country);
		
		panel.add(new JLabel("Teléfono:"));
		panel.add(telephone);
		
		panel.add(new JLabel("Email:"));
		panel.add(email);

		JButton saveButton = new JButton("Guardar");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientDto client = ClientDto.builder()
						.id(id.getText().equals("") ? 0 : Integer.parseInt(id.getText()))
						.name(name.getText())
						.cif(cif.getText())
						.address(address.getText())
						.location(location.getText())
						.province(province.getText())
						.postalCode(postalCode.getText())
						.country(country.getText())
						.telephone(telephone.getText())
						.email(email.getText())
						.build();
				clientDataProvider.upsert(client);
				setVisible(false);
				parent.refresh();
			}
		});
		panel.add(saveButton);

		add(panel);
	}

	public void fillData(ClientDto t) {
		setTitle("Editar cliente");
		id.setText("" + t.getId());
		name.setText(t.getName());
		cif.setText(t.getCif());
		address.setText(t.getAddress());
		location.setText(t.getLocation());
		province.setText(t.getProvince());
		postalCode.setText(t.getPostalCode());
		country.setText(t.getCountry());
		telephone.setText(t.getTelephone());
		email.setText(t.getEmail());
	}

	public void clearData() {
		setTitle("Añadir cliente");
		id.setText("");
		name.setText("");
		cif.setText("");
		address.setText("");
		location.setText("");
		province.setText("");
		postalCode.setText("");
		country.setText("");
		telephone.setText("");
		email.setText("");
	}

}
