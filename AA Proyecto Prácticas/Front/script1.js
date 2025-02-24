const API_URL = "http://localhost:8080"; // URL de la API

// Cargamos la lista de los surtidores
async function cargarSurtidores() {
    const respuesta = await fetch(`${API_URL}/surtidores`);
    const surtidores = await respuesta.json();

    const tabla = document.querySelector("#tablaSurtidores tbody");
    const selectSurtidor = document.querySelector("#surtidor");

    tabla.innerHTML = "";
    selectSurtidor.innerHTML = "";

    surtidores.forEach(s => {
        tabla.innerHTML += `<tr>
            <td>${s.id}</td>
            <td>${s.nombre}</td>
            <td>${s.combustible.nombre}</td>
            <td>${s.combustible.precio.toFixed(2)} €</td>
        </tr>`;

        // Agregar surtidores al select del formulario
        selectSurtidor.innerHTML += `<option value="${s.id}">${s.nombre}</option>`;
    });
}

// Cargar últimos 25 suministros
async function cargarSuministros() {
    const respuesta = await fetch(`${API_URL}/suministros`);
    const suministros = await respuesta.json();

    const tabla = document.querySelector("#tablaSuministros tbody");
    tabla.innerHTML = "";

    suministros.slice(-25).forEach(s => {
        let fechaSuministro = new Date(s.fecha);
        let fechaFormateada = fechaSuministro.toLocaleDateString("es-ES") + " " + fechaSuministro.toLocaleTimeString("es-ES");

        tabla.innerHTML += `<tr>
            <td>${s.id}</td>
            <td>${s.combustible.nombre}</td>
            <td>${s.cantidadLitros.toFixed(2)} L</td>
            <td>€${s.importe.toFixed(2)}</td>
            <td>${fechaFormateada}</td>
        </tr>`;
    });
}

// Función para registrar un nuevo suministro
document.querySelector("#formSuministro").addEventListener("submit", async function(event) {
    event.preventDefault();

    const surtidorId = document.querySelector("#surtidor").value;
    const combustibleId = document.querySelector("#combustible").value;
    const cantidadLitros = document.querySelector("#litros").value;

    const nuevoSuministro = {
        surtidor: { id: parseInt(surtidorId) },
        combustible: { id: parseInt(combustibleId) },
        cantidadLitros: parseFloat(cantidadLitros),
        importe: 0, // Se calculará en el backend según el precio del combustible
        fecha: new Date().toISOString() // Formato correcto para la API
    };

    const respuesta = await fetch(`${API_URL}/suministros`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(nuevoSuministro)
    });

    if (respuesta.ok) {
        alert("✅ Suministro registrado con éxito.");
        document.querySelector("#formSuministro").reset();
        cargarSuministros();
    } else {
        alert("❌ Error al registrar el suministro.");
    }
});

// Generamos el reporte de los últimos 25 suministros (solo si hay más de 25)
async function cargarReporte() {
    try {
        const respuesta = await fetch(`${API_URL}/suministros/reporte`);
        if (!respuesta.ok) {
            document.getElementById("reporte").innerHTML = "<p>Debe haber al menos 25 suministros.</p>";
            return;
        }

        const reporte = await respuesta.json();

        let litrosVendidosHTML = "<ul>";
        for (const [tipo, litros] of Object.entries(reporte.litrosVendidosPorCombustible)) {
            litrosVendidosHTML += `<li>${tipo}: ${litros.toFixed(2)} L</li>`;
        }
        litrosVendidosHTML += "</ul>";

        let importeFacturadoHTML = "<ul>";
        for (const [tipo, importe] of Object.entries(reporte.importeFacturadoPorCombustible)) {
            importeFacturadoHTML += `<li>${tipo}: €${importe.toFixed(2)}</li>`;
        }
        importeFacturadoHTML += "</ul>";

        let litrosRestantesHTML = "<ul>";
        for (const [tipo, litros] of Object.entries(reporte.litrosRestantesPorTanque)) {
            litrosRestantesHTML += `<li>${tipo}: ${litros.toFixed(2)} L restantes</li>`;
        }
        litrosRestantesHTML += "</ul>";

        document.getElementById("reporte").innerHTML = `
            <p><strong>Total Facturado:</strong> €${reporte.totalFacturado.toFixed(2)}</p>
            <h3>⛽ Litros Vendidos por Combustible:</h3> ${litrosVendidosHTML}
            <h3>💰 Importe Facturado por Combustible:</h3> ${importeFacturadoHTML}
            <h3>📦 Litros Restantes en Tanques:</h3> ${litrosRestantesHTML}
        `;

    } catch (error) {
        alert.error("Debe haber al menos 25 suministros", error);
        document.getElementById("reporte").innerHTML = "<p>Error al generar el reporte.</p>";
    }
}

// Cargamos los datos al abrir la página
document.addEventListener("DOMContentLoaded", () => {
    cargarSurtidores();
    cargarSuministros();
});