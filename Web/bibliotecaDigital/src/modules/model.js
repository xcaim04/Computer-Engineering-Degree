export const MAX_ACTIVE_LOANS = 3;
export const GENRES = ["Novela", "Ciencia", "Historia", "Poesía", "Fantasía", "Ensayo", "Biografía", "Arte"];

export function today() {
	const date = new Date();
	return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")}`;
}

export function shiftDate(date, days) {
	const value = new Date(`${date}T12:00:00`);
	value.setDate(value.getDate() + days);
	const year = value.getFullYear();
	const month = String(value.getMonth() + 1).padStart(2, "0");
	const day = String(value.getDate()).padStart(2, "0");
	return `${year}-${month}-${day}`;
}

export function createId() {
	return globalThis.crypto?.randomUUID?.() ?? `${Date.now()}-${Math.random().toString(16).slice(2)}`;
}

export function createInitialData() {
	const libros = [
		{ id: "book-1", titulo: "Cien años de soledad", autor: "Gabriel García Márquez", anio: 1967, genero: "Novela", coleccion: false, comentario: "Una saga familiar inolvidable.", isbn: "9780307474728" },
		{ id: "book-2", titulo: "Breves respuestas a las grandes preguntas", autor: "Stephen Hawking", anio: 2018, genero: "Ciencia", coleccion: false, comentario: "", isbn: "9781984819192" },
		{ id: "book-3", titulo: "El infinito en un junco", autor: "Irene Vallejo", anio: 2019, genero: "Historia", coleccion: false, comentario: "", isbn: "9788417860790" },
		{ id: "book-4", titulo: "Veinte poemas de amor", autor: "Pablo Neruda", anio: 1924, genero: "Poesía", coleccion: true, comentario: "Primera edición de colección.", isbn: "9788437604947" },
		{ id: "book-5", titulo: "El nombre del viento", autor: "Patrick Rothfuss", anio: 2007, genero: "Fantasía", coleccion: false, comentario: "", isbn: "9780756404741" },
		{ id: "book-6", titulo: "Sapiens", autor: "Yuval Noah Harari", anio: 2011, genero: "Historia", coleccion: false, comentario: "", isbn: "9780062316097" },
		{ id: "book-7", titulo: "El principito", autor: "Antoine de Saint-Exupéry", anio: 1943, genero: "Novela", coleccion: true, comentario: "Edición ilustrada de colección.", isbn: "9780156012195" },
		{ id: "book-8", titulo: "Una habitación propia", autor: "Virginia Woolf", anio: 1929, genero: "Ensayo", coleccion: false, comentario: "", isbn: "9780156030410" },
	];
	const date = today();
	const prestamos = [
		{ id: "loan-1", idLibro: "book-1", persona: "Ana Torres", fechaPrestamo: shiftDate(date, -21), fechaLimite: shiftDate(date, -7), fechaDevolucion: null },
		{ id: "loan-2", idLibro: "book-5", persona: "Luis Pérez", fechaPrestamo: shiftDate(date, -4), fechaLimite: shiftDate(date, 10), fechaDevolucion: null },
		{ id: "loan-3", idLibro: "book-2", persona: "Marta Ríos", fechaPrestamo: shiftDate(date, -20), fechaLimite: shiftDate(date, -6), fechaDevolucion: shiftDate(date, -8) },
		{ id: "loan-4", idLibro: "book-6", persona: "Diego León", fechaPrestamo: shiftDate(date, -18), fechaLimite: shiftDate(date, -4), fechaDevolucion: shiftDate(date, -2) },
	];
	return { libros, prestamos, tema: "claro", usuario: "", orden: "titulo", direccion: "asc" };
}

export function getActiveLoans(data) {
	return data.prestamos.filter((loan) => loan.fechaDevolucion === null);
}

export function getActiveLoanForBook(data, bookId) {
	return getActiveLoans(data).find((loan) => loan.idLibro === bookId);
}

export function isOverdue(loan) {
	return loan.fechaDevolucion === null && loan.fechaLimite < today();
}

export function daysLate(date) {
	return Math.max(0, Math.floor((new Date(`${today()}T12:00:00`) - new Date(`${date}T12:00:00`)) / 86400000));
}

export function validateBook(data, book, editingId) {
	const errors = {};
	if (book.titulo.length < 3 || book.titulo.length > 60) errors.titulo = "Escribe entre 3 y 60 caracteres.";
	if (!book.autor.trim()) errors.autor = "El nombre del autor es obligatorio.";
	if (!Number.isInteger(book.anio) || book.anio < 1000 || book.anio > 2026) errors.anio = "Usa un año entre 1000 y 2026.";
	const normalize = (text) => text.trim().normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLocaleLowerCase("es");
	if (data.libros.some((item) => item.id !== editingId && normalize(item.titulo) === normalize(book.titulo) && normalize(item.autor) === normalize(book.autor))) errors.titulo = "Ya existe un libro con ese título y autor.";
	if (book.comentario.length > 120) errors.comentario = "El comentario no puede superar 120 caracteres.";
	if (editingId && getActiveLoanForBook(data, editingId) && book.coleccion) errors.coleccion = "No puedes marcar como colección un libro prestado.";
	return errors;
}