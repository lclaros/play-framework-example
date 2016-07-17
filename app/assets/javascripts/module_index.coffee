$ ->
  $.get "/modules", (rows) ->
    $.each rows, (index, row) ->
      name = $("<td>").text row.name
      president = $("<td>").text row.president
      description = $("<td>").text row.description
      associationName = $("<td>").text row.associationName
      link = $("<td>").html '<a href="/module_update/' + row.id + '" class="btn btn-primary btn-sm">Editar</a>' + '<a href="/module_remove/' + row.id + '" class="btn btn-danger btn-sm">Eliminar</a>' + '<a href="/module_show/' + row.id + '" class="btn btn-info btn-sm">Mostrar</a>'
      $("#rows").append $("<tr>").append(name).append(president).append(description).append(associationName).append(link)