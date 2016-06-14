$ ->
  $.get "/products", (rows) ->
    $.each rows, (index, row) ->
      nombre = $("<td>").text row.nombre
      cost = $("<td>").text row.cost
      percent = $("<td>").text row.percent
      price = $("<td>").text row.price
      descripcion = $("<td>").text row.descripcion
      unitMeasureName = $("<td>").text row.unitMeasureName
      currentAmount = $("<td>").text row.currentAmount
      links = $("<td>").html '<a href="/product_update/' + row.id + '" class="btn btn-primary">Editar</a>' + '<a href="/product_remove/' + row.id + '" class="btn btn-danger">Eliminar</a>' + '<a href="/product_show/' + row.id + '" class="btn btn-info">Mostrar</a>'
      $("#rows").append $("<tr>").append(nombre).append(cost).append(percent).append(price).append(descripcion).append(unitMeasureName).append(currentAmount).append(links)
