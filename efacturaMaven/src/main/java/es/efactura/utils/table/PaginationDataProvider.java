package es.efactura.utils.table;

import java.util.List;

public interface PaginationDataProvider<T> {
	int getTotalRowCount();

	List<T> getRows(int startIndex, int endIndex);
}