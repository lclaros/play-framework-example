row_id = location.pathname.split('/')[2]
$ ->
  $.get "/requestRowProductorsByProductor/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      requestRowId = $("<td>").text row.requestRowId
      productId = $("<td>").text row.productId
      productorId = $("<td>").text row.productorId
      quantity = $("<td>").text row.quantity
      price = $("<td>").text row.price
      totalPrice = $("<td>").text (row.quantity * row.price)
      status = $("<td>").text row.status
      links = $("<td>").html '<a href="/requestRow_show/' + row.id + '" class="btn btn-info btn-sm">Mostrar</a>'
      $("#requestRowProductors").append $("<tr>").append(requestRowId).append(productId).append(productorId).append(quantity).append(price).append(totalPrice).append(status).append(links)
