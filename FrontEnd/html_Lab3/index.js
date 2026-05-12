/**
 * Lógica del Mapa - Laboratorio 3
 */

window.initMap = function() {
    console.log("Iniciando mapa en Ibagué...");
    const centroIbague = { lat: 4.4389, lng: -75.2322 };
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 14,
        center: centroIbague,
    });

    cargarDatosServidor(map);
};

function cargarDatosServidor(map) {
    // REEMPLAZA ESTE TOKEN SI EXPIRA (Dura aprox 24h según tu log)
    const tokenDesdePostman = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJzb2Z0dXRKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzc4NDMwNDc5LCJleHAiOjE3ODAyMzA0Nzl9.gr5gC-7FSU2oqMmRhIzzazSNgkkEq0ZuouveKxD6axY8VERzlLVU9Oj0SSnpHxEVBlscR1F__QY3Pwv93__wjg";

    const authHeader = tokenDesdePostman.startsWith("Bearer ") ? tokenDesdePostman : "Bearer " + tokenDesdePostman;

    console.log("Consultando coordenadas...");

    fetch('http://localhost:8081/LaboratorioV1/coordenadas', {
        method: 'GET',
        headers: {
            'Authorization': authHeader,
            'APIKey': 'clave123', // Header personalizado validado en Backend
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) throw new Error("Error " + response.status + ": No autorizado.");
        return response.json();
    })
    .then(data => {
        console.log("Datos recibidos:", data);
        const infoWindow = new google.maps.InfoWindow();

        data.forEach((coord) => {
            const lat = parseFloat(coord.latitud);
            const lng = parseFloat(coord.longitud);
            
            if (!isNaN(lat) && !isNaN(lng) && lat !== 0) {
                const marker = new google.maps.Marker({
                    position: { lat: lat, lng: lng },
                    map: map,
                    title: coord.me_marca || "Vehículo",
                    icon: {
                        path: google.maps.SymbolPath.CIRCLE,
                        fillColor: '#FF0000',
                        fillOpacity: 1,
                        strokeColor: '#FFFFFF',
                        strokeWeight: 2,
                        scale: 8
                    }
                });

                marker.addListener("click", () => {
                    infoWindow.setContent(`<strong>${coord.me_marca || 'Sin nombre'}</strong><br>Lat: ${lat}<br>Lng: ${lng}`);
                    infoWindow.open(map, marker);
                });
            }
        });
    })
    .catch(error => {
        console.error("Fallo total:", error);
        alert("Error de conexión. Revisa que el Backend esté corriendo y hayas aplicado los cambios de CORS.");
    });
}