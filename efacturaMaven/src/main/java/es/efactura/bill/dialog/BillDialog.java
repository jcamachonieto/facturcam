package es.efactura.bill.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import es.efactura.bill.model.BillDataProvider;
import es.efactura.bill.model.BillDto;
import es.efactura.bill.panels.BillPanel;
import es.efactura.client.model.ClientDataProvider;
import es.efactura.client.model.ClientDto;

@Component
public class BillDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	BillPanel parent;

	@Autowired
	private BillDataProvider billDataProvider;
	
	@Autowired
	private ClientDataProvider clientDataProvider; 

	private final JPanel contentPanel = new JPanel();

	JTextField id, number;
	JComboBox<ClientDto> clientCombo;
	JDatePicker broadcastDate, expirationDate;
	
	UtilDateModel broadcastmodel = new UtilDateModel();
	UtilDateModel expirationmodel = new UtilDateModel();

	public void initialize() {
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setLocationRelativeTo(null);

		id = new JTextField();
		id.setVisible(false);

		setBounds(100, 100, 600, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(
				new ColumnSpec[] { ColumnSpec.decode("147px"), ColumnSpec.decode("56px:grow"),
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("226px"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.UNRELATED_GAP_ROWSPEC, RowSpec.decode("22px"),
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));
		{
			JLabel lblNewLabel = new JLabel("Número");
			contentPanel.add(lblNewLabel, "1, 2, left, center");
		}
		{
			number = new JTextField();
			contentPanel.add(number, "2, 2, 3, 1, left, top");
			number.setColumns(20);
		}

		{
			JLabel lblNewLabel_1 = new JLabel("Cliente");
			contentPanel.add(lblNewLabel_1, "1, 4, left, center");
		}
		{
			List<ClientDto> data = clientDataProvider.getList("");
			ClientDto[] clients = data.toArray(new ClientDto[data.size()]);
			clientCombo = new JComboBox<ClientDto>(clients);
			contentPanel.add(clientCombo, "2, 4, 3, 1, left, top");
		}

		{
			JLabel lblNewLabel_1 = new JLabel("Fecha emisión");
			contentPanel.add(lblNewLabel_1, "1, 6, left, center");
		}
		{
			JDatePanelImpl datePanel = new JDatePanelImpl(broadcastmodel, p);
			broadcastDate = new JDatePickerImpl(datePanel, null);
			contentPanel.add((java.awt.Component) broadcastDate, "2, 6, 2, 1, left, top");
		}
		{
			JLabel lblNewLabel_2 = new JLabel("Fecha vencimiento");
			contentPanel.add(lblNewLabel_2, "1, 8, left, center");
		}
		{
			JDatePanelImpl datePanel = new JDatePanelImpl(expirationmodel, p);
			expirationDate = new JDatePickerImpl(datePanel, null);
			contentPanel.add((java.awt.Component) expirationDate, "2, 6, 2, 1, left, top");
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
						BillDto bill = BillDto.builder()
								.id(id.getText().equals("") ? 0 : Integer.parseInt(id.getText()))
								.number(Integer.parseInt(number.getText()))
								.client((ClientDto) clientCombo.getSelectedItem())
								.broadcastDate((Date) broadcastDate.getModel().getValue())
								.expirationDate((Date) expirationDate.getModel().getValue())
								.build();
						billDataProvider.upsert(bill);
						setVisible(false);
						parent.refresh(parent.getFindText().getText());
					}
				});
				buttonPane.add(saveButton);
				getRootPane().setDefaultButton(saveButton);
			}
		}
	}

	public void fillData(BillDto t) {
		setTitle("Editar factura");
		id.setText("" + t.getId());
		number.setText(t.getNumber() + "");
		clientCombo.setSelectedItem(t.getClient());
		
		broadcastmodel.setDate(1990, 8, 24);
		broadcastmodel.setSelected(true);
		
		expirationmodel.setDate(1990, 8, 24);
		expirationmodel.setSelected(true);
	}

	public void clearData() {
		setTitle("Añadir factura");
		id.setText("");
		number.setText("");
		clientCombo.setSelectedItem(null);
		broadcastmodel.setSelected(false);
		expirationmodel.setSelected(false);
	}

}
