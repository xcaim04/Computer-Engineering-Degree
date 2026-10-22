import { renderCatalog } from "./modules/views/catalog.js";
import { renderLoans } from "./modules/views/loans.js";
import { renderBookForm } from "./modules/views/book-form.js";
import { renderSummary } from "./modules/views/summary.js";
import { escapeHtml } from "./modules/helpers.js";
import {
	MAX_ACTIVE_LOANS,
	createId,
	createInitialData,
	daysLate,
	getActiveLoanForBook,
	getActiveLoans,
	shiftDate,
	today,
	validateBook,
} from "./modules/model.js";
import { loadData, saveData } from "./modules/storage.js";

const state = {
	data: loadData(),
	view: "catalogo",
	editingId: null,
	filters: { selectedFilter: "todos", searchTerm: "", selectedGenre: "todos" },
	messageTimer: null,
};
const dialog = document.querySelector("#app-modal");

function save() {
	saveData(state.data);
}

function announce(message, type = "success") {
	const feedback = document.querySelector("#feedback");
	feedback.textContent = message;
	feedback.dataset.type = type;
	feedback.hidden = false;
	clearTimeout(state.messageTimer);
	state.messageTimer = setTimeout(() => { feedback.hidden = true; }, 5200);
}

function updateChrome() {
	const count = getActiveLoans(state.data).length;
	const loanCount = document.querySelector("#loan-count");
	loanCount.textContent = count;
	loanCount.hidden = count === 0;
	document.querySelector("#user-greeting").textContent = state.data.usuario ? `Hola, ${state.data.usuario}` : "Biblioteca personal";
	document.documentElement.dataset.theme = state.data.tema;
	const themeToggle = document.querySelector("#theme-toggle");
	themeToggle.setAttribute("aria-label", state.data.tema === "oscuro" ? "Cambiar a tema claro" : "Cambiar a tema oscuro");
	themeToggle.innerHTML = state.data.tema === "oscuro" ? "<span aria-hidden=\"true\">☼</span>" : "<span aria-hidden=\"true\">◐</span>";
	const userName = document.querySelector("#user-name");
	if (document.activeElement !== userName) userName.value = state.data.usuario;
}

function setView(view) {
	state.view = view;
	document.querySelectorAll("[data-view-link]").forEach((link) => {
		const active = link.dataset.viewLink === view;
		link.classList.toggle("active", active);
		if (active) link.setAttribute("aria-current", "page");
		else link.removeAttribute("aria-current");
	});
	document.querySelectorAll(".view").forEach((section) => {
		section.hidden = section.id !== `view-${view}`;
		if (!section.hidden) {
			section.classList.remove("view-enter");
			requestAnimationFrame(() => section.classList.add("view-enter"));
		}
	});
	document.querySelector("#page-title").textContent = ({ catalogo: "El catálogo", prestamos: "Mis préstamos", libro: state.editingId ? "Editar libro" : "Agregar un libro", resumen: "Resumen y ajustes" })[view];
	if (view === "libro") renderBookForm(state.data, state.editingId);
}

function renderAll() {
	updateChrome();
	renderCatalog(state.data, state.filters);
	renderLoans(state.data);
	renderSummary(state.data);
	if (state.view === "libro") setView("libro");
}

function clearForm() {
	state.editingId = null;
	renderBookForm(state.data, null);
	document.querySelector("#page-title").textContent = "Agregar un libro";
}

function openBorrowModal(book) {
	dialog.dataset.mode = "borrow";
	dialog.dataset.bookId = book.id;
	dialog.innerHTML = `<form method="dialog" class="modal-content" id="borrow-form"><div class="modal-kicker">Préstamo nuevo</div><button class="modal-close" type="button" aria-label="Cerrar">×</button><h2 id="modal-title">¿Quién se lleva<br><em>${escapeHtml(book.titulo)}</em>?</h2><p class="modal-copy">El préstamo tendrá un plazo de 14 días.</p><label class="field-label" for="borrower-name">Nombre de la persona</label><input id="borrower-name" name="persona" class="text-input" autocomplete="name" minlength="2" maxlength="40" required value="${escapeHtml(state.data.usuario)}" placeholder="Ej. Ana Torres"><p class="field-error modal-error" id="modal-error" role="alert"></p><div class="modal-actions"><button class="button button-secondary" value="cancel">Cancelar</button><button class="button button-primary" type="submit" value="confirm">Confirmar préstamo</button></div></form>`;
	dialog.showModal();
	dialog.querySelector("#borrower-name").focus();
}

