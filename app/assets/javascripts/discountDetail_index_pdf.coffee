$ ->
  $.get "/discountDetails", (rows) ->
    $.each rows, (index, row) ->
      discountReport = $("<td>").text row.discountReport
      productorId = $("<td>").text row.productorId
      discount = $("<td>").text row.discount
      $("#rows").append $("<tr>").append(discountReport).append(productorId).append(discount)
