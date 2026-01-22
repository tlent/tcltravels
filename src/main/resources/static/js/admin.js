document.addEventListener("DOMContentLoaded", () => {
  const employeeTable = document.getElementById("employee_table");
  const editBtn = document.getElementById("edit_employee");
  const deleteBtn = document.getElementById("del_employee");
  const cancelEditBtn = document.getElementById("cancel_edit");
  const cancelDeleteBtn = document.getElementById("cancel_delete");
  const employeeHint = document.getElementById("employee_hint");

  const setMode = (mode) => {
    if (!employeeTable) {
      return;
    }
    employeeTable.dataset.mode = mode;
    const rows = employeeTable.querySelectorAll("tbody tr[data-person-id]");
    rows.forEach((row) => {
      row.classList.remove("edit_active", "del_active");
      if (mode === "edit") {
        row.classList.add("edit_active");
      } else if (mode === "delete") {
        row.classList.add("del_active");
      }
    });
  };

  if (editBtn && deleteBtn && cancelEditBtn && cancelDeleteBtn && employeeHint) {
    editBtn.addEventListener("click", () => {
      editBtn.style.display = "none";
      deleteBtn.style.display = "block";
      cancelEditBtn.style.display = "inline-block";
      cancelDeleteBtn.style.display = "none";
      employeeHint.style.display = "block";
      employeeHint.textContent = "Click on the employee you want to edit";
      setMode("edit");
    });

    deleteBtn.addEventListener("click", () => {
      editBtn.style.display = "block";
      deleteBtn.style.display = "none";
      cancelEditBtn.style.display = "none";
      cancelDeleteBtn.style.display = "inline-block";
      employeeHint.style.display = "block";
      employeeHint.textContent = "Click on the employee you want to delete";
      setMode("delete");
    });

    const resetMode = () => {
      editBtn.style.display = "block";
      deleteBtn.style.display = "block";
      cancelEditBtn.style.display = "none";
      cancelDeleteBtn.style.display = "none";
      employeeHint.style.display = "none";
      employeeHint.textContent = "";
      setMode("none");
    };

    cancelEditBtn.addEventListener("click", resetMode);
    cancelDeleteBtn.addEventListener("click", resetMode);
  }

  if (employeeTable) {
    employeeTable.addEventListener("click", (event) => {
      const row = event.target.closest("tr[data-person-id]");
      if (!row) {
        return;
      }
      const mode = employeeTable.dataset.mode;
      if (!mode || mode === "none") {
        return;
      }
      const personId = row.dataset.personId;
      if (!personId) {
        return;
      }
      if (mode === "edit") {
        window.location.href = `/admin/employees/${personId}/edit`;
      } else if (mode === "delete") {
        window.location.href = `/admin/employees/${personId}/delete`;
      }
    });
  }

});
