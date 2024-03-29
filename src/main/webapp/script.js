// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

function validateURL(link) {
  let url = link.value;
  if (!~url.indexOf("http")) {
    url = "http://" + url;
  }
  link.value = url;
  return link;
}

function formValidation() {
  "use strict";

  // Fetch the Resource Form
  let forms = document.querySelectorAll(".needs-validation");

  // Loop over inputs and prevent invalid submission
  Array.prototype.slice.call(forms).forEach(function (form) {
    form.addEventListener(
      "submit",
      function (event) {
        event.preventDefault();
        if (!form.checkValidity()) {
          event.preventDefault();
          event.stopPropagation();
        }
        form.classList.add("was-validated");
      },
      false
    );
  });
}

async function handleSubmit() {
  // Handle category data checkboxes
  let categoryData = [
    ...document.querySelectorAll(".form-check-input:checked"),
  ].map((e) => e.value);
  console.log("type: " + typeof categoryData + " value: " + categoryData);
  if (document.getElementById("otherCategory").value) {
    categoryData.push(document.getElementById("otherCategory").value);
  }

  // Handle form data
  const formData = {
    name: document.getElementById("nameEntry").value,
    phone: document.getElementById("phoneEntry").value,
    email: document.getElementById("emailEntry").value,
    url: document.getElementById("urlEntry").value,
    zip: document.getElementById("zipCodeEntry").value,
    description: document.getElementById("descriptionEntry").value,
    category: categoryData,
  };

  const queryURL = "/new-resource?" + new URLSearchParams(formData);

  // Make POST request to create new resource
  $("#toast-loading").toast("show");

  fetch(queryURL, {
    method: "POST",
  }).then((response) => {
    if (response.ok) {
      $("#toast-success").toast("show");
      setTimeout(() => {
        window.location.href = "/dashboard.html";
      }, 5000);
    } else {
      $("#toast-failure").toast("show");
    }
  });
}
