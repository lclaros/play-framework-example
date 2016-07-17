
$ ->
  $.get "/requestRowByInsumos", (rows) ->
    $.each rows, (index, row) ->
      requestId = $("<td>").text row.requestId
      productId = $("<td>").text row.productId
      quantity = $("<td>").text row.quantity
      price = $("<td>").text row.price
      paid = $("<td>").text row.paid
      status = $("<td>").text row.status
      links = $("<td>").html '<a href="/requestRowByInsumo_cancel/' + row.id + '" class="btn btn-danger btn-sm">Cancelar</a>' + '<a href="/requestRowByInsumo_fill/' + row.id + '" class="btn btn-primary btn-sm">Entregar</a>' + '<a href="/requestRowByInsumo_update/' + row.id + '" class="btn btn-primary btn-sm">Editar</a>' + '<a href="/requestRowByInsumo_remove/' + row.id + '" class="btn btn-danger btn-sm">Eliminar</a>' + '<a href="/requestRowByInsumo_show/' + row.id + '" class="btn btn-info btn-sm">Mostrar</a>'
      $("#rows").append $("<tr>").append(requestId).append(productId).append(quantity).append(price).append(paid).append(status).append(links)
