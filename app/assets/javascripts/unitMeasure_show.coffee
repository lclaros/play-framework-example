row_id = location.pathname.split('/')[2]

$ ->
  $.get "/measures/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#name").html row.name
      $("#quantity").html row.quantity
      $("#description").html row.description
