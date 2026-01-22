document.addEventListener("DOMContentLoaded", () => {
  const toMoveElement = document.querySelector("#toMove");
  if (!toMoveElement) {
    return;
  }
  toMoveElement.addEventListener("change", () => {
    const target = document.getElementById(toMoveElement.value);
    if (target) {
      target.scrollIntoView({
        behavior: "smooth",
        block: "start",
      });
    }
  });
});
