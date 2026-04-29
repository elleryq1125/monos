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

// 商品サジェスト設定
function setProductSuggest({
    keyword,
    suggestList,
    product,
    productId
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
             productEl.vallue = li.textContent;
             productIdEl.value = p.productId;
             suggestListEl.innerHTML = "";
         };

		 li.className = "list-group-item list-group-item-action";
         
         suggestListEl.appendChild(li);
      });
   });
}