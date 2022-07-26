var resourceObjectArray = []; // All resources will live here so they can be accessed outside any functions
var resourceHTMLArray = []; // Corresponding HTML elements for objects will live here
var filteredTags = []; // Contains strings corresponding to the checkboxes that have been selected

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

  //resourceArray.push(newResource);
  //let resourceObject = {htmlElement:dashCard, JSObject:newResource};

  // The categories are being stored as one string in a one-element string array.
  // Split category string and store each category string into a new array:
  let categoryString = newResource.category;
  let categoryArray = categoryString[0].split(",");
  newResource.category = categoryArray;
  //console.log(categoryArray);

  // Convert categories to all lowercase for more effective filtering:

  for (i = 0; i < newResource.category.length; i++) {
    newResource.category[i] = newResource.category[i].toLowerCase();
  }
  console.log(newResource.category);
  resourceObjectArray.push(newResource);
  resourceHTMLArray.push(dashCard);
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

function addToFilteredTags(tagString) {
  filteredTags.push(tagString);
  console.log(filteredTags);
}

function toggleFilter() {
  // Temporarily disable checkboxes after user hits "Apply":
  document.getElementById("defaultCheck1").disabled = true;
  document.getElementById("defaultCheck2").disabled = true;
  document.getElementById("defaultCheck3").disabled = true;
  document.getElementById("defaultCheck4").disabled = true;
  document.getElementById("defaultCheck5").disabled = true;

  // For each resource:
  for (i = 0; i < resourceObjectArray.length; i++) {
    let match = false;
    let currentResource = resourceObjectArray[i]; // resourceObject = {htmlElement, JSObject}
    // For each filtered tag:
    for (j = 0; j < filteredTags.length; j++) {
      // Check if any of the categories for this resource are selected or filtered out:
      if (currentResource.category.includes(filteredTags[j])) {
        match = true;
      }
      //console.log(currentResource.category);
    }
    if (!match) {
      resourceHTMLArray[i].style.display = "none";
    }
    //resourceHTMLArray[i].style.display = "none";
    if (resourceHTMLArray[i].style.display == "none") {
      console.log(currentResource.name + " has been hidden!");
    }
  }
}
