row_id = location.pathname.split('/')[2]

$ ->
  $.get "/insumoUsers/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#name").html row.nombre
      $("#carnet").html row.carnet
      $("#telefono").html row.telefono
      $("#direccion").html row.direccion
      $("#sueldo").html row.sueldo
