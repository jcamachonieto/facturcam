var table;
var tableConcepts;

$(document)
		.ready(
				function() {
					table = $('#billTable')
							.DataTable(
									{
										"sAjaxSource" : "/bill/list",
										"sAjaxDataProp" : "",
										"order" : [ [ 0, "asc" ] ],
										"aoColumns" : [ {
											"mData" : "id"
										}, {
											"mData" : "number"
										} ],
										"dom" : '<"top"if>rt<"bottom"lp><"clear">',
										"columnDefs" : [
												{
													"targets" : 0,
													"visible" : false
												},
												{
													"targets" : 2,
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
							clearForm();
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
						clearForm();
						$("#modalFormLabel").text("Añadir factura");
					});
					
					$.datepicker.setDefaults($.datepicker.regional['es']);
					$('#broadCast').datepicker({    
					    todayHighlight: true
					});
					
					$('#expiration').datepicker({    
					    todayHighlight: true
					});
					
					$('#addConcept').on('click', function() {
						tableConcepts.row.add( {
					        "description": $("#description").val(),
					        "taxBase": $("#taxBase").val(),
					        "quantity": $("#quantity").val()
					    } ).draw();
						$("#description").val("");
						$("#taxBase").val("");
						$("#quantity").val("");
						$('#addConcept').prop("disabled", true);
					});
					
					tableConcepts = $('#concepts').DataTable({
						"aoColumns" : [ {
							"mData" : "description"
						}, {
							"mData" : "taxBase"
						}, {
							"mData" : "quantity"
						} ],
				        "columnDefs": [ {
				            "targets": 3,
				            "data": null,
				            "defaultContent" : "<button id='delete' class='btn btn-secundary btn-red'>Eliminar</button>"
				        } ],
				        "paging":   false,
				        "ordering": false,
				        "info":     false,
				        "searching": false
				    });
					
					$('#concepts tbody').on('click', 'button', function() {
						var row = tableConcepts.row($(this).parents('tr'));
						var data = row.data();
						$.confirm({
							title : 'Eliminar concepto',
							closeIcon: true,
							content : '¿ Desea eliminar ' + data['description'] + '?',
							buttons : {
								Cancelar: {},
								Eliminar : {
									btnClass : 'btn-red',
									action : function() {
										row.remove().draw( false );
									}
								}
							}
						});
					});
					
				});

function activeAddConcept() {
	if ($("#description").val() != ""
		&& $("#taxBase").val() != ""
		&& $("#quantity").val() != "") {
		$('#addConcept').prop("disabled", false);
	} else {
		$('#addConcept').prop("disabled", true);
	}
}

function clearForm() {
	$(':input','#modalForm')
	  .not(':button, :submit, :reset, :hidden')
	  .val('')
	  .prop('checked', false)
	  .prop('selected', false);
}

function remove(data) {
	$.confirm({
		title : 'Eliminar factura',
		closeIcon: true,
		content : '¿ Desea eliminar ' + data['number'] + '?',
		buttons : {
			Eliminar : {
				btnClass : 'btn-red',
				action : function() {
					$.ajax({
						type : 'DELETE',
						url : '/bill/' + data['id'],
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