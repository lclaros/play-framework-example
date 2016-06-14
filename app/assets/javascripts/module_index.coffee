$ ->
  $.get "/modules", (rows) ->
    $.each rows, (index, row) ->
      name = $("<td>").text row.name
      president = $("<td>").text row.president
      description = $("<td>").text row.description
      asociacionName = $("<td>").text row.asociacionName
      link = $("<td>").html '<a href="/module_update/' + row.id + '" class="btn btn-primary">Editar</a>' + '<a href="/module_remove/' + row.id + '" class="btn btn-danger">Eliminar</a>' + '<a href="/module_show/' + row.id + '" class="btn btn-info">Mostrar</a>'
      $("#rows").append $("<tr>").append(name).append(president).append(description).append(asociacionName).append(link)