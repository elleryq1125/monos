document.addEventListener("DOMContentLoaded", () => {
	// 商品サジェスト設定
    setProductSuggest({
        keyword: "modalProductKeyword",
        suggestList: "modalProductSuggestList",
        product:"modalProduct",
        productId: "modalProductId",
		onSelected: (productId) => {
			loadInventory(productId, "modalInventory", null);
		}
    });
    
});

// 出庫予定登録モーダル表示
function openOutboundScheduleAddModal(){
  const modal = document.getElementById("outboundScheduleModal");
  const modalEl = getOutboundScheduleModalElements(modal);
	
  clearModalFieldErros(modal);
  
  initOutboundScheduleModal(modalEl, "add");
}

// 出庫予定情報モーダルの要素取得
function getOutboundScheduleModalElements(modal) {
    return {
        title: modal.querySelector("#modalTitle"),
        outboundScheduleId: modal.querySelector("#modalOutboundScheduleId"),
        outboundScheduleVersion: modal.querySelector("#modalOutboundScheduleVersion"),
        productKeyword: modal.querySelector("#modalProductKeyword"),
        product: modal.querySelector("#modalProduct"),
        productId: modal.querySelector("#modalProductId"),
        inventory: modal.querySelector("#modalInventory"),
        scheduleDate: modal.querySelector("#modalScheduleDate"),
        scheduleQty: modal.querySelector("#modalScheduleQty")
    };
}

// 出庫予定モーダル初期化
function initOutboundScheduleModal(modalEl, mode){
  switch(mode){
	  case "add":
		  modalEl.title.innerText = "出庫予定登録";
		  break;
		  
	  case "update":
		  modalEl.title.innerText = "出庫予定更新";
		  break;
	  }
	  
	  // 入力項目初期化
	  modalEl.outboundScheduleId.value = "";
	  modalEl.outboundScheduleVersion.value = "";
	  modalEl.productKeyword.value = "";
	  modalEl.product.value = "";
	  modalEl.productId.value = "";
	  modalEl.inventory.innerHTML = "";
	  modalEl.scheduleQty.value = "";
	  modalEl.scheduleDate.value = "";

	  // 入力項目の状態初期化
	  modalEl.productKeyword.disabled = false;
	  modalEl.product.disabled = true;
	  modalEl.inventory.disabled = true;
	  modalEl.scheduleQty.disabled = false;
	  modalEl.scheduleDate.disabled = false;
}

// 出庫予定更新モーダル表示
async function openOutboundScheduleUpdateModal(outboundScheduleId){
	const modal = document.getElementById("outboundScheduleModal");
	const modalEl = getOutboundScheduleModalElements(modal);
	
  	clearModalFieldErros(modal);
	
	initOutboundScheduleModal(modalEl, "update");
	
	// 出庫予定情報を取得
	const res = await fetch("/api/outbound-schedules/" + outboundScheduleId);
	const result = await res.json();
	
	if (result.success){
		setDataForOutboundScheduleUpdateModal(modalEl, result.data);
		toggleStateOutboundScheduleUpdateForm(modalEl, result.data.status);
	} else{
		handleOutboundScheduleLoadError(result.message);
	}
}

// 出庫予定更新モーダルにデータ設定
function setDataForOutboundScheduleUpdateModal(modalEl, data){
	modalEl.outboundScheduleId.value = data.outboundScheduleId;
	modalEl.outboundScheduleVersion.value = data.version;
	modalEl.product.value = `${data.productCode} ${data.productName}`;
	modalEl.productId.value = data.productId;
	modalEl.scheduleDate.value = data.scheduleDate;
	modalEl.scheduleQty.value = data.scheduleQty;

	// 在庫リスト作成
	loadInventory(data.productId, "modalInventory", data.outboundScheduleId);
}

// 出庫予定更新モーダルの入力項目の状態切り替え
function toggleStateOutboundScheduleUpdateForm(modalEl, status){
	switch(status){
		case 1:
			// 出庫中
			modalEl.productKeyword.disabled =true;
			modalEl.inventory.disabled=true;
			modalEl.scheduleQty.disabled = true;
			break;
	}
}

