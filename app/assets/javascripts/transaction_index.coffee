$ ->
  $.get "/transactions", (rows) ->
    $.each rows, (index, row) ->
      date = $("<td>").text row.date
      type_1 = $("<td>").text row.type_1
      description = $("<td>").text row.description
      createdByName = $("<td>").text row.createdByName
      receivedByName = $("<td>").text row.receivedByName
      autorizedByName = $("<td>").text row.autorizedByName
      links = $("<td>").html '<a href="/transaction_update/' + row.id + '"><span class="glyphicon glyphicon-pencil">Editar</span></a>' + '<a href="/transaction_remove/' + row.id + '"><span class="glyphicon glyphicon-remove">Eliminar</span></a>' + '<a href="/transaction_show/' + row.id + '"><span class="glyphicon glyphicon-remove">Mostrar</span></a>'
      $("#rows").append $("<tr>").append(date).append(type_1).append(description).append(createdByName).append(receivedByName).append(autorizedByName).append(links)
