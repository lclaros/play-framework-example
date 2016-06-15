row_id = location.pathname.split('/')[2]
$ ->
  $.get "/requestRowsByParent/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      id = $("<td>").text row.id
      requestId = $("<td>").text row.requestId
      productName = $("<td>").text row.productName
      quantity = $("<td>").text row.quantity
      precio = $("<td>").text row.precio
      status = $("<td>").text row.status
      links = $("<td>").html '<a href="/requestRow_cancel/' + row.id + '" class="btn btn-danger">Cancelar</a>' + '<a href="/requestRow_fill/' + row.id + '" class="btn btn-primary">Entregar</a>' + '<a href="/requestRow_update/' + row.id + '" class="btn btn-primary">Editar</a>' + '<a href="/requestRow_remove/' + row.id + '" class="btn btn-danger">Eliminar</a>' + '<a href="/requestRow_show/' + row.id + '" class="btn btn-info">Mostrar</a>'
      $("#rows").append $("<tr>").append(id).append(requestId).append(productName).append(quantity).append(precio).append(status).append(links)
