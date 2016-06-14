$ ->
  $.get "/discountDetails", (rows) ->
    $.each rows, (index, row) ->
      discountDetail = $("<td>").text row.discountDetail
      productorName = $("<td>").text row.productorName
      status = $("<td>").text row.status
      discount = $("<td>").text row.discount
      links = $("<td>").html '<a href="/discountDetail_update/' + row.id + '" class="btn btn-primary">Editar</a>' + '<a href="/discountDetail_remove/' + row.id + '" class="btn btn-danger">Eliminar</a>' + '<a href="/discountDetail_show/' + row.id + '" class="btn btn-info">Mostrar</a>'
      $("#rows").append $("<tr>").append(discountDetail).append(productorName).append(status).append(discount).append(links)
