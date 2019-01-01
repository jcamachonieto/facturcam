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
										} , {
											"mData" : "year"
										} , {
											"mData" : "number"
										} , {
											"mData" : "clientName"
										} , {
											"mData" : "broadCast",
											"render": function ( oObj ) {
												return moment(oObj).format("DD/MM/YYYY");
											}
										} , {
											"mData" : "expiration",
											"render": function ( oObj ) {
												return moment(oObj).format("DD/MM/YYYY");
											}
										}],
										"dom" : '<"top"Bif>Brt<"bottom"lp><"clear">',
									    "buttons": [
									        'pdf'
									    ],
										"columnDefs" : [
												{
													"targets" : 0,
													"visible" : false
												},
												{
													"targets" : 6,
													"data" : null,
													"defaultContent" : "<button id='pdf' class='btn btn-secundary'>Pdf</button><button id='edit' data-toggle='modal' data-target='#modalForm' class='btn btn-secundary'>Editar</button> <button id='delete' class='btn btn-secundary'>Eliminar</button>"
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

					$('#billTable tbody')
							.on(
									'click',
									'button',
									function() {
										var data = table.row(
												$(this).parents('tr')).data();
										if (this.id === 'edit') {
											clearForm();
											$("#modalFormLabel").text(
													"Editar factura");
											$("#modalFormContent #id").val(data['id']);
											$("#modalFormContent #year").val(data['year']);
											$("#modalFormContent #number").val(data['number']);
											$("#modalFormContent #idClient").val(data['idClient']);
											$("#modalFormContent #broadCast").val(moment(data['broadCast']).format("DD/MM/YYYY"));
											$("#modalFormContent #expiration").val(moment(data['expiration']).format("DD/MM/YYYY"));
											$("#modalFormContent #tax").val(data['tax']);
											$.ajax({
												url : '/concept/bill/' + data['id'],
												type : 'get',
												success : function(data) {
													$(data).each(function(index, element) {
														tableConcepts.row.add({
															"description" : element.description,
															"taxBase" : element.taxBase,
															"quantity" : element.quantity,
														}).draw();
													});
												}
											});
										} else if (this.id === 'pdf') {
											var name = data['year'] + '_' + data['number'] + ".pdf";
											var req = new XMLHttpRequest();
										    req.open("POST", '/bill/download/' + data['id'], true);
										    req.responseType = "blob";
										    req.onreadystatechange = function () {
										        if (req.readyState === 4 && req.status === 200) {
										            var blob = req.response;
										            var link = document.createElement('a');
										            link.href = window.URL.createObjectURL(blob);
										            link.download = name;
										            link.click();
										        }
										    };
										    req.send();
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
						todayHighlight : true
					});

					$('#expiration').datepicker({
						todayHighlight : true
					});

					$('#addConcept').on('click', function() {
						tableConcepts.row.add({
							"description" : $("#description").val(),
							"taxBase" : $("#taxBase").val(),
							"quantity" : $("#quantity").val()
						}).draw();
						$("#description").val("");
						$("#taxBase").val("");
						$("#quantity").val("");
						$('#addConcept').prop("disabled", true);
					});

					tableConcepts = $('#concepts')
							.DataTable(
									{
										"aoColumns" : [ {
											"mData" : "description"
										}, {
											"mData" : "taxBase"
										}, {
											"mData" : "quantity"
										} ],
										"columnDefs" : [ {
											"targets" : 3,
											"data" : null,
											"defaultContent" : "<input type='button' id='conceptDelete' class='btn btn-secundary btn-red' value='Eliminar' onclick='deleteConcept(this)'/>"
										} ],
										"paging" : false,
										"ordering" : false,
										"info" : false,
										"searching" : false
									});

					var form = $('#modalFormContent');
					form.on('submit', function(e) {
						e.preventDefault();
						var concepts = [];
						tableConcepts.rows().data().each(function (value, index) { 
							concepts.push(
									{
										description: value.description, 
										quantity: value.quantity, 
										taxBase: value.taxBase
									});
							});
						$.ajax({
							url : form.attr('action'),
							type : 'post',
							data : {
								"bill" : JSON.stringify(
										{
											id: $("#modalFormContent #id").val(),
											number: $("#modalFormContent #number").val(),
											year: $("#modalFormContent #year").val(),
											idClient: $("#modalFormContent #idClient").val(),
											broadCast: $("#modalFormContent #broadCast").val(),
											expiration: $("#modalFormContent #expiration").val(),
											tax: $("#modalFormContent #tax").val()
										}
										),
								"concepts" : JSON.stringify(concepts),
							},
							success : function(data) {
								$('#modalForm').modal('hide');
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
						});
					});

				});

function deleteConcept(element) {
	var row = tableConcepts.row($(element).parents('tr'));
	var data = row.data();
	$.confirm({
		title : 'Eliminar concepto',
		closeIcon : true,
		content : '¿ Desea eliminar '
				+ data['description'] + '?',
		buttons : {
			Cancelar : {},
			Eliminar : {
				btnClass : 'btn-red',
				action : function() {
					row.remove().draw(false);
				}
			}
		}
	});
}

function activeAddConcept() {
	if ($("#description").val() != "" && $("#taxBase").val() != ""
			&& $("#quantity").val() != "") {
		$('#addConcept').prop("disabled", false);
	} else {
		$('#addConcept').prop("disabled", true);
	}
}

function clearForm() {
	$(':input', '#modalForm')
		.not(':button, :submit, :reset, :hidden').val('')
		.prop('checked', false)
		.prop('selected', false);
	$("#modalFormContent #idClient").val('');
	$("#modalFormContent #broadCast").val('');
	$("#modalFormContent #expiration").val('');
	$("#modalFormContent #tax").val('');
	tableConcepts.clear().draw();
}

function remove(data) {
	$.confirm({
		title : 'Eliminar factura',
		closeIcon : true,
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