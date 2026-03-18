// 商品登録モーダル表示
function openProductAddModal(){  
  document.getElementById("modalTitle").innerText = "商品登録";
  document.getElementById("modalProductId").value = "";
  document.getElementById("modalProductCode").value = "";
  document.getElementById("modalName").value = "";
  document.getElementById("modalUnit").value = "";
}

// 商品更新モーダル表示
async function openProductUpdateModal(productId){
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
	} else{
		alert(result.message);
		return;
	}
}

// 商品追加・更新処理
async function saveProduct(){
	// CSRFトークン取得
	const csrfToken = document.querySelector("meta[name='_csrf']").content;
	const csrfHeader = document.querySelector("meta[name='_csrf_header']").content;
	
	// エラークリア
	clearModalFieldErros();
	
	// 入力データを設定
	const form = {
		productId: document.getElementById("modalProductId").value,
		productCode: document.getElementById("modalProductCode").value,
		name: document.getElementById("modalName").value,
		unit: document.getElementById("modalUnit").value
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
		showSuccessAlert(result.message);
	}else{
		if (result.message == null){
			// バリデーションエラー表示
			showModalFieldErrors(result.fieldErrors);
		}else{
			// モーダルを非表示にしてエラーメッセージ表示
			hideModal("productModal");
			showErrorAlert(result.message);
		}
	}
}