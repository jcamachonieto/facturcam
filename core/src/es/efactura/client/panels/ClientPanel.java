package es.efactura.client.panels;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.text.TableView.TableRow;

import es.efactura.client.dialog.ClientDialog;
import es.efactura.client.model.ClientDto;
import es.efactura.client.table.ClientRowAction;
import es.efactura.client.table.ClientTable;
import es.efactura.table.JTableButtonMouseListener;
import es.efactura.table.PaginatedTableDecorator;
import es.efactura.table.PaginationDataProvider;

public class ClientPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClientPanel() {
		super(new GridLayout(3, 0));
		
		ClientDialog clientDialog = new ClientDialog(600, 600);
		clientDialog.setVisible(false);
		
		JButton showDialogButton = new JButton("Añadir");
		showDialogButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  clientDialog.clearData();
			  clientDialog.setVisible(true);
		  }
		});
		add(showDialogButton);

		JTable table = new JTable(new ClientTable(clientDialog));
		ClientRowAction rowAction = new ClientRowAction();
		TableColumn column = table.getColumnModel().getColumn(3);
		column.setCellRenderer(rowAction);
		table.addMouseListener(new JTableButtonMouseListener(table));

		table.setAutoCreateRowSorter(true);

		// table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);

		// Add the scroll pane to this panel.
		add(table);

		PaginationDataProvider<ClientDto> dataProvider = createDataProvider();
		PaginatedTableDecorator<ClientDto> paginatedDecorator = PaginatedTableDecorator.decorate(table, dataProvider,
				new int[] { 10, 20, 50 }, 10);
		
		add(paginatedDecorator.getContentPanel());
	}

	private PaginationDataProvider<ClientDto> createDataProvider() {

		final List<ClientDto> list = new ArrayList<>();
        for (int i = 1; i <= 500; i++) {
        	ClientDto e = new ClientDto();
            e.setId(i);
            e.setName("name" + i);
            e.setCif("cif" + i);
            list.add(e);
        }

		return new PaginationDataProvider<ClientDto>() {
			@Override
			public int getTotalRowCount() {
				return list.size();
			}

			@Override
			public java.util.List<ClientDto> getRows(int startIndex, int endIndex) {
				return list.subList(startIndex, endIndex);
			}
		};
	}

}
