function loadResources() {
  fetch("/get-all-resources")
      .then((response) => response.json())
      .then((resources) => {
        const resourceListElement = document.getElementById("resource-list");
        resources.forEach((newResource) => {
          resourceListElement.appendChild(createResourceElement(newResource));
        });
      });
}

function createResourceElement(newResource) {
  let dashCard = document.createElement("div");
  dashCard.className = "newResource";
  let {
    name : resource_name,
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
                  <ul class='list-group list-group-flush'>
                  <li> ${phone} </li> 
                  <li> ${email} </li>  
                  <li> ${zip} </li>
                  <p> ${description}</p> 
                  <li> ${category} </li> 
                  </ul>
                  <div class="card-body">
                  <a href="${url}"> ${url} </a>
                  </div>
              </div>
          </div>
      </div>`;
  return dashCard;
}
