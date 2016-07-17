row_id = location.pathname.split('/')[2]

$ ->
  $.get "/accounts/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#code").html row.code
      $("#name").html row.name
      $("#type_1").html row.type_1
      $("#description").html row.description
      $("#child").html row.child
