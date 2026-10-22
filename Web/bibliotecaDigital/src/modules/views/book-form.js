export function renderBookForm(data, editingId) {
	const form = document.querySelector("#book-form");
	const book = editingId ? data.libros.find((item) => item.id === editingId) : null;
	form.reset();
	form.elements.titulo.value = book?.titulo ?? "";
	form.elements.autor.value = book?.autor ?? "";
	form.elements.anio.value = book?.anio ?? "";
	form.elements.genero.value = book?.genero ?? "Novela";
	form.elements.comentario.value = book?.comentario ?? "";
	form.elements.coleccion.checked = book?.coleccion ?? false;
	document.querySelector("#comment-count").textContent = 120 - form.elements.comentario.value.length;
	document.querySelector("#book-submit").textContent = editingId ? "Guardar cambios" : "Agregar libro";
	document.querySelector("#cancel-edit").hidden = !editingId;
	document.querySelector("#form-heading").textContent = editingId ? "Editar ejemplar" : "Un nuevo título";
	document.querySelector("#form-intro").textContent = editingId ? "Actualiza los datos del catálogo." : "Incorpora una nueva lectura a la biblioteca.";
	form.querySelectorAll("[data-error-for]").forEach((error) => { error.textContent = ""; });
}