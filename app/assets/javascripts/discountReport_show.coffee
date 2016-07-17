row_id = location.pathname.split('/')[2]
$ ->
  $.get "/discountReports/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#startDate").html row.startDate
      $("#endDate").html row.endDate
      $("#status").html row.status
      $("#total").html row.total
