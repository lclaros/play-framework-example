$ ->
  $.get "/accounts", (rows) ->
    $.each rows, (index, row) ->
      code = $("<td>").text row.code
      name = $("<td>").text row.name
      type_1 = $("<td>").text row.type_1
      parent = $("<td>").text row.parent
      description = $("<td>").text row.description
      child = $("<td>").text row.child
      link = $("<td>").html '<a href="/account_update/' + row.id + '" class="btn btn-primary">Editar</a>' + '<a href="/account_remove/' + row.id + '" class="btn btn-danger">Eliminar</a>' + '<a href="/account_show/' + row.id + '" class="btn btn-info">Mostrar</a>'
      $("#rows").append $("<tr>").append(code).append(name).append(type_1).append(parent).append(description).append(child).append(link)