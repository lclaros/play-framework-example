$ ->
  $.get "/productInvs", (rows) ->
    $.each rows, (index, row) ->
      productId = $("<td>").text row.productId
      proveedorId = $("<td>").text row.proveedorId
      amount = $("<td>").text row.amount
      amountLeft = $("<td>").text row.amountLeft
      links = $("<td>").html '<a href="/productInv_request_row"><span class="glyphicon glyphicon-pencil">Add</span></a>'
      $("#rows_1").append $("<tr>").append(productId).append(proveedorId).append(amount).append(amountLeft).append(links)

$ ->
  $.get "/productInvs", (rows) ->
    $.each rows, (index, row) ->
      productId = $("<td>").text row.productId
      proveedorId = $("<td>").text row.proveedorId
      amount = $("<td>").text row.amount
      amountLeft = $("<td>").text row.amountLeft
      links = $("<td>").html '<a href="/productInv_request/' + row.id + '"><span class="glyphicon glyphicon-pencil">Update</span></a>' + '<a href="/productInv_remove/' + row.id + '"><span class="glyphicon glyphicon-remove">Eliminar</span></a>' + '<a href="/productInv_show/' + row.id + '"><span class="glyphicon glyphicon-remove">Mostrar</span></a>'
      $("#rows_2").append $("<tr>").append(productId).append(proveedorId).append(amount).append(amountLeft).append(links)

