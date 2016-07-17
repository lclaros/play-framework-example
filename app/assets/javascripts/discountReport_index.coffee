$ ->
  $.get "/discountReports", (rows) ->
    $.each rows, (index, row) ->
      startDate = $("<td>").text row.startDate
      endDate = $("<td>").text row.endDate
      status = $("<td>").text row.status
      total = $("<td>").text row.total
      links = $("<td>").html '<a href="/discountReport_update/' + row.id + '" class="btn btn-primary btn-sm">Editar</a>' + '<a href="/discountReport_remove/' + row.id + '" class="btn btn-danger btn-sm">Eliminar</a>' + '<a href="/discountReport_finalize/' + row.id + '" class="btn btn-primary btn-sm">Finalizar</a>' + '<a href="/discountReport_show/' + row.id + '" class="btn btn-info btn-sm">Mostrar</a>' + '<a href="/discountReport_show_pdf/' + row.id + '" class="btn btn-info btn-sm">PDF</a>'
      $("#rows").append $("<tr>").append(startDate).append(endDate).append(status).append(total).append(links)
