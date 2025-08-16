function mostrarAlerta() {
    alert("Has hecho clic en un botón.");
}


const botones = document.querySelectorAll('.btn');

botones.forEach(boton => {
    boton.addEventListener('click', mostrarAlerta);
});