row_id = location.pathname.split('/')[2]
$ ->
  $.get "/products/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#nombre").html row.nombre
      $("#cost").html row.cost
      $("#percent").html row.percent
      $("#price").html row.price
      $("#descripcion").html row.descripcion
      $("#unitMeasure").html row.unitMeasureName
      $("#currentAmount").html row.currentAmount
