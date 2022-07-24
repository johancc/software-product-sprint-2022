function loadAllResources() {
  fetch("/all-resources")
    .then((response) => response.json())
    .then((resources) => {
      const resourceListElement = document.getElementById("resource-list");
      resources.forEach((newResource) => {
        resourceListElement.appendChild(createResourceElement(newResource));
      });
    });
}

function loadZipResources() {
  formValidation();
  const searchParam = document.getElementById("zipCodeEntry").value;
  if (!searchParam) return;
  const resourceListElement = document.getElementById("resource-list");
  const messageBox = document.getElementById("resource-message");
  messageBox.innerHTML = "Loading...";
  fetch("/zip-resources" + "?zipCode=" + searchParam)
    .then((response) => response.json())
    .then((resources) => {
      // Resource found
      if (resources.length == 0) {
        resourceListElement.innerHTML = "";
        messageBox.innerHTML = `Sorry, no nearby resource found. Add new resources <a href="/ResourceForm.html#goToForm">here</a>!`;
      } else {
        // Resource not found
        messageBox.innerHTML = `We found the following resources near you. You can also add new resources <a href="/ResourceForm.html#goToForm">here</a>!`;
        resourceListElement.innerHTML = "";
        resources.forEach((newResource) => {
          resourceListElement.appendChild(createResourceElement(newResource));
        });
      }
    });
}

function createResourceElement(newResource) {
  let dashCard = document.createElement("div");
  dashCard.className = "newResource";
  let {
    name: resource_name,
    phone,
    email,
    url,
    zip,
    description,
    category,
  } = newResource;

  dashCard.innerHTML = `
  <div class="col-auto mb-3">
  <div class="card" style="width: 18rem;">
      <div class="card-body">
          <h5> ${resource_name} </h5>
          <p> ${description}</p>
          <ul class="list-group list-group-flush">
              <li class="list-group-item"> ${phone} </li> 
              <li class="list-group-item"> ${email} </li>  
              <li class="list-group-item"> ${zip} </li> 
              <li class="list-group-item"> ${category} </li> 
              <a href="${url}" class="list-group-item"> ${url} </a>
          </ul>
      </div>
  </div>
</div>`;
  return dashCard;
}

function search_card() {
  let input = document.getElementById("searchbar").value;
  input = input.toLowerCase();
  let x = document.getElementsByClassName("col-auto mb-3");

  for (i = 0; i < x.length; i++) {
    if (!x[i].innerText.toLowerCase().includes(input)) {
      x[i].style.display = "none";
    } else {
      x[i].style.display = "inline";
    }
  }
}
