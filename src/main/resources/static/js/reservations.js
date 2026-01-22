document.addEventListener("DOMContentLoaded", () => {
  const passengerCount = document.querySelector("[data-passenger-count]");
  if (!passengerCount) {
    return;
  }

  const hideInputToggle = () => {
    const count = Number(passengerCount.value);
    for (let i = 1; i <= 5; i++) {
      const toggleDisplayIds = [`person${i}_head`, `person${i}`];
      const toggleDisableIds = [
        `first${i}`,
        `last${i}`,
        `class${i}`,
        `food${i}`,
      ];
      if (count >= i) {
        for (const id of toggleDisplayIds) {
          const element = document.getElementById(id);
          if (element) {
            element.style.display = "table-row";
          }
        }
        for (const id of toggleDisableIds) {
          const element = document.getElementById(id);
          if (element) {
            element.removeAttribute("disabled");
          }
        }
      } else {
        for (const id of toggleDisplayIds) {
          const element = document.getElementById(id);
          if (element) {
            element.style.display = "none";
          }
        }
        for (const id of toggleDisableIds) {
          const element = document.getElementById(id);
          if (element) {
            element.setAttribute("disabled", true);
          }
        }
      }
    }
  };

  passengerCount.addEventListener("change", hideInputToggle);
  hideInputToggle();
});
