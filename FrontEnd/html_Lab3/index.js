/**
 * Lógica del Mapa - Laboratorio 3
 * Asegúrate de que el backend esté corriendo en el puerto 8081
 */

window.initMap = function() {
    console.log("Iniciando mapa en Ibagué...");
    
    // 1. Configuración inicial del mapa
    const centroIbague = { lat: 4.4389, lng: -75.2322 };
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 14,
        center: centroIbague,
        styles: [
            {
                featureType: "poi",
                stylers: [{ visibility: "off" }] // Opcional: oculta puntos de interés para ver mejor tus marcadores
            }
        ]
    });

    // 2. Llamamos a la función para cargar los datos
    cargarDatosServidor(map);
};

function cargarDatosServidor(map) {
    // PEGA AQUÍ TU TOKEN MÁS RECIENTE DE POSTMAN
    const tokenDesdePostman = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJzb2Z0dXRKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzc4Mzg5NjI2LCJleHAiOjE3ODAxODk2MjZ9.eu6X0BXk1LotZeYr412sI5H7YY-w3uaZjHz3WehIlASXb2-vR6lZeHjeYN0g1AjZhGPYJSMMwZDTIEfwU0JmKw";

    // Limpiamos el token para que no diga "Bearer Bearer" (error 401 común)
    const AuthorizationHeader = tokenDesdePostman.startsWith("Bearer ") 
        ? tokenDesdePostman 
        : "Bearer " + tokenDesdePostman;

    console.log("Consultando coordenadas al servidor...");

    fetch('http://localhost:8081/LaboratorioV1/coordenadas', {
        method: 'GET',
        headers: {
            'Authorization': AuthorizationHeader,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error " + response.status + ": No se pudo obtener datos del servidor.");
        }
        return response.json();
    })
    .then(data => {
        console.log("¡Éxito! Datos recibidos:", data);
        
        if (data.length === 0) {
            console.warn("El servidor regresó una lista vacía. Revisa tu base de datos.");
            return;
        }

        const labels = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        data.forEach((coord, index) => {
            // Extraemos latitud y longitud asegurando que sean números
            const lat = parseFloat(coord.latitud);
            const lng = parseFloat(coord.longitud);
            const nombre = coord.me_marca || "Sin nombre";

            // Solo dibujamos si las coordenadas son válidas y no son 0
            if (!isNaN(lat) && !isNaN(lng) && lat !== 0) {
                console.log(`Dibujando marcador ${index}: ${nombre} en [${lat}, ${lng}]`);
                
                const marker = new google.maps.Marker({
                    position: { lat: lat, lng: lng },
                    map: map,
                    label: labels[index % labels.length],
                    title: nombre,
                    animation: google.maps.Animation.DROP
                });

                // Ventana de información al hacer clic
                const infoWindow = new google.maps.InfoWindow({
                    content: `<div style="color: black;"><strong>${nombre}</strong><br>Lat: ${lat}<br>Lng: ${lng}</div>`
                });

                marker.addListener("click", () => {
                    infoWindow.open(map, marker);
                });
            } else {
                console.warn(`La coordenada para ${nombre} es inválida o está en 0. Geocoder falló en el backend.`);
            }
        });
    })
    .catch(error => {
        console.error("Hubo un problema con la petición fetch:", error);
        alert("Error al cargar coordenadas. Revisa la consola (F12).");
    });
}