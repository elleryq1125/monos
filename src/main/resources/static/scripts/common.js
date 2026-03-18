// モーダルのフィールドエラークリア
function clearModalFieldErros(){
	   document.querySelectorAll(".field-error")
        .forEach(e => e.innerText = "");
}

// モーダルのフィールドエラー表示
function showModalFieldErrors(fieldErrors){
	for(const key in fieldErrors){
		const element = document.getElementById(key + "Error");

        if(element){
            element.innerText = fieldErrors[key];
    	}
	}
}

// モーダル終了
function hideModal(modalId){
	const modalEl = document.getElementById(modalId);
	const modal = bootstrap.Modal.getInstance(modalEl);
    modal.hide();
}