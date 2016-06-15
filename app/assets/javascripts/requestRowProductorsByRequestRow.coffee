row_id = location.pathname.split('/')[2]

$ ->
  $.get "/requestRowProductorsByRequestRow/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      id = $("<td>").text row.id
      requestRowId = $("<td>").text row.requestRowId
      productName = $("<td>").text row.productName
      productorName = $("<td>").text row.productorName
      quantity = $("<td>").text row.quantity
      precio = $("<td>").text row.precio
      status = $("<td>").text row.status
      links = $("<td>").html '<a href="/requestRowProductor_cancel/' + row.id + '" class="btn btn-danger">Cancelar</a>' + '<a href="/requestRowProductor_fill/' + row.id + '" class="btn btn-primary">Entregar</a>' + '<a href="/requestRowProductor_update/' + row.id + '" class="btn btn-primary">Editar</a>' + '<a href="/requestRowProductor_remove/' + row.id + '" class="btn btn-danger">Eliminar</a>' + '<a href="/requestRowProductor_show/' + row.id + '" class="btn btn-info">Mostrar</a>'
      $("#rows").append $("<tr>").append(id).append(requestRowId).append(productName).append(productorName).append(quantity).append(precio).append(status).append(links)
