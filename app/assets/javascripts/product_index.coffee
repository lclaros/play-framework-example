$ ->
  $.get "/products", (rows) ->
    $.each rows, (index, row) ->
      name = $("<td>").text row.name
      cost = $("<td>").text row.cost
      percent = $("<td>").text row.percent
      price = $("<td>").text row.price
      description = $("<td>").text row.description
      measureName = $("<td>").text row.measureName
      currentAmount = $("<td>").text row.currentAmount
      links = $("<td>").html '<a href="/product_update/' + row.id + '" class="btn btn-primary btn-sm">Editar</a>' + '<a href="/product_remove/' + row.id + '" class="btn btn-danger btn-sm">Eliminar</a>' + '<a href="/product_show/' + row.id + '" class="btn btn-info btn-sm">Mostrar</a>'
      $("#rows").append $("<tr>").append(name).append(cost).append(percent).append(price).append(description).append(measureName).append(currentAmount).append(links)
