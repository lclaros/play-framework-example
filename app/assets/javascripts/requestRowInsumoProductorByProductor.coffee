row_id = location.pathname.split('/')[2]
$ ->
  $.get "/requestRowProductorsByProductor/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      requestRowId = $("<td>").text row.requestRowId
      productId = $("<td>").text row.productId
      productorId = $("<td>").text row.productorId
      quantity = $("<td>").text row.quantity
      precio = $("<td>").text row.precio
      paid = $("<td>").text row.paid
      status = $("<td>").text row.status
      links = $("<td>").html '<a href="/requestRow_show/' + row.id + '" class="btn btn-info">Mostrar</a>'
      $("#requestRowProductors").append $("<tr>").append(requestRowId).append(productId).append(productorId).append(quantity).append(precio).append(paid).append(status).append(links)
