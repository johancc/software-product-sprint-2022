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
  const searchParam = document.getElementById("zipCodeEntry").value;
  fetch(
    "/zip-resources" +
      new URLSearchParams({
        zipCode: searchParam,
      })
  )
    .then((response) => {
      response.status == 200
        ? response.json()
        : (document.getElementById("resource-list").innerHTML =
            "Something went wrong, please try searching again.");
      return;
    })
    .then((resources) => {
      const resourceListElement = document.getElementById("resource-list");
      if (resources.length == 0) {
        resourceListElement.innerHTML = `Sorry, no nearby resource found. Add new resources <a href="/ResourceForm.html#goToForm">here</a>!`;
      } else {
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
