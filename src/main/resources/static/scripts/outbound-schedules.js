document.addEventListener("DOMContentLoaded", () => {
	// 商品サジェスト設定
    setProductSuggest({
        keyword: "modalProductKeyword",
        suggestList: "modalProductSuggestList",
        product:"modalProduct",
        productId: "modalProductId",
		onSelected: (productId) => {
			loadInventory(productId, "modalInventory");
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
		  modalEl.outboundScheduleId.value = "";
		  modalEl.outboundScheduleVersion.value = "";
		  modalEl.productKeyword.value = "";
		  modalEl.product.value = "";
		  modalEl.productId.value = "";
		  modalEl.inventory.innerHTML = "";
		  modalEl.scheduleQty.value = "";
		  modalEl.scheduleDate.value = "";
		  break;
		  
	  case "update":
		  modalEl.title.innerText = "出庫予定更新";
		  break;
	  }
	  
	  modalEl.productKeyword.disabled = false;
	  modalEl.product.disabled = true;
	  modalEl.inventory.disabled = true;
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
		outboundScheduleVersion: modalEl.version.value,
		inventoryId: modalEl.warehouse.value,
		inventoryVersion: modalEl.warehouse.options[modalEl.warehouse.selectedIndex].dataset.version,
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

// 入庫実績登録モーダル表示
async function openInboundResultModal(inboundScheduleId){
	const modal = document.getElementById("inboundResultModal");
	const modalEl = getInboundResultModalElements(modal);
  
  	modalEl.inboundScheduleId.value = inboundScheduleId;
  	
  	clearModalFieldErros(modal);
	  
	// 入庫予定情報を取得
  	const res = await fetch("/api/inboundschedules/" + inboundScheduleId);
 	const result = await res.json();
	
	if (result.success){
		setDataForInboundResultModal(modalEl, result.data);
	} else{
		handleInboundReulstModalLoadError(result.message);
	}
}

// 入庫実績登録モーダルの要素取得
function getInboundResultModalElements(modal) {
    return {
        title: modal.querySelector("#modalTitle"),
        inboundScheduleId: modal.querySelector("#modalInboundScheduleId"),
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

// 入庫実績登録モーダルにデータ設定
function setDataForInboundResultModal(modalEl, data){
	modalEl.version.value = data.version;
	modalEl.product.value = `${data.productCode} ${data.productName}`;
	modalEl.warehouse.value = `${data.warehouseCode} ${data.warehouseName}`;
	modalEl.scheduleQty.value = data.scheduleQty;
	modalEl.scheduleDate.value = data.scheduleDate;
	modalEl.totalResultQty.value = data.totalResultQty;
}

// 入庫実績モーダルのエラーハンドリング
function handleInboundReulstModalLoadError(message){
	// モーダルを非表示にしてエラーメッセージ表示
	hideModal("inboundResultModal");
	setErrorMessage(message);
	search();
}

// 入庫実績登録処理
async function registerInboundResult(){
	// CSRFトークン取得
	const csrfToken = document.querySelector("meta[name='_csrf']").content;
	const csrfHeader = document.querySelector("meta[name='_csrf_header']").content;
	
	const modal = document.getElementById("inboundResultModal");
	const modalEl = getInboundResultModalElements(modal);
	
	clearModalFieldErros(modal);
	
	// 入力データを設定
	const form = {
		inboundScheduleId: modalEl.inboundScheduleId.value,
		inboundScheduleVersion: modalEl.version.value,
		totalResultQty: modalEl.totalResultQty.value,
		resultQty: modalEl.resultQty.value,
		resultDate: modalEl.resultDate.value
	};
	
	// リクエスト
	const res = await fetch("/api/inboundschedules/result/regist",{
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
		hideModal("inboundResultModal");
		setSuccessMessage(result.message);
		search();
	}else{
		if (result.message == null){
			// バリデーションエラー表示
			showModalFieldErrors(modal, result.fieldErrors);
		}else{
			handleInboundReulstModalLoadError(result.message);
		}
	}
}

// 検索
function search(){
	document.getElementById("searchForm").submit();
}