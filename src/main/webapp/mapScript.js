// Store resource information as name-geolocation pair in hashmap
async function getData() {
  const resourceMap = new Map();

  await fetch("/map-resources")
    .then((response) => response.json())
    .then((resources) => {
      resources.forEach((resource) => {
        resourceMap.set(resource.name, [resource.lat, resource.lon]);
      });
    });

  return resourceMap;
}

// Load map
async function initMap() {
  // Get resource name-geolocation hashmap
  const resourceMap = await getData();

  // Initialize Google map
  const map = new google.maps.Map(document.getElementById("mapContainer"), {
    center: { lat: 39.999947, lng: -97.702313 },
    zoom: 4,
    mapId: "a1a8966f509039c3",
  });

  // Store markers
  let markers = [];

  resourceMap.forEach((val, key) => {
    let latitude = val[0];
    let longitude = val[1];
    const marker = new google.maps.Marker({
      position: { lat: latitude, lng: longitude },
      map: map,
    });
    marker.addListener("click", () => {
      const infowindow = new google.maps.InfoWindow({
        content: key,
      });
      infowindow.open(map, marker);
    });
    markers.push(marker);
  });

  // Make markers into cluster
  new markerClusterer.MarkerClusterer({ markers, map });
}
