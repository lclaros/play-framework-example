$ ->
  $.get "/insumoUsers", (rows) ->
    $.each rows, (index, row) ->
      nombre = $("<td>").text row.nombre
      carnet = $("<td>").text row.carnet
      telefono = $("<td>").text row.telefono
      direccion = $("<td>").text row.direccion
      sueldo = $("<td>").text row.sueldo
      links = $("<td>").html '<a href="/insumoUser_update/' + row.id + '" class="btn btn-primary">Editar</a>' + '<a href="/insumoUser_remove/' + row.id + '" class="btn btn-danger">Eliminar</a>' + '<a href="/insumoUser_show/' + row.id + '" class="btn btn-info">Mostrar</a>' + '<a href="/insumoUser_profile/' + row.id + '" class="btn btn-info">Profile</a>'
      $("#rows").append $("<tr>").append(nombre).append(carnet).append(telefono).append(direccion).append(sueldo).append(links)