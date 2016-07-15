page = location.pathname.split('/')[2]

$ ->
  $.get "/productoresPage/" + page, (rows) ->
    $.each rows, (index, row) ->
      cl = $("<td>")
      name = $("<td>").text row.name
      carnet = $("<td>").text row.carnet
      telefono = $("<td>").text row.telefono
      account = $("<td>").text row.account
      moduleName = $("<td>").text row.moduleName
      link = $("<td>").html '<a href="/productor_update/' + row.id + '" class="btn btn-primary btn-sm">Editar</a>' + '<a href="/productor_remove/' + row.id + '" class="btn btn-danger btn-sm">Eliminar</a>' + '<a href="/productor_show/' + row.id + '" class="btn btn-info btn-sm">Mostrar</a>'
      $("#rows tbody").append $("<tr>").append(cl).append(name).append(carnet).append(telefono).append(account).append(moduleName).append(link)