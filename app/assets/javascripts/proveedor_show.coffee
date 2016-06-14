row_id = location.pathname.split('/')[2]
$ ->
  $.get "/proveedores/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#name").html row.nombre
      $("#telefono").html row.telefono
      $("#direccion").html row.direccion
      $("#contacto").html row.contacto
      $("#cuenta").html row.cuenta
