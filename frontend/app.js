function animateDots() {
    const dots = document.querySelectorAll('.dot');
    let current = 0;

    setInterval(() => {
        if (current < dots.length) {
            dots[current].style.opacity = '1';
            current++;
        } else {
            dots.forEach(d => d.style.opacity = '0')
            current = 0;
        }
    }, 400);
}
animateDots();