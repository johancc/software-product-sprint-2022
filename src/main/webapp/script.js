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
  const dashCard = document.createElement("div");
  dashCard.className = 'Resource';

  const nameElement = document.createElement('h5');
  nameElement.innerText = newResource.name;

  const phoneElement = document.createElement('li');
  phoneElement.innerText = newResource.phone;

  const emailElement = document.createElement('li');
  emailElement.innerText = newResource.email;

  const urlElement = document.createElement('a');
  urlElement.innerText = newResource.url;

  const zipElement = document.createElement('li');
  zipElement.innerText = newResource.zip;

  const descriptionElement = document.createElement('p');
  descriptionElement.innerText = newResource.description;

  const categoryElement = document.createElement('li');
  categoryElement.innerText = newResource.category;

  dashCard.innerHTML = `<div class='col-4'>`
  `<div class='card-body'>`
  nameElement
  descriptionElement
  `</div>`
  `<ul class='list-group list-group-flush'>`
  phoneElement
  emailElement
  zipElement
  categoryElement
  `</ul>`
  `<div class="card-body">`
  urlElement
  `</div>`
  `</div>`;
  return dashCard;
}

function validateURL(link) {
  var url = link.value;
  if (!~url.indexOf("http")) {
    url = "http://" + url;
  }
  link.value = url;
  return link
}