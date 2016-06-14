$ ->
  $.get "/discountReports", (rows) ->
    $.each rows, (index, row) ->
      startDate = $("<td>").text row.startDate
      endDate = $("<td>").text row.endDate
      status = $("<td>").text row.status
      total = $("<td>").text row.total
      links = $("<td>").html '<a href="/discountReport_update/' + row.id + '" class="btn btn-primary">Editar</a>' + '<a href="/discountReport_remove/' + row.id + '" class="btn btn-danger">Eliminar</a>' + '<a href="/discountReport_finalize/' + row.id + '" class="btn btn-primary">Finalizar</a>' + '<a href="/discountReport_show/' + row.id + '" class="btn btn-info">Mostrar</a>' + '<a href="/discountReport_show_pdf/' + row.id + '" class="btn btn-info">PDF</a>'
      $("#rows").append $("<tr>").append(startDate).append(endDate).append(status).append(total).append(links)