function openDeleteModal(book) {
	dialog.dataset.mode = "delete";
	dialog.dataset.bookId = book.id;
	dialog.innerHTML = `<form method="dialog" class="modal-content" id="delete-form"><div class="modal-kicker">Quitar del catálogo</div><button class="modal-close" value="cancel" aria-label="Cerrar">×</button><h2 id="modal-title">¿Eliminar<br><em>${escapeHtml(book.titulo)}</em>?</h2><p class="modal-copy">El ejemplar y su historial de préstamos devueltos se eliminarán permanentemente.</p><div class="modal-actions"><button class="button button-secondary" value="cancel">Cancelar</button><button class="button button-danger" type="submit" value="confirm">Eliminar libro</button></div></form>`;
	dialog.showModal();
}

function returnBook(bookId) {
	const loan = getActiveLoanForBook(state.data, bookId);
	if (!loan) return;
	const late = Math.max(0, Math.floor((new Date(`${today()}T12:00:00`) - new Date(`${loan.fechaLimite}T12:00:00`)) / 86400000));
	loan.fechaDevolucion = today();
	save();
	renderAll();
	announce(late ? `Devolución registrada con ${late} ${late === 1 ? "día" : "días"} de retraso.` : "Devolución registrada a tiempo.", late ? "warning" : "success");
}

function handleBookSubmit(event) {
	event.preventDefault();
	const form = event.currentTarget;
	const book = {
		titulo: form.elements.titulo.value.trim(),
		autor: form.elements.autor.value.trim(),
		anio: Number(form.elements.anio.value),
		genero: form.elements.genero.value,
		comentario: form.elements.comentario.value.trim(),
		coleccion: form.elements.coleccion.checked,
	};
	const errors = validateBook(state.data, book, state.editingId);
	form.querySelectorAll("[data-error-for]").forEach((node) => { node.textContent = errors[node.dataset.errorFor] || ""; });
	const firstInvalid = Object.keys(errors)[0];
	if (firstInvalid) { form.elements[firstInvalid].focus(); return; }
	if (state.editingId) {
		Object.assign(state.data.libros.find((item) => item.id === state.editingId), book);
		announce(`«${book.titulo}» se actualizó correctamente.`);
	} else {
		state.data.libros.push({ id: createId(), ...book });
		announce(`«${book.titulo}» se agregó al catálogo.`);
	}
	save();
	clearForm();
	renderAll();
	setView("catalogo");
}

document.addEventListener("click", (event) => {
	const viewLink = event.target.closest("[data-view-link]");
	if (viewLink) {
		event.preventDefault();
		if (viewLink.dataset.viewLink === "libro") clearForm();
		setView(viewLink.dataset.viewLink);
		return;
	}
	const filter = event.target.closest("[data-filter]");
	if (filter) { state.filters.selectedFilter = filter.dataset.filter; renderCatalog(state.data, state.filters); return; }
	const action = event.target.closest("[data-action]");
	if (!action || action.disabled) return;
	const book = state.data.libros.find((item) => item.id === action.dataset.id);
	if (!book) return;
	if (action.dataset.action === "borrow") {
		if (getActiveLoans(state.data).length >= MAX_ACTIVE_LOANS) { announce("No se puede prestar: la biblioteca ya alcanzó el máximo de 3 préstamos activos.", "error"); return; }
		if (book.coleccion || getActiveLoanForBook(state.data, book.id)) { announce("Este ejemplar no está disponible para préstamo.", "error"); return; }
		openBorrowModal(book);
	} else if (action.dataset.action === "return") returnBook(book.id);
	else if (action.dataset.action === "edit") {
		state.editingId = book.id;
		setView("libro");
		document.querySelector("#book-form input[name='titulo']").focus();
	} else if (action.dataset.action === "delete") {
		if (getActiveLoanForBook(state.data, book.id)) { announce("No se puede eliminar un libro con un préstamo activo.", "error"); return; }
		openDeleteModal(book);
	}
});

