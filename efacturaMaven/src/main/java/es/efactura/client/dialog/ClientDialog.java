package es.efactura.client.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

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
	
	private final JPanel contentPanel = new JPanel();

	JTextField id, name, cif, address, location, province, postalCode, country, telephone, email;

	public void initialize() {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setLocationRelativeTo(null);

		id = new JTextField();
		id.setVisible(false);

		setBounds(100, 100, 600, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("147px"),
				ColumnSpec.decode("56px:grow"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("226px"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("22px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		{
			JLabel lblNewLabel = new JLabel("Nombre");
			contentPanel.add(lblNewLabel, "1, 2, left, center");
		}
		{
			name = new JTextField();
			contentPanel.add(name, "2, 2, 3, 1, left, top");
			name.setColumns(20);
		}
		
		{
			JLabel lblNewLabel_1 = new JLabel("Nif/Cif");
			contentPanel.add(lblNewLabel_1, "1, 4, left, center");
		}
		{
			cif = new JTextField();
			contentPanel.add(cif, "2, 4, 3, 1, left, top");
			cif.setColumns(20);
		}
		
		{
			JLabel lblNewLabel_1 = new JLabel("Direccion");
			contentPanel.add(lblNewLabel_1, "1, 6, left, center");
		}
		{
			address = new JTextField();
			contentPanel.add(address, "2, 6, 2, 1, left, top");
			address.setColumns(20);
		}
		{
			JLabel lblNewLabel_2 = new JLabel("Localidad");
			contentPanel.add(lblNewLabel_2, "1, 8, left, center");
		}
		{
			location = new JTextField();
			contentPanel.add(location, "2, 8, 3, 1, left, top");
			location.setColumns(20);
		}
		{
			JLabel lblNewLabel_3 = new JLabel("Provincia");
			contentPanel.add(lblNewLabel_3, "1, 10, left, center");
		}
		{
			province = new JTextField();
			contentPanel.add(province, "2, 10, 3, 1, left, top");
			province.setColumns(20);
		}
		{
			JLabel lblNewLabel_4 = new JLabel("Código postal");
			contentPanel.add(lblNewLabel_4, "1, 12, left, center");
		}
		{
			postalCode = new JTextField();
			contentPanel.add(postalCode, "2, 12, 3, 1, left, top");
			postalCode.setColumns(20);
		}
		{
			JLabel lblNewLabel_5 = new JLabel("Pais");
			contentPanel.add(lblNewLabel_5, "1, 14, left, center");
		}
		{
			country = new JTextField();
			contentPanel.add(country, "2, 14, 3, 1, left, top");
			country.setColumns(20);
		}
		{
			JLabel lblNewLabel_6 = new JLabel("Teléfono");
			contentPanel.add(lblNewLabel_6, "1, 16, left, center");
		}
		{
			telephone = new JTextField();
			contentPanel.add(telephone, "2, 16, 3, 1, left, top");
			telephone.setColumns(20);
		}
		{
			JLabel lblNewLabel_7 = new JLabel("Email");
			contentPanel.add(lblNewLabel_7, "1, 18, left, center");
		}
		{
			email = new JTextField();
			contentPanel.add(email, "2, 18, 3, 1, left, top");
			email.setColumns(20);
		}
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
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
						parent.refresh(parent.getFindText().getText());
					}
				});
				buttonPane.add(saveButton);
				getRootPane().setDefaultButton(saveButton);
			}
		}
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
