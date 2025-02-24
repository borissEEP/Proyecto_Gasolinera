const API_URL = "http://localhost:8080"; // Verifica que el backend corre en este puerto

// Cargamos la lista de surtidores
async function cargarSurtidores() {
    try {
        const respuesta = await fetch(`${API_URL}/surtidores`);
        if (!respuesta.ok) throw new Error("Error al cargar surtidores.");
        
        const surtidores = await respuesta.json();
        const tabla = document.querySelector("#tablaSurtidores tbody");
        tabla.innerHTML = "";

        surtidores.forEach(s => {
            tabla.innerHTML += `<tr>
                <td>${s.id}</td>
                <td>${s.nombre}</td>
                <td>${s.combustible.nombre}</td>
                <td>${s.combustible.precio.toFixed(2)} €</td>
                <td>
                    <button class="edit-btn" onclick="llenarFormularioEdicion(${s.id})">Editar</button>
                    <button class="delete-btn" onclick="borrarSurtidor(${s.id})">Eliminar</button>
                </td>
            </tr>`;
        });
    } catch (error) {
        console.error(error);
        alert("❌ No se pudieron cargar los surtidores.");
    }
}

// Cargamos la lista de combustibles para el formulario
async function cargarCombustibles() {
    try {
        const respuesta = await fetch(`${API_URL}/combustibles`);
        if (!respuesta.ok) throw new Error("Error al cargar combustibles.");

        const combustibles = await respuesta.json();
        const selectAlta = document.querySelector("#combustibleSurtidor");
        const selectEdicion = document.querySelector("#editCombustibleSurtidor");

        selectAlta.innerHTML = "";
        selectEdicion.innerHTML = "";

        combustibles.forEach(c => {
            const option = `<option value="${c.id}">${c.nombre}</option>`;
            selectAlta.innerHTML += option;
            selectEdicion.innerHTML += option;
        });
    } catch (error) {
        console.error(error);
        alert("❌ No se pudieron cargar los combustibles.");
    }
}

// Dar de alta un surtidor (POST)
document.querySelector("#formSurtidor").addEventListener("submit", async function(event) {
    event.preventDefault();

    const nombre = document.querySelector("#nombreSurtidor").value;
    const combustibleId = document.querySelector("#combustibleSurtidor").value;

    const nuevoSurtidor = { nombre: nombre, combustible: { id: parseInt(combustibleId) } };

    try {
        const respuesta = await fetch(`${API_URL}/surtidores`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(nuevoSurtidor)
        });

        if (!respuesta.ok) throw new Error("Error al agregar surtidor.");

        alert("✅ Surtidor agregado con éxito!");
        document.querySelector("#formSurtidor").reset();
        cargarSurtidores();
    } catch (error) {
        console.error(error);
        alert("❌ No se pudo agregar el surtidor.");
    }
});

// Dar de baja un surtidor (DELETE)
async function borrarSurtidor(id) {
    if (!confirm("¿Seguro que deseas eliminar este surtidor?")) return;

    try {
        const respuesta = await fetch(`${API_URL}/surtidores/${id}`, { method: "DELETE" });
        if (!respuesta.ok) throw new Error("Error al eliminar surtidor.");

        alert("✅ Surtidor eliminado correctamente.");
        cargarSurtidores();
    } catch (error) {
        console.error(error);
        alert("❌ No se pudo eliminar el surtidor.");
    }
}

// Llenamos el formulario de edición con los datos de un surtidor
async function llenarFormularioEdicion(id) {
    try {
        const respuesta = await fetch(`${API_URL}/surtidores/${id}`);
        if (!respuesta.ok) throw new Error("Error al obtener surtidor.");

        const surtidor = await respuesta.json();
        document.querySelector("#editSurtidorId").value = surtidor.id;
        document.querySelector("#editNombreSurtidor").value = surtidor.nombre;
        document.querySelector("#editCombustibleSurtidor").value = surtidor.combustible.id;
    } catch (error) {
        console.error(error);
        alert("❌ No se encontró el surtidor.");
    }
}

// Modificamos un surtidor (PUT)
document.querySelector("#formEditarSurtidor").addEventListener("submit", async function(event) {
    event.preventDefault();

    const id = document.querySelector("#editSurtidorId").value;
    const nuevoNombre = document.querySelector("#editNombreSurtidor").value;
    const nuevoCombustibleId = document.querySelector("#editCombustibleSurtidor").value;

    const surtidorEditado = { nombre: nuevoNombre, combustible: { id: parseInt(nuevoCombustibleId) } };

    try {
        const respuesta = await fetch(`${API_URL}/surtidores/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(surtidorEditado)
        });

        if (!respuesta.ok) throw new Error("Error al actualizar surtidor.");

        alert("✅ Surtidor actualizado con éxito!");
        document.querySelector("#formEditarSurtidor").reset();
        cargarSurtidores();
    } catch (error) {
        console.error(error);
        alert("❌ No se pudo actualizar el surtidor.");
    }
});


// Cargamos los datos al iniciar
document.addEventListener("DOMContentLoaded", () => {
    cargarSurtidores();
    cargarCombustibles();
});
