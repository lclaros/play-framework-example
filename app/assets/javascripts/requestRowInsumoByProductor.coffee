row_id = location.pathname.split('/')[2]
$ ->
  $.get "/requestRowsByProductor/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      requestId = $("<td>").text row.requestId
      productId = $("<td>").text row.productId
      productorId = $("<td>").text row.productorId
      quantity = $("<td>").text row.quantity
      price = $("<td>").text row.price
      paid = $("<td>").text row.paid
      status = $("<td>").text row.status
      links = $("<td>").html '<a href="/requestRow_show/' + row.id + '" class="btn btn-info btn-sm">Mostrar</a>'
      $("#rows").append $("<tr>").append(requestId).append(productId).append(productorId).append(quantity).append(price).append(paid).append(status).append(links)
