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
    document.getElementById('jobs-container').innerHTML = '';
    const response = await fetch(API);
    const jobs = await response.json();
    jobs.forEach(job => {
        document.getElementById('jobs-container').appendChild(createJobCard(job));
    });
}

function createJobCard(job) {
    const deadline = new Date(job.deadline).toLocaleDateString('de-DE', {
        day: '2-digit', month: '2-digit', year: 'numeric',
        hour: '2-digit', minute: '2-digit'
    });

    const div = document.createElement('div');
    div.className = 'bg-gray-900 p-4';
    div.innerHTML = `
        <p class="text-sm font-medium text-gray-100">${job.title}</p>
        <p class="text-xs text-gray-500 pb-4 mt-1">${job.description} · prio ${job.priority} · ${deadline}</p>
    `;
    const btnAccept = document.createElement('button');
    const btnPostpone = document.createElement('button');

    if (job.status === 'PENDING') {
        setButtonState(btnAccept, true);
        setButtonState(btnPostpone, true);
    } else if (job.status === 'RUNNING') {
        setButtonState(btnAccept, false);
        setButtonState(btnPostpone, true);
    } else if (job.status === 'POSTPONED') {
        setButtonState(btnAccept, true);
        setButtonState(btnPostpone, false);
    } else {
        setButtonState(btnAccept, false);
        setButtonState(btnPostpone, false);
    }

    btnAccept.className = 'p-4 text-xs border border-gray-700 rounded'
    btnAccept.innerHTML = `<p>Accept</p>`;

    const originalBorder = btnAccept.style.borderColor;
    btnAccept.onmouseover = () => btnAccept.style.borderColor = '#9ca3af';
    btnAccept.onmouseleave = () => btnAccept.style.borderColor = originalBorder;

    btnAccept.onclick = () => acceptJob(job.id);

    btnPostpone.className = 'ml-4 p-4 text-xs border border-gray-700 rounded'
    btnPostpone.innerHTML = `<p>Postpone</p>`;

    btnPostpone.onmouseover = () => btnPostpone.style.borderColor = '#9ca3af';
    btnPostpone.onmouseleave = () => btnPostpone.style.borderColor = originalBorder;
    btnPostpone.onclick = () => postponeJob(job.id);

    div.appendChild(btnAccept);
    div.appendChild(btnPostpone);

    return div;
}

async function acceptJob(id) {
    await fetch(`${API}/${id}/accepted`, {
        method: 'PATCH'
    });
    await loadJobs();
}

async function postponeJob(id) {
    await fetch(`${API}/${id}/postponed`, {
        method: 'PATCH'
    })
    await loadJobs();
}

function setButtonState(btn, active) {
    btn.disabled = !active;
    btn.style.cursor = active ? 'default' : 'not-allowed';
    btn.style.opacity = active ? '1' : '0.4';
}


animateDots();
loadJobs();
