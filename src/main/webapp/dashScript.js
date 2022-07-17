const DEBUG = true;

var isHidden = {
  // Keeps track of which tags/categories are hidden.
  finances: false,
  food: false,
  sponsorship: false,
  mentorship: false,
  education: false,
};

function loadResources() {
  fetch("/all-resources")
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
  for (i = 0; i < category.length; i++) {
    dashCard.classList.add(category[i]);
  }
  return dashCard;
}

function toggleFilter(tagString) {
  if (DEBUG) {
    console.log("toggleFilter() : tagString = " + tagString);
  }

  // Get all the elements associated with this class:
  var dashCards = document.getElementsByClassName(tagString);

  for (i = 0; i < dashCards.length; i++) {
    var card = dashCards[i];

    // Get all the class names associated with this card:
    var cardClassList = card.className.split(" ");
    if (DEBUG) {
      console.log(cardClassList);
    }

    // Determines if this card should be hidden or not:
    if (card.style.display === "none") {
      if (DEBUG) {
        console.log("This " + tagString + " card is currently hidden");
      }
      // If it's already hidden, make sure it doesn't have another class that's hidden:
      for (j = 0; j < cardClassList.length; j++) {
        var className = cardClassList[j];
        if (className != tagString) {
          if (isHidden[className] === true) {
            if (DEBUG) {
              console.log(
                className +
                  " is hidden too, so IT REMAINS HIDDEN! Returning from function..."
              );
            }
            // Don't do anything - this card remains hidden!
            var keepHidden = true;
            j = cardClassList.length; // get out of this for loop
          }
        }
      }
      if (!keepHidden) {
        // Else, if no other filters were applied to this card, we can unhide it!
        if (DEBUG) {
          console.log("No other filters on this card - unhiding now!");
        }
        isHidden[tagString] = false;
        card.style.display = "block";
      }
    } else {
      // If the card is visible, we can simply hide it.
      if (DEBUG) {
        console.log("This card is currently visible");
      }
      card.style.display = "none";
      isHidden[tagString] = true;
      if (DEBUG) {
        console.log("Hiding this card now!");
      }
    }
  }
}