// 出庫予定追加・更新処理
async function saveOutboundSchedule(){
	// CSRFトークン取得
	const csrfToken = document.querySelector("meta[name='_csrf']").content;
	const csrfHeader = document.querySelector("meta[name='_csrf_header']").content;
	
	const modal = document.getElementById("outboundScheduleModal");
	const modalEl = getOutboundScheduleModalElements(modal);
	
	clearModalFieldErros(modal);
	
	// 入力データを設定
	const form = {
		outboundScheduleId: modalEl.outboundScheduleId.value,
		outboundScheduleVersion: modalEl.outboundScheduleVersion.value,
		inventoryId: modalEl.inventory.value,
		inventoryVersion: modalEl.inventory.selectedOptions[0] ? modalEl.inventory.selectedOptions[0].dataset.version : "",
		scheduleQty: modalEl.scheduleQty.value,
		scheduleDate: modalEl.scheduleDate.value
	};
	
	// リクエスト
	const res = await fetch("/api/outbound-schedules/save",{
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
		hideModal("outboundScheduleModal");
		setSuccessMessage(result.message);
		search();
	}else{
		if (result.message == null){
			// バリデーションエラー表示
			showModalFieldErrors(modal, result.fieldErrors);
		}else{
			handleOutboundScheduleLoadError(result.message);
		}
	}
}

// 出庫予定モーダルのエラーハンドリング
function handleOutboundScheduleLoadError(message){
	// モーダルを非表示にしてエラーメッセージ表示
	hideModal("outboundScheduleModal");
	setErrorMessage(message);
	search();
}


// 出庫実績登録モーダル表示
async function openOutboundResultModal(outboundScheduleId){
	const modal = document.getElementById("outboundResultModal");
	const modalEl = getOutboundResultModalElements(modal);
  
  	modalEl.outboundScheduleId.value = outboundScheduleId;
  	
  	clearModalFieldErros(modal);
	  
	// 出庫予定情報を取得
  	const res = await fetch("/api/outbound-schedules/" + outboundScheduleId);
 	const result = await res.json();
	
	if (result.success){
		setDataForOutboundResultModal(modalEl, result.data);
	} else{
		handleOutboundResultModalLoadError(result.message);
	}
}

// 出庫実績登録モーダルの要素取得
function getOutboundResultModalElements(modal) {
    return {
        title: modal.querySelector("#modalTitle"),
        outboundScheduleId: modal.querySelector("#modalOutboundScheduleId"),
        version: modal.querySelector("#modalVersion"),
        product: modal.querySelector("#modalProduct"),
        warehouse: modal.querySelector("#modalWarehouse"),
        scheduleQty: modal.querySelector("#modalScheduleQty"),
        scheduleDate: modal.querySelector("#modalScheduleDate"),
        totalResultQty: modal.querySelector("#modalTotalResultQty"),
        resultQty: modal.querySelector("#modalResultQty"),
        resultDate: modal.querySelector("#modalResultDate")
    };
}

// 出庫実績登録モーダルにデータ設定
function setDataForOutboundResultModal(modalEl, data){
	modalEl.version.value = data.version;
	modalEl.product.value = `${data.productCode} ${data.productName}`;
	modalEl.warehouse.value = `${data.warehouseCode} ${data.warehouseName}`;
	modalEl.scheduleQty.value = data.scheduleQty;
	modalEl.scheduleDate.value = data.scheduleDate;
	modalEl.totalResultQty.value = data.totalResultQty;
}

// 出庫実績モーダルのエラーハンドリング
function handleOutboundResultModalLoadError(message){
	// モーダルを非表示にしてエラーメッセージ表示
	hideModal("OutboundResultModal");
	setErrorMessage(message);
	search();
}

// 出庫実績登録処理
async function registerOutboundResult(){
	// CSRFトークン取得
	const csrfToken = document.querySelector("meta[name='_csrf']").content;
	const csrfHeader = document.querySelector("meta[name='_csrf_header']").content;
	
	const modal = document.getElementById("outboundResultModal");
	const modalEl = getOutboundResultModalElements(modal);
	
	clearModalFieldErros(modal);
	
	// 入力データを設定
	const form = {
		outboundScheduleId: modalEl.outboundScheduleId.value,
		version: modalEl.version.value,
		resultQty: modalEl.resultQty.value,
		resultDate: modalEl.resultDate.value
	};
	
	// リクエスト
	const res = await fetch("/api/outbound-schedules/result/regist",{
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
		hideModal("outboundResultModal");
		setSuccessMessage(result.message);
		search();
	}else{
		if (result.message == null){
			// バリデーションエラー表示
			showModalFieldErrors(modal, result.fieldErrors);
		}else{
			handleOutboundResultModalLoadError(result.message);
		}
	}
}

// 検索
function search(){
	document.getElementById("searchForm").submit();
}