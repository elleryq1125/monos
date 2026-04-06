// 倉庫登録モーダル表示
function openWarehouseAddModal(){  
　// フィールドエラークリア
  clearModalFieldErros();
  
  // 初期値設定
  document.getElementById("modalTitle").innerText = "倉庫登録";
  document.getElementById("modalWarehouseId").value = "";
  document.getElementById("modalWarehouseCode").value = "";
  document.getElementById("modalName").value = "";
  document.getElementById("modalActive").checked = true;
  
　// 倉庫コード活性
　document.getElementById("modalWarehouseCode").disabled = false;
}

// 倉庫更新モーダル表示
async function openWarehouseUpdateModal(warehouseId){
	// フィールドエラークリア
	clearModalFieldErros();
	
	document.getElementById("modalTitle").innerText = "倉庫更新";
	document.getElementById("modalWarehouseId").value = warehouseId;
	
	// 倉庫情報を取得
	const res = await fetch("/api/warehouses/" + warehouseId);
	const result = await res.json();
	
	if (result.success){
		// 戻り値を画面に設定
		const warehouse = result.data;
		document.getElementById("modalWarehouseCode").value = warehouse.warehouseCode;
		document.getElementById("modalName").value = warehouse.name;
		document.getElementById("modalActive").checked = warehouse.active;
		
		// 倉庫コード非活性
		document.getElementById("modalWarehouseCode").disabled = true;
	} else{
		// モーダルを非表示にしてエラーメッセージ表示
		hideModal("warehouseModal");
		setErrorMessage(result.message);
		search();
	}
}

// 倉庫追加・更新処理
async function saveWarehouse(){
	// CSRFトークン取得
	const csrfToken = document.querySelector("meta[name='_csrf']").content;
	const csrfHeader = document.querySelector("meta[name='_csrf_header']").content;
	
	// フィールドエラークリア
	clearModalFieldErros();
	
	// 入力データを設定
	const form = {
		warehouseId: document.getElementById("modalWarehouseId").value,
		warehouseCode: document.getElementById("modalWarehouseCode").value,
		name: document.getElementById("modalName").value,
		active: document.getElementById("modalActive").checked
	};
	
	// リクエスト
	const res = await fetch("/api/warehouses/save",{
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
		hideModal("warehouseModal");
		setSuccessMessage(result.message);
		search();
	}else{
		if (result.message == null){
			// バリデーションエラー表示
			showModalFieldErrors(result.fieldErrors);
		}else{
			// モーダルを非表示にしてエラーメッセージ表示
			hideModal("warehouseModal");
			setErrorMessage(result.message);
			search();
		}
	}
}

// 検索
function search(){
	document.getElementById("searchForm").submit();
}