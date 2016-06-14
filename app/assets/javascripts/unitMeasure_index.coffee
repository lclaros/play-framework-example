$ ->
  $.get "/unitMeasures", (rows) ->
    $.each rows, (index, row) ->
      name = $("<td>").text row.name
      quantity = $("<td>").text row.quantity
      description = $("<td>").text row.description
      link = $("<td>").html '<a href="/unitMeasure_update/' + row.id + '" class="btn btn-primary">Editar</a>' + '<a href="/unitMeasure_remove/' + row.id + '" class="btn btn-danger">Eliminar</a>' + '<a href="/unitMeasure_show/' + row.id + '" class="btn btn-info">Mostrar</a>'
      $("#rows").append $("<tr>").append(name).append(quantity).append(description).append(link)
