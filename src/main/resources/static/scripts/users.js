$(function(){
	$("form[name='user-delete']").submit(function(){
		const userName = $(this).data('user-name');
		
		if (window.confirm(userName + 'を削除します。\nよろしいですか？')){
			return ture;
		}
		else{
			return false;
		}
	});
})