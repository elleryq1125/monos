document.addEventListener("DOMContentLoaded", () => {
	// 商品サジェスト設定
    setProductSuggest({
        keyword: "modalProductKeyword",
        suggestList: "modalProductSuggestList",
        product:"modalProduct",
        productId: "modalProductId"
    });
    
});

// 入庫予定情報モーダルの要素取得
function getInboundScheduleModalElements(modal) {
    return {
        title: modal.querySelector("#modalTitle"),
        inboundScheduleId: modal.querySelector("#modalInboundScheduleId"),
        version: modal.querySelector("#modalVersion"),
        productKeyword: modal.querySelector("#modalProductKeyword"),
        product: modal.querySelector("#modalProduct"),
        productId: modal.querySelector("#modalProductId"),
        warehouse: modal.querySelector("#modalWarehouse"),
        scheduleDate: modal.querySelector("#modalScheduleDate"),
        scheduleQty: modal.querySelector("#modalScheduleQty")
    };
}

// 入庫予定登録モーダル表示
function openInboundScheduleAddModal(){
  const modal = document.getElementById("inboundScheduleModal");
  const modalEl = getInboundScheduleModalElements(modal);
	
  clearModalFieldErros(modal);
  
  initInboundScheduleModal(modalEl, "add");
}

// 入庫予定モーダル初期化
function initInboundScheduleModal(modalEl, mode){
  switch(mode){
	  case "add":
		  modalEl.title.innerText = "入庫予定登録";
		  modalEl.inboundScheduleId.value = "";
		  modalEl.version.value = "";
		  modalEl.productKeyword.value = "";
		  modalEl.product.value = "";
		  modalEl.productId.value = "";
		  modalEl.scheduleQty.value = "";
		  modalEl.scheduleDate.value = "";
		  break;
		  
	  case "update":
		  modalEl.title.innerText = "入庫予定更新";
		  break;
	  }
	  
	  modalEl.productKeyword.disabled = false;
	  modalEl.product.disabled = true;
	  modalEl.warehouse.disabled = false;
	  modalEl.scheduleQty.disabled = false;
	  modalEl.scheduleDate.disabled = false;
}

// 入庫予定更新モーダル表示
async function openInboundScheduleUpdateModal(inboundScheduleId){
	const modal = document.getElementById("inboundScheduleModal");
	const modalEl = getInboundScheduleModalElements(modal);
	
  	clearModalFieldErros(modal);
	
	initInboundScheduleModal(modalEl, "update");
	
	modalEl.inboundScheduleId.value = inboundScheduleId;
	
	// 入庫予定情報を取得
	const res = await fetch("/api/inboundschedules/" + inboundScheduleId);
	const result = await res.json();
	
	if (result.success){
		setDataForInboundScheduleUpdateModal(modalEl, result.data);
		toggleStateInboudScheduleUpdateForm(modalEl, result.data.status);
	} else{
		handleInboundScheduleLoadError(result.message);
	}
}

// 入庫予定更新モーダルにデータ設定
function setDataForInboundScheduleUpdateModal(modalEl, data){
	modalEl.version.value = data.version;
	modalEl.product.value = `${data.productCode} ${data.productName}`;
	modalEl.productId.value = data.productId;
	modalEl.warehouse.value = data.warehouseId;
	modalEl.scheduleDate.value = data.scheduleDate;
	modalEl.scheduleQty.value = data.scheduleQty;
}

// 入庫予定更新モーダルの入力項目の状態切り替え
function toggleStateInboudScheduleUpdateForm(modalEl, status){
	switch(status){
		case 1:
			// 入庫中
			modalEl.productKeyword.disabled =true;
			modalEl.warehouse.disabled=true;
			break;
			
		case 2,3:
			// 入庫済、キャンセル
			modalEl.productKeyword.disabled = true;
			modalEl.warehouse.disabled = true;
			modalEl.scheduleDate.disabled = true;
			modalEl.scheduleQty.disabled = true;
			break;
	}
}

// 入庫予定追加・更新処理
async function saveInboundSchedule(){
	// CSRFトークン取得
	const csrfToken = document.querySelector("meta[name='_csrf']").content;
	const csrfHeader = document.querySelector("meta[name='_csrf_header']").content;
	
	const modal = document.getElementById("inboundScheduleModal");
	const modalEl = getInboundScheduleModalElements(modal);
	
	clearModalFieldErros(modal);
	
	// 入力データを設定
	const form = {
		inboundScheduleId: modalEl.inboundScheduleId.value,
		version: modalEl.version.value,
		productId: modalEl.productId.value,
		warehouseId: modalEl.warehouse.value,
		scheduleQty: modalEl.scheduleQty.value,
		scheduleDate: modalEl.scheduleDate.value
	};
	
	// リクエスト
	const res = await fetch("/api/inboundschedules/save",{
		method:"POST",
		headers:{
			"Content-Type":"application/json",
			[csrfHeader]: csrfToken
		},
		body:JSON.stringify(form)
	});
	const result = await res.json();
	
	if (result.success){	
		// モーダルを非表示にして成功メッセージ表示
		hideModal("inboundScheduleModal");
		setSuccessMessage(result.message);
		search();
	}else{
		if (result.message == null){
			// バリデーションエラー表示
			showModalFieldErrors(modal, result.fieldErrors);
		}else{
			handleInboundScheduleLoadError(result.message);
		}
	}
}

// 入庫予定モーダルのエラーハンドリング
function handleInboundScheduleLoadError(message){
	// モーダルを非表示にしてエラーメッセージ表示
	hideModal("inboundScheduleModal");
	setErrorMessage(message);
	search();
}

// 検索
function search(){
	document.getElementById("searchForm").submit();
}