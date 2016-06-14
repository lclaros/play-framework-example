row_id = location.pathname.split('/')[2]
$ ->
  $.get "/transactions/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#date").html row.date
      $("#type_1").html row.type_1
      $("#description").html row.description
