$ ->
  $.get "/proveedores", (rows) ->
    $.each rows, (index, row) ->
      nombre = $("<td>").text row.nombre
      telefono = $("<td>").text row.telefono
      direccion = $("<td>").text row.direccion
      contacto = $("<td>").text row.contacto
      account = $("<td>").text row.account
      links = $("<td>").html '<a href="/proveedor_update/' + row.id + '" class="btn btn-primary">Editar</a>' + '<a href="/proveedor_remove/' + row.id + '" class="btn btn-danger">Eliminar</a>' + '<a href="/proveedor_show/' + row.id + '" class="btn btn-info">Mostrar</a>'
      $("#rows").append $("<tr>").append(nombre).append(telefono).append(direccion).append(contacto).append(account).append(links)
