document.addEventListener("DOMContentLoaded", () => {
  const resizeInput = (element, font) => {
    const size = font * 0.7;
    element.style.width = element.value.length * size + "px";
  };

  const resizableInputs = document.querySelectorAll("[data-resize-font]");
  resizableInputs.forEach((input) => {
    const font = Number(input.dataset.resizeFont || "0");
    if (!font) {
      return;
    }
    const handler = () => resizeInput(input, font);
    input.addEventListener("keyup", handler);
    handler();
  });

  const toggleEditable = (choice) => {
    const display1 = choice === 1 ? "inline" : "none";
    const display2 = choice === 1 ? "none" : "inline-block";

    const submitButton = document.getElementById("s_button");
    const editButton = document.getElementById("e_button");
    const cancelButton = document.getElementById("c_button");

    if (submitButton) {
      submitButton.style.display = display1;
    }
    if (editButton) {
      editButton.style.display = display2;
    }
    if (cancelButton) {
      cancelButton.style.display = display1;
    }

    const firstNameInput = document.getElementById("I_firstName");
    const firstNameSpan = document.getElementById("S_firstName");
    if (firstNameInput && firstNameSpan) {
      firstNameInput.style.width = 2 + firstNameSpan.offsetWidth + "px";
    }

    const lastNameInput = document.getElementById("I_lastName");
    const lastNameSpan = document.getElementById("S_lastName");
    if (lastNameInput && lastNameSpan) {
      lastNameInput.style.width = 2 + lastNameSpan.offsetWidth + "px";
    }

    document
      .querySelectorAll("*[id*=I_]")
      .forEach((element) => (element.style.display = display1));
    document
      .querySelectorAll("*[id*=S_]")
      .forEach((element) => (element.style.display = display2));
    document
      .querySelectorAll(".hint")
      .forEach((element) => (element.style.display = display1));
    document
      .querySelectorAll(".error")
      .forEach((element) => (element.style.display = "none"));
  };

  const editButton = document.getElementById("e_button");
  if (editButton) {
    editButton.addEventListener("click", () => toggleEditable(1));
  }

  const cancelButton = document.getElementById("c_button");
  if (cancelButton) {
    cancelButton.addEventListener("click", () => toggleEditable(0));
  }
});
