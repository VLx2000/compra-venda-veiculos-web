function cor() {
    let estado = document.querySelectorAll('.estado')
    for (let i = 0; i < estado.length; i++) {
        if (estado[i].textContent == 'ACEITO'){
            estado[i].style = 'color: green; background-color: greenyellow; font-weight: bolder;'
        }
        else if(estado[i].textContent == 'ABERTO'){
            estado[i].style = 'color: orange; background-color: yellow; font-weight: bolder;'
        }
        else {
            estado[i].style = 'color: red; background-color: darkred; font-weight: bolder;'
        }
    }
}

window.onload = cor()