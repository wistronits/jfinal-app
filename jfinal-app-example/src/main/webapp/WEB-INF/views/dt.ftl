<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="http://ajax.aspnetcdn.com/ajax/bootstrap/3.0.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/css/jquery.dataTables.css">
    <link rel="stylesheet" href="${ctx}/assets/css/datatables.css">
    <title>dt示例</title>
</head>
<body>
<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">jFinal-app 示例</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="${ctx}/">首页</a></li>
                <li><a href="${ctx}/dt">bt插件示例</a></li>
                <li><a href="#about">关于我</a></li>
                <li><a href="#contact">联系我</a></li>
            </ul>
        </div>
        <!--/.nav-collapse -->
    </div>
</div>

<div class="container">

    <table cellpadding="0" cellspacing="0" border="0" class="datatable table table-striped table-bordered">
    </table>

</div>

<script type="text/javascript" src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="http://ajax.aspnetcdn.com/ajax/bootstrap/3.0.3/bootstrap.js"></script>
<script type="text/javascript"
        src="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="${ctx}/assets/js/datatables.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        var $datatable = $('.datatable');
        $datatable.dataTable({
            "sPaginationType": "bs_normal",
            "bServerSide": true,
            "sAjaxSource": "${ctx}/dt"
        });
        $datatable.each(function () {
            var datatable = $(this);
            // SEARCH - Add the placeholder for Search and Turn this into in-line form control
            var search_input = datatable.closest('.dataTables_wrapper').find('div[id$=_filter] input');
            search_input.attr('placeholder', 'Search');
            search_input.addClass('form-control input-sm');
            // LENGTH - Inline-Form control
            var length_sel = datatable.closest('.dataTables_wrapper').find('div[id$=_length] select');
            length_sel.addClass('form-control input-sm');
            datatable.bind('page', function (e) {
                window.console && console.log('pagination event:', e) //this event must be fired whenever you paginate
            });
        });
    });
</script>
</body>
</html>