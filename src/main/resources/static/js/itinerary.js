document.addEventListener("DOMContentLoaded", () => {
  const toggles = document.querySelectorAll("[data-itinerary-toggle]");
  toggles.forEach((button) => {
    button.addEventListener("click", () => {
      const targetId = button.dataset.itineraryToggle;
      if (!targetId) {
        return;
      }
      const target = document.getElementById(targetId);
      if (!target) {
        return;
      }
      const isHidden = target.style.display === "none" || target.style.display === "";
      target.style.display = isHidden ? "table-row" : "none";
      button.textContent = isHidden ? "Close" : "Expand";
    });
  });
});
