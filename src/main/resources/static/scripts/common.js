// モーダルのフィールドエラークリア
function clearModalFieldErros(modal){
	   modal.querySelectorAll(".field-error")
        .forEach(e => e.innerText = "");
}

// モーダルのフィールドエラー表示
function showModalFieldErrors(modal, fieldErrors){
	for(const key in fieldErrors){
		const element = modal.querySelector(`#${key}Error`);

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

/** 
 * 商品IDから倉庫と在庫数を取得して倉庫セレクトを更新
 * @param {number} productId - 商品ID
 * @param {string} warehouseElId - 倉庫セレクトのID
 */
async function loadWarehouseAndInventory(productId, warehouseElId){
    const res = await fetch(`/api/inventories/warehouse-availabilities?productId=${encodeURIComponent(productId)}`);
    const result = await res.json();

    const warehouseEl = document.getElementById(warehouseElId);
    warehouseEl.disabled = false;
    warehouseEl.innerHTML = "";

    if (!Array.isArray(result) || result.length === 0){
        const option = document.createElement("option");    
        option.value = "";
        option.textContent = "該当商品を保管している倉庫がありません";
        warehouseEl.appendChild(option);
        warehouseEl.disabled = true;
        return;
    }

    result.forEach(w => {
        const option = document.createElement("option");
        option.value = w.warehouseId;
        option.textContent = `${w.warehouseName} (在庫数: ${w.availableQty.toString()})`;
        warehouseEl.appendChild(option);
    });

}

/** 
 * 商品サジェストを設定する
 * @param {string} options.keyword - キーワード入力フィールドのID
 * @param {string} options.suggestList - サジェストリストのID
 * @param {string} options.product - 商品名表示フィールドのID
 * @param {string} options.productId - 商品ID表示フィールドのID
 * @param {Function} options.onSelected - 商品が選択されたときのコールバック関数（省略可）
 */
function setProductSuggest({
    keyword,
    suggestList,
    product,
    productId,
    onSelected
}) {
	// 要素取得
   const keywordEl = document.getElementById(keyword);
   const suggestListEl = document.getElementById(suggestList);
   const productEl = document.getElementById(product);
   const productIdEl = document.getElementById(productId);

   keywordEl.addEventListener("input", async () => {
      const keywordText = keywordEl.value.trim();

      if (!keywordText) {
         suggestListEl.innerHTML = "";
         return;
      }
      
      const res = await fetch(`/api/products/suggest?keyword=${encodeURIComponent(keywordText)}`);
      const result = await res.json();

      suggestListEl.innerHTML = "";

      result.forEach(p => {
         const li = document.createElement("li");
         li.textContent = `${p.productCode} ${p.name}`;

         li.onclick = () => {
             keywordEl.value = li.textContent;
             productEl.value = li.textContent;
             productIdEl.value = p.productId;
             suggestListEl.innerHTML = "";

            // 商品が選択されたときのコールバック関数を呼び出す
            if(onSelected){
                onSelected(p.productId);
            }
         };

		 li.className = "list-group-item list-group-item-action";
         
         suggestListEl.appendChild(li);
      });
   });
}