row_id = location.pathname.split('/')[2]
$ ->
  $.get "/products/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#name").html row.name
      $("#cost").html row.cost
      $("#percent").html row.percent
      $("#price").html row.price
      $("#description").html row.description
      $("#measure").html row.measureName
      $("#currentAmount").html row.currentAmount
