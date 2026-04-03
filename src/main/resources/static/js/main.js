window.appToast = function appToast(message, type = "success", duration = 1400) {
	let container = document.getElementById("toastContainer");
	if (!container) {
		container = document.createElement("div");
		container.id = "toastContainer";
		container.className = "toast-container";
		document.body.appendChild(container);
	}

	const toast = document.createElement("div");
	toast.className = `toast-item toast-${type}`;
	toast.textContent = message;
	container.appendChild(toast);

	requestAnimationFrame(() => {
		toast.classList.add("show");
	});

	window.setTimeout(() => {
		toast.classList.remove("show");
		window.setTimeout(() => toast.remove(), 220);
	}, duration);
};

