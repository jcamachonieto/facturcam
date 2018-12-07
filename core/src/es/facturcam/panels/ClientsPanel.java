package es.facturcam.panels;

import java.awt.Dimension;
import java.awt.GridLayout;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import es.facturcam.model.ClientDto;
import es.facturcam.table.ClientsTable;
import es.facturcam.table.PaginatedTableDecorator;
import es.facturcam.table.PaginationDataProvider;

public class ClientsPanel extends JPanel {

	public ClientsPanel() {
		// super(new GridLayout(1, 0));

		JTable table = new JTable(new ClientsTable());

		table.setAutoCreateRowSorter(true);

		// table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);

		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		// Add the scroll pane to this panel.
		add(scrollPane);

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
