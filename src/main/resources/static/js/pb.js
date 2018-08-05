var loading = {
	baosight: {
		showPageLoadingMsg: function(showMessage) {
			if($("#_loading_div").length == 0) {
				$("body").append('<div id="_loading_div"><span class="item-1"></span><span class="item-2"></span><span class="item-3"></span><span class="item-4"></span><span class="item-5"></span><span class="item-6"></span><span class="item-7"></span></div>');
			}
			if($("#_loadMsg").length == 0) {
				$("body").append('<div id="_loadMsg">正在加载,请稍候... ...</div>');
			}
			if(showMessage == true || showMessage == "true") {
				$("#_loadMsg").show();
			}
			$("#_loading_div").show();
		},
		hidePageLoadingMsg: function() {
			$("#_loading_div").hide();
			$("#_loadMsg").hide();
		}
	}
}