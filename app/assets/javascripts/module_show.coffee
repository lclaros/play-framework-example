row_id = location.pathname.split('/')[2]

$ ->
  $.get "/modules/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#name").html row.name
      $("#president").html row.president
      $("#description").html row.description
