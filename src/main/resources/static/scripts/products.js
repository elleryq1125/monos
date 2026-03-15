// 商品登録モーダル表示
function openProductAddModal(){  
  document.getElementById("modalTitle").innerText = "商品登録";
}

// 商品更新モーダル表示
async function openProductUpdateModal(productId){
	 console.trace("modal open");
	
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
		showErrorAlert(result.message);
		return;
	}
}