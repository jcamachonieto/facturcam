var table;
$(document).ready( function () {
	 table = $('#clientTable').DataTable({
			"sAjaxSource": "/client/list",
			"sAjaxDataProp": "",
			"order": [[ 0, "asc" ]],
			"aoColumns": [
			  { "mData": "id" },
		      { "mData": "name" },
		      { "mData": "cif" },
		      { "mData": "address" },
		      { "mData": "location" },
		      { "mData": "province" },
		      { "mData": "postalCode" },
		      { "mData": "country" },
		      { "mData": "telephone" },
		      { "mData": "email" }
			],
			"dom": '<"top"if>rt<"bottom"lp><"clear">',
			"columnDefs": [ 
				{
		            "targets": 0,
		            "visible" : false 
				},
				{
		            "targets": 10,
		            "data": null,
		            "defaultContent": "<button id='edit'>Editar</button> <button id='delete'>Eliminar</button>"
				} 
			],
	        "language": {
	            "lengthMenu": "Mostrar _MENU_ datos por pagina.",
	            "zeroRecords": "No se encontraron resultados.",
	            "info": "Mostrando _PAGE_ de _PAGES_",
	            "infoEmpty": "No hay datos disponibles",
	            "infoFiltered": "(filtrado de _MAX_ datos totales)",
	            "search": "Buscar:",
	            "paginate": {
	                "first":      "Primero",
	                "last":       "Último",
	                "next":       "Siguiente",
	                "previous":   "Anterior"
	            }
	        }
	 });
	 
	 $('#clientTable tbody').on( 'click', 'button', function () {
		var data = table.row( $(this).parents('tr') ).data();
	 	if (this.id === 'edit') {
	 		edit(data);
	 	} else {
	 		remove(data);
	 	}
	 });
	 
	 $('#add').on( 'click', function () {
		 showDialog('Nuevo cliente');
	 });
});

function edit(data) {
	showDialog('Modificar cliente' + data['name']);
}

function remove(data) {
	$.confirm({
	    title: 'Eliminar cliente',
	    content: '¿ Desea eliminar el cliente ' + data['name'] + '?',
	    buttons: {
	    	Cancelar: function () {
	        },
	        Eliminar: {
	        	btnClass: 'btn-red',
	        	action: function () {
	        		$.ajax({
	                    type: 'DELETE',
	                    url: '/client/' + data['id'],
	                    cache: false,
	                    success: function(data) {
	                    	$.confirm({
	                		    title: data.title,
	                		    content: data.text,
	                		    type: data.type,
	                		    typeAnimated: true,
	                		    autoClose: 'cerrar|8000',
	                		    buttons: {
	                		        cerrar: function () {
	                		        }
	                		    }
	                		});
	                    	table.ajax.reload( null, false );
	                    },
	                    error: function(data) {
	                    	$.confirm({
	                		    title: data.title,
	                		    content: data.text,
	                		    type: data.type,
	                		    typeAnimated: true,
	                		    autoClose: 'cerrar|8000',
	                		    buttons: {
	                		        cerrar: function () {
	                		        }
	                		    }
	                		});
	                    }
	                })
	        	}
	        }
	    }
	});
}

function showDialog(title) {
	$.confirm({
	    title: title,
	    content: function(){
            return $('#dialog').html(); 
        },
	    buttons: {
	    	Cancelar: function () {
	        },
	        Guardar: {
	        	btnClass: 'btn-green',
	        	action: function () {
	        		$("#dialogForm").submit();
	        	}
	        }
	    }
	});
}