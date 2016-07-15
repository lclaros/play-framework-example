$ ->
  $.get "/requestRowProductors", (rows) ->
    $.each rows, (index, row) ->
      requestRowId = $("<td>").text row.requestRowId
      productName = $("<td>").text row.productName
      productorName = $("<td>").text row.productorName
      quantity = $("<td>").text row.quantity
      price = $("<td>").text row.price
      status = $("<td>").text row.status
      links = $("<td>").html '<a href="/requestRowProductor_cancel/' + row.id + '" class="btn btn-danger btn-sm">Cancelar</a>' + '<a href="/requestRowProductor_fill/' + row.id + '" class="btn btn-primary btn-sm">Entregar</a>' + '<a href="/requestRowProductor_update/' + row.id + '" class="btn btn-primary btn-sm">Editar</a>' + '<a href="/requestRowProductor_remove/' + row.id + '" class="btn btn-danger btn-sm">Eliminar</a>' + '<a href="/requestRowProductor_show/' + row.id + '" class="btn btn-info btn-sm">Mostrar</a>'
      $("#rows").append $("<tr>").append(requestRowId).append(productName).append(productorName).append(quantity).append(price).append(status).append(links)
