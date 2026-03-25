// 商品登録モーダル表示
function openProductAddModal(){  
　// フィールドエラークリア
  clearModalFieldErros();
  
  // 初期値設定
  document.getElementById("modalTitle").innerText = "商品登録";
  document.getElementById("modalProductId").value = "";
  document.getElementById("modalProductCode").value = "";
  document.getElementById("modalName").value = "";
  document.getElementById("modalUnit").value = "";
  document.getElementById("modalActive").checked = true;
  
　// 商品コード活性
　document.getElementById("modalProductCode").disabled = false;
}

// 商品更新モーダル表示
async function openProductUpdateModal(productId){
	// フィールドエラークリア
	clearModalFieldErros();
	
	document.getElementById("modalTitle").innerText = "商品更新";
	document.getElementById("modalProductId").value = productId;
	
	// 商品情報を取得
	const res = await fetch("/api/products/" + productId);
	const result = await res.json();
	
	if (result.success){
		// 戻り値を画面に設定
		const product = result.data;
		document.getElementById("modalProductCode").value = product.productCode;
		document.getElementById("modalName").value = product.name;
		document.getElementById("modalUnit").value = product.unit;
		document.getElementById("modalActive").checked = product.active;
		
		// 商品コード非活性
		document.getElementById("modalProductCode").disabled = true;
	} else{
		// モーダルを非表示にしてエラーメッセージ表示
		hideModal("productModal");
		setErrorMessage(result.message);
		search();
	}
}

// 商品追加・更新処理
async function saveProduct(){
	// CSRFトークン取得
	const csrfToken = document.querySelector("meta[name='_csrf']").content;
	const csrfHeader = document.querySelector("meta[name='_csrf_header']").content;
	
	// フィールドエラークリア
	clearModalFieldErros();
	
	// 入力データを設定
	const form = {
		productId: document.getElementById("modalProductId").value,
		productCode: document.getElementById("modalProductCode").value,
		name: document.getElementById("modalName").value,
		unit: document.getElementById("modalUnit").value,
		active: document.getElementById("modalActive").checked
	};
	
	// リクエスト
	const res = await fetch("/api/products/save",{
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
		hideModal("productModal");
		setSuccessMessage(result.message);
		search();
	}else{
		if (result.message == null){
			// バリデーションエラー表示
			showModalFieldErrors(result.fieldErrors);
		}else{
			// モーダルを非表示にしてエラーメッセージ表示
			hideModal("productModal");
			setErrorMessage(result.message);
			search();
		}
	}
}

// 検索
function search(){
	document.getElementById("searchForm").submit();
}