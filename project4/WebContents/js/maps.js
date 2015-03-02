function isEmpty(s) {
    return (!s || 0 === s.length);
}

function initialize(address, latitude, longitude) {
    var map;
    var address = address;
    var latlng = null;

    if (!isEmpty(latitude) && !isEmpty(longitude)) {
        latlng = new google.maps.LatLng(latitude, longitude);
        address = null;
    }

    if (isEmpty(address) && (isEmpty(latitude) || isEmpty(longitude)
        || (latitude > 90) || (latitude < -90) || (longitude > 180) || (longitude < -180))) {
        console.log("Geocode was not successful for the following reason: " + status);
        var myOptions = {
          zoom: 1,
          center: new google.maps.LatLng(0, 0),
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        map = new google.maps.Map(document.getElementById("map_canvas"),
            myOptions);
    }

    else {
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode( { 'address': address, 'location': latlng}, function(results, status) {

         if (status == google.maps.GeocoderStatus.ZERO_RESULTS) {
            console.log("Geocode was not successful for the following reason: " + status);
             var myOptions = {
                       zoom: 1,
                       center: new google.maps.LatLng(0, 0),
                       mapTypeId: google.maps.MapTypeId.ROADMAP
                     };
                     map = new google.maps.Map(document.getElementById("map_canvas"),
                         myOptions);
         }
         else if (status == google.maps.GeocoderStatus.OK) {
            var myOptions = {
                          zoom: 8,
                          center: results[0].geometry.location,
                          mapTypeId: google.maps.MapTypeId.ROADMAP
                        };
                        map = new google.maps.Map(document.getElementById("map_canvas"),
                            myOptions);
            map.setCenter(results[0].geometry.location);
            var marker = new google.maps.Marker({
                map: map,
                position: results[0].geometry.location
            });
          } else {
            console.log("Geocode was not successful for the following reason: " + status);
            var myOptions = {
                      zoom: 1,
                      center: new google.maps.LatLng(0, 0),
                      mapTypeId: google.maps.MapTypeId.ROADMAP
                    };
                    map = new google.maps.Map(document.getElementById("map_canvas"),
                        myOptions);
          }
        });
    }
}