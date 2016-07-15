$ ->
  $.get "/proveedores", (rows) ->
    $.each rows, (index, row) ->
      name = $("<td>").text row.name
      telefono = $("<td>").text row.telefono
      direccion = $("<td>").text row.direccion
      contacto = $("<td>").text row.contacto
      account = $("<td>").text row.account
      links = $("<td>").html '<a href="/proveedor_update/' + row.id + '" class="btn btn-primary btn-sm">Editar</a>' + '<a href="/proveedor_remove/' + row.id + '" class="btn btn-danger btn-sm">Eliminar</a>' + '<a href="/proveedor_show/' + row.id + '" class="btn btn-info btn-sm">Mostrar</a>'
      $("#rows").append $("<tr>").append(name).append(telefono).append(direccion).append(contacto).append(account).append(links)