document.querySelector("#search-books").addEventListener("input", (event) => {
	state.filters.searchTerm = event.target.value;
	renderCatalog(state.data, state.filters);
	const search = document.querySelector("#search-books");
	search.focus();
	search.setSelectionRange(search.value.length, search.value.length);
});
document.querySelector("#genre-filter").addEventListener("change", (event) => {
	state.filters.selectedGenre = event.target.value;
	renderCatalog(state.data, state.filters);
});
document.querySelector("#sort-books").addEventListener("change", (event) => { state.data.orden = event.target.value; save(); renderCatalog(state.data, state.filters); });
document.querySelector("#sort-direction").addEventListener("change", (event) => { state.data.direccion = event.target.value; save(); renderCatalog(state.data, state.filters); });

document.querySelector("#book-form").addEventListener("input", (event) => {
	if (event.target.name === "comentario") document.querySelector("#comment-count").textContent = 120 - event.target.value.length;
});
document.querySelector("#book-form").addEventListener("submit", handleBookSubmit);
document.querySelector("#cancel-edit").addEventListener("click", () => { clearForm(); announce("Edición cancelada.", "info"); });
document.querySelector("#theme-toggle").addEventListener("click", () => {
	state.data.tema = state.data.tema === "oscuro" ? "claro" : "oscuro";
	save();
	updateChrome();
});
document.querySelector("#user-name").addEventListener("input", (event) => { state.data.usuario = event.target.value.slice(0, 40); save(); updateChrome(); });
document.querySelector("#user-name").addEventListener("blur", (event) => { state.data.usuario = event.target.value.trim(); save(); updateChrome(); });
document.querySelector("#reset-library").addEventListener("click", () => {
	state.data = createInitialData();
	save();
	state.filters = { selectedFilter: "todos", searchTerm: "", selectedGenre: "todos" };
	state.editingId = null;
	renderAll();
	setView("resumen");
	announce("La biblioteca volvió a sus datos iniciales.");
});

dialog.addEventListener("submit", (event) => {
	const mode = dialog.dataset.mode;
	if (event.submitter?.value === "cancel") return;
	if (mode === "borrow") {
		event.preventDefault();
		const input = dialog.querySelector("#borrower-name");
		const name = input.value.trim();
		const bookId = dialog.dataset.bookId;
		const book = state.data.libros.find((item) => item.id === bookId);
		const error = name.length < 2 || name.length > 40 ? "El nombre debe tener entre 2 y 40 caracteres." : getActiveLoans(state.data).length >= MAX_ACTIVE_LOANS ? "Ya hay 3 préstamos activos. Devuelve uno antes de continuar." : !book || book.coleccion || getActiveLoanForBook(state.data, bookId) ? "Este libro ya no está disponible." : "";
		if (error) { dialog.querySelector("#modal-error").textContent = error; input.focus(); return; }
		state.data.prestamos.push({ id: createId(), idLibro: bookId, persona: name, fechaPrestamo: today(), fechaLimite: shiftDate(today(), 14), fechaDevolucion: null });
		save();
		dialog.close();
		renderAll();
		announce(`Préstamo de «${book.titulo}» registrado para ${name}.`);
	} else if (mode === "delete") {
		event.preventDefault();
		const bookId = dialog.dataset.bookId;
		const book = state.data.libros.find((item) => item.id === bookId);
		if (book && !getActiveLoanForBook(state.data, bookId)) {
			state.data.libros = state.data.libros.filter((item) => item.id !== bookId);
			state.data.prestamos = state.data.prestamos.filter((loan) => loan.idLibro !== bookId);
			save();
			dialog.close();
			renderAll();
			announce(`«${book.titulo}» y su historial se eliminaron.`);
		}
	}
});

dialog.addEventListener("click", (event) => {
	if (event.target === dialog || event.target.closest(".modal-close")) dialog.close();
});
document.addEventListener("keydown", (event) => {
	if (event.key === "Escape" && !dialog.open && state.view === "libro") clearForm();
});

renderAll();
setView("catalogo");