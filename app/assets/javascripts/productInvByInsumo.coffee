row_id = location.pathname.split('/')[2]

$ ->
  $.get "/ProductInvsByInsumo/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      productId = $("<td>").text row.productId
      proveedorId = $("<td>").text row.proveedorId
      amount = $("<td>").text row.amount
      amountLeft = $("<td>").text row.amountLeft
      links = $("<td>").html '<a href="/productInv_update/' + row.id + '" class="btn btn-primary">Editar</a>' + '<a href="/productInv_remove/' + row.id + '" class="btn btn-danger" >Eliminar</a>' + '<a href="/productInv_show/' + row.id + '" class="btn btn-info">Mostrar</a>'
      $("#rows").append $("<tr>").append(productId).append(proveedorId).append(amount).append(amountLeft).append(links)
