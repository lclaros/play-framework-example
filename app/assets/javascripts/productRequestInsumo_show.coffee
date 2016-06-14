row_id = location.pathname.split('/')[2]
$ ->
  $.get "/productRequestByInsumos/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#date").html row.date
      $("#status").html row.status
      $("#moduleName").html row.moduleName
      $("#detail").html row.detail
