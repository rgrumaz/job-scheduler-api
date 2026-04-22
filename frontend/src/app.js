const API = 'http://localhost:8080/jobs';

function animateDots() {
    const dots = document.querySelectorAll('.dot');
    let current = 0;

    setInterval(() => {
        if (current < dots.length) {
            dots[current].style.opacity = '1';
            current++;
        } else {
            dots.forEach(d => d.style.opacity = '0');
            current = 0;
        }
    }, 400);
}

async function loadJobs() {
    const response = await fetch(API);
    const jobs = await response.json();
    console.log(jobs);
}

animateDots();
loadJobs();
