$ ->
  $.get "/productInvs", (rows) ->
    $.each rows, (index, row) ->
      productId = $("<td>").text row.productId
      proveedorId = $("<td>").text row.proveedorId
      amount = $("<td>").text row.amount
      amountLeft = $("<td>").text row.amountLeft
      links = $("<td>").html '<a href="/productInv_update/' + row.id + '" class="btn btn-primary btn-sm">Editar</a>' + '<a href="/productInv_remove/' + row.id + '" class="btn btn-danger btn-sm">Eliminar</a>' + '<a href="/productInv_show/' + row.id + '" class="btn btn-info btn-sm">Mostrar</a>'
      $("#rows").append $("<tr>").append(productId).append(proveedorId).append(amount).append(amountLeft).append(links)
