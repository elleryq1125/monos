async function openInventoryUpdateModal(inventoryId) {
  const modal = document.getElementById("inventoryModal");
  clearModalFieldErros(modal);
  const res = await fetch("/api/inventories/" + inventoryId);
  const result = await res.json();

  if (result.success) {
    const inventory = result.data;
    document.getElementById("modalInventoryId").value = inventory.inventoryId;
    document.getElementById("modalInventoryVersion").value = inventory.version;
    document.getElementById("modalProductCode").value = inventory.productCode;
    document.getElementById("modalProductName").value = inventory.productName;
    document.getElementById("modalWarehouseCode").value = inventory.warehouseCode;
    document.getElementById("modalWarehouseName").value = inventory.warehouseName;
    document.getElementById("modalOnHandQty").value = inventory.onHandQty;
    document.getElementById("modalReorderPoint").value = inventory.reorderPoint;
    document.getElementById("modalAppropriateStockQty").value = inventory.appropriateStockQty;
  } else {
    hideModal("inventoryModal");
    setErrorMessage(result.message);
    search();
  }
}

async function saveInventory() {
  const csrfToken = document.querySelector("meta[name='_csrf']").content;
  const csrfHeader = document.querySelector("meta[name='_csrf_header']").content;
  const modal = document.getElementById("inventoryModal");
  clearModalFieldErros(modal);
  const form = {
    inventoryId: document.getElementById("modalInventoryId").value,
    version: document.getElementById("modalInventoryVersion").value,
    reorderPoint: document.getElementById("modalReorderPoint").value,
    appropriateStockQty: document.getElementById("modalAppropriateStockQty").value
  };

  const res = await fetch("/api/inventories/save", {
    method: "POST",
    headers: { "Content-Type": "application/json", [csrfHeader]: csrfToken },
    body: JSON.stringify(form)
  });
  const result = await res.json();

  if (result.success) {
    hideModal("inventoryModal");
    setSuccessMessage(result.message);
    search();
  } else if (result.message == null) {
    showModalFieldErrors(modal, result.fieldErrors);
  } else {
    hideModal("inventoryModal");
    setErrorMessage(result.message);
    search();
  }
}

function search() {
  document.getElementById("searchForm").submit();
}
