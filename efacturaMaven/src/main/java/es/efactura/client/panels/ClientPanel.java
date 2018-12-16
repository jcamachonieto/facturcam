package es.efactura.client.panels;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import es.efactura.client.dialog.ClientDialog;
import es.efactura.client.model.ClientDataProvider;
import es.efactura.client.model.ClientDto;
import es.efactura.client.table.ClientRowAction;
import es.efactura.client.table.ClientTable;
import es.efactura.utils.table.JTableButtonMouseListener;
import es.efactura.utils.table.ObjectTableModel;
import es.efactura.utils.table.PaginatedTableDecorator;
import es.efactura.utils.table.PaginationDataProvider;
import lombok.Getter;

@Component
@Getter
public class ClientPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable table;
	private List<ClientDto> listData;
	private PaginatedTableDecorator<ClientDto> paginatedTableDecorator;
	private PaginationDataProvider<ClientDto> paginationDataProvider;

	@Autowired
	private ClientDataProvider clientDataProvider;

	@Autowired
	ClientDialog clientDialog;

	@Autowired
	ClientTable clientTable;

	@Autowired
	ClientRowAction clientRowAction;
	
	JTextField findText;

	public void initialize() {
		clientDialog.initialize();
		clientDialog.setVisible(false);

		setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.BUTTON_COLSPEC, ColumnSpec.decode("default:grow"),
						FormSpecs.BUTTON_COLSPEC, ColumnSpec.decode("256px:grow"), FormSpecs.BUTTON_COLSPEC,
						FormSpecs.BUTTON_COLSPEC, FormSpecs.BUTTON_COLSPEC },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("default:grow"), FormSpecs.UNRELATED_GAP_ROWSPEC, RowSpec.decode("22px:grow"),
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));
		{
			JLabel findLabel = new JLabel("Buscar");
			add(findLabel, "1, 2");

			findText = new JTextField();
			findText.addKeyListener(new KeyAdapter() {
			      @Override
				public void keyReleased(KeyEvent e) {
			        JTextField textField = (JTextField) e.getSource();
			        refresh(textField.getText());
			      }

			      @Override
				public void keyTyped(KeyEvent e) {
			      }

			      @Override
				public void keyPressed(KeyEvent e) {
			      }
			    });
			add(findText, "2, 2");

			JButton showDialogButton = new JButton("Añadir");
			showDialogButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					clientDialog.clearData();
					clientDialog.setVisible(true);
				}
			});
			add(showDialogButton, "5, 2");
		}

		table = new JTable(clientTable);
		TableColumn column = table.getColumnModel().getColumn(9);
		column.setCellRenderer(clientRowAction);
		table.addMouseListener(new JTableButtonMouseListener(table));

		table.setAutoCreateRowSorter(true);

		// table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if (me.getClickCount() == 2) {
					clientDialog.setVisible(true);
					clientDialog.fillData(listData.get(row));
				}
			}
		});

		// Add the scroll pane to this panel.
		//add(new JScrollPane(table));
		add(table, "1, 4, 4, 1, fill, fill");
		table.updateUI();

		paginationDataProvider = createDataProvider();
		paginatedTableDecorator = PaginatedTableDecorator.decorate(table, paginationDataProvider,
				new int[] { 10, 20, 50 }, 10);

		add(paginatedTableDecorator.getContentPanel(), "1, 6, 4, 1, fill, fill");
	}

	@SuppressWarnings("unchecked")
	public void refresh(String filter) {
		listData = clientDataProvider.getList(filter);

		TableModel model = table.getModel();
		int startIndex = (paginatedTableDecorator.getCurrentPage() - 1) * paginatedTableDecorator.getCurrentPageSize();
		int endIndex = startIndex + paginatedTableDecorator.getCurrentPageSize();
		if (endIndex > paginationDataProvider.getTotalRowCount()) {
			endIndex = paginationDataProvider.getTotalRowCount();
		}
		List<ClientDto> rows = paginationDataProvider.getRows(startIndex, endIndex);

		ObjectTableModel<ClientDto> objectTableModel = (ObjectTableModel<ClientDto>) model;

		objectTableModel.setObjectRows(rows);
		objectTableModel.fireTableDataChanged();
	}

	private PaginationDataProvider<ClientDto> createDataProvider() {
		listData = clientDataProvider.getList(findText.getText());

		return new PaginationDataProvider<ClientDto>() {
			@Override
			public int getTotalRowCount() {
				return listData.size();
			}

			@Override
			public java.util.List<ClientDto> getRows(int startIndex, int endIndex) {
				return listData.subList(startIndex, endIndex);
			}
		};
	}

}
