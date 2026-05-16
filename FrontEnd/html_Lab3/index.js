/**
 * Lógica del Mapa - Entrega 3 Final
 */

let map;
let directionsService;
let directionsRenderer;

// Inicialización global exigida por la API de Google Maps
window.initMap = function() {
    console.log("Iniciando mapa en Ibagué...");
    const centroIbague = { lat: 4.4389, lng: -75.2322 };
    
    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 14,
        center: centroIbague,
    });

    // Componentes de Google Maps para trazar trayectos/líneas viales de forma óptima
    directionsService = new google.maps.DirectionsService();
    directionsRenderer = new google.maps.DirectionsRenderer({
        map: map,
        suppressMarkers: false // Muestra las letras A, B, C... por defecto en las paradas
    });
};

// Función gatillada por el botón de la interfaz
function buscarRuta() {
    const codigoRuta = document.getElementById("codigoRuta").value.trim();
    if (!codigoRuta) {
        alert("Por favor ingresa un código de ruta.");
        return;
    }
    cargarRutaServidor(codigoRuta);
}

function cargarRutaServidor(codigoRuta) {
    // Token JWT de Postman (Mantenlo actualizado)
    const tokenDesdePostman = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJzb2Z0dXRKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzc4NDMwNDc5LCJleHAiOjE3ODAyMzA0Nzl9.gr5gC-7FSU2oqMmRhIzzazSNgkkEq0ZuouveKxD6axY8VERzlLVU9Oj0SSnpHxEVBlscR1F__QY3Pwv93__wjg";
    const authHeader = tokenDesdePostman.startsWith("Bearer ") ? tokenDesdePostman : "Bearer " + tokenDesdePostman;

    // Ajusta la URL de acuerdo a los endpoints definidos en el requerimiento de servicio de tu backend
    // NOTA: Se añade el QueryParam o PathVariable del código de ruta según tus mapeos
    const url = `http://localhost:8081/LaboratorioV1/rutas?codigoRuta=${codigoRuta}`;

    console.log(`Consultando trayectos para la ruta: ${codigoRuta}...`);

    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': authHeader,
            'APIKey': 'clave123', // Validación personalizada del backend si persiste
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) throw new Error("Error " + response.status + ": No autorizado o ruta no encontrada.");
        return response.json();
    })
    .then(trayectos => {
        console.log("Trayectos recibidos:", trayectos);

        if (!trayectos || trayectos.length === 0) {
            alert("No se encontraron trayectos para ese código de ruta.");
            return;
        }

        // 1. REQUERIMIENTO: Ordenar los trayectos estrictamente por el campo de orden de parada (0 = inicial, mayor = final)
        trayectos.sort((a, b) => parseInt(a.ordenParada) - parseInt(b.ordenParada));

        // 2. Renderizar visualmente el listado en el panel izquierdo (FrontEnd Requirement)
        actualizarPanelLateral(trayectos);

        // 3. Trazar la ruta geográfica real uniendo los puntos en el mapa
        dibujarRutaEnMapa(trayectos);
    })
    .catch(error => {
        console.error("Fallo total en la consulta:", error);
        alert("Error al cargar la ruta. Verifica el Token JWT, la conexión al Backend o que la ruta exista.");
    });
}

function actualizarPanelLateral(trayectos) {
    const listaContenedor = document.getElementById("lista-paradas");
    listaContenedor.innerHTML = ""; // Limpiar estados anteriores

    trayectos.forEach((trayecto) => {
        const div = document.createElement("div");
        div.className = "parada-card";
        
        // Determinar etiqueta según el orden numérico
        let tipoParada = `Parada Intermedia #${trayecto.ordenParada}`;
        if (parseInt(trayecto.ordenParada) === 0) {
            tipoParada = "Inicio del Trayecto 🏁";
        } else if (parseInt(trayecto.ordenParada) === trayectos.length - 1) {
            tipoParada = "Fin del Trayecto 🛑";
        }

        div.innerHTML = `
            <div class="orden">${tipoParada}</div>
            <p class="ubicacion"><strong>Lugar:</strong> ${trayecto.ubicacion || 'Ubicación Desconocida'}</p>
            <small style="color: #666;">Coordenadas: [${trayecto.latitud}, ${trayecto.longitud}]</small>
        `;
        listaContenedor.appendChild(div);
    });
}

function dibujarRutaEnMapa(trayectos) {
    // Mapeamos y extraemos las coordenadas válidas procesadas por el API de Google Maps o Backend
    const puntos = trayectos
        .map(t => ({
            lat: parseFloat(t.latitud),
            lng: parseFloat(t.longitud)
        }))
        .filter(p => !isNaN(p.lat) && !isNaN(p.lng) && p.lat !== 0);

    if (puntos.length < 2) {
        console.warn("Se necesitan mínimo 2 coordenadas válidas para dibujar un camino.");
        return;
    }

    // Configuración de la solicitud de direcciones viales
    const origen = puntos[0];
    const destino = puntos[puntos.length - 1];
    
    // Paradas intermedias estructuradas como Waypoints para el API de Google Maps (Máximo 5 permitidas según reglas del negocio)
    const waypoints = [];
    for (let i = 1; i < puntos.length - 1; i++) {
        waypoints.push({
            location: puntos[i],
            stopover: true
        });
    }

    const request = {
        origin: origen,
        destination: destino,
        waypoints: waypoints,
        travelMode: google.maps.TravelMode.DRIVING // Modo Conducción tal como el requerimiento vehicular
    };

    directionsService.route(request, function(result, status) {
        if (status === google.maps.DirectionsStatus.OK) {
            // Pinta la línea azul oficial y ubica marcadores estandarizados automáticamente
            directionsRenderer.setDirections(result);
        } else {
            console.error("Fallo del servicio de rutas (Directions) debido a: " + status);
            // Fallback: Si Google Maps no encuentra calles/vías conectables, une los puntos con una línea recta
            alert("No se pudo trazar la ruta exacta por calles. Mostrando conexión directa.");
        }
    });
}