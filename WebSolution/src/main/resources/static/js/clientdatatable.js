var table;
$(document)
		.ready(
				function() {
					table = $('#clientTable')
							.DataTable(
									{
										"sAjaxSource" : "/client/list",
										"sAjaxDataProp" : "",
										"order" : [ [ 0, "asc" ] ],
										"aoColumns" : [ {
											"mData" : "id"
										}, {
											"mData" : "name"
										}, {
											"mData" : "cif"
										}, {
											"mData" : "address"
										}, {
											"mData" : "location"
										}, {
											"mData" : "province"
										}, {
											"mData" : "postalCode"
										}, {
											"mData" : "country"
										}, {
											"mData" : "telephone"
										}, {
											"mData" : "email"
										} ],
										"dom" : '<"top"if>rt<"bottom"lp><"clear">',
										"columnDefs" : [
												{
													"targets" : 0,
													"visible" : false
												},
												{
													"targets" : 10,
													"data" : null,
													"defaultContent" : "<button id='edit' data-toggle='modal' data-target='#modalForm' class='btn btn-secundary'>Editar</button> <button id='delete' class='btn btn-secundary'>Eliminar</button>"
												} ],
										"language" : {
											"lengthMenu" : "Mostrar _MENU_ datos por pagina.",
											"zeroRecords" : "No se encontraron resultados.",
											"info" : "Mostrando _PAGE_ de _PAGES_",
											"infoEmpty" : "No hay datos disponibles",
											"infoFiltered" : "(filtrado de _MAX_ datos totales)",
											"search" : "Buscar:",
											"paginate" : {
												"first" : "Primero",
												"last" : "Último",
												"next" : "Siguiente",
												"previous" : "Anterior"
											}
										}
									});

					$('#clientTable tbody').on('click', 'button', function() {
						var data = table.row($(this).parents('tr')).data();
						if (this.id === 'edit') {
							$("#modalFormLabel").text("Editar cliente");
							$("#modalForm #id").val(data['id']);
							$("#modalForm #name").val(data['name']);
							$("#modalForm #cif").val(data['cif']);
							$("#modalForm #address").val(data['address']);
							$("#modalForm #location").val(data['location']);
							$("#modalForm #province").val(data['province']);
							$("#modalForm #postalCode").val(data['postalCode']);
							$("#modalForm #country").val(data['country']);
							$("#modalForm #telephone").val(data['telephone']);
							$("#modalForm #email").val(data['email']);
						} else {
							remove(data);
						}
					});

					$('#add').on('click', function() {
						$("#modalFormLabel").text("Añadir cliente");
					});
					
				});

function remove(data) {
	$.confirm({
		title : 'Eliminar cliente',
		closeIcon: true,
		content : '¿ Desea eliminar el cliente ' + data['name'] + '?',
		buttons : {
			Eliminar : {
				btnClass : 'btn-red',
				action : function() {
					$.ajax({
						type : 'DELETE',
						url : '/client/' + data['id'],
						cache : false,
						success : function(data) {
							$.confirm({
								title : data.title,
								content : data.text,
								type : data.type,
								typeAnimated : true,
								autoClose : 'cerrar|8000',
								buttons : {
									cerrar : function() {
									}
								}
							});
							table.ajax.reload(null, false);
						},
						error : function(data) {
							$.confirm({
								title : data.title,
								content : data.text,
								type : data.type,
								typeAnimated : true,
								autoClose : 'cerrar|8000',
								buttons : {
									cerrar : function() {
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