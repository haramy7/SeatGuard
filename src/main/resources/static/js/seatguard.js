document.addEventListener('DOMContentLoaded', () => {
    const IDLE_LIMIT = 3 * 60;
    const DANGER_LIMIT = 10 * 60;
    
    let idleTime = 0;
    let isManualAway = false;
    let currentStatus = 'IN';

    const statusText = document.getElementById('statusText');
    const timeDisplay = document.getElementById('timeDisplay');
    const body = document.body;
    const btnAway = document.getElementById('btnAway');
    
    function formatTimeDisplay(seconds) {
        const m = Math.floor(seconds / 60);
        if (m < 1) return '방금 전 활동';
        if (m < 60) return `${m}분 전 활동`;
        const h = Math.floor(m / 60);
        return `${h}시간 ${m % 60}분 전 활동`;
    }

    function updateUI() {
        timeDisplay.innerText = formatTimeDisplay(idleTime);
        
        if (isManualAway) return;

        if (idleTime >= DANGER_LIMIT) {
            setDangerState();
        } else if (idleTime >= IDLE_LIMIT) {
            setAwayState();
        } else {
            setActiveState();
        }
    }

    function setDangerState() {
        if (currentStatus === 'DANGER') return;
        currentStatus = 'DANGER';
        
        body.className = 'is-alert';
        statusText.innerText = '장기 이석 중';
        notifyServer('DANGER');
    }

    function setAwayState() {
        if (currentStatus === 'AWAY') return;
        if (currentStatus === 'DANGER') return;
        currentStatus = 'AWAY';
        
        body.className = 'is-away';
        statusText.innerText = '자리를 비웠습니다';
        notifyServer('AWAY');
    }

    function setActiveState() {
        if (currentStatus === 'IN') return;
        currentStatus = 'IN';
        
        body.className = '';
        statusText.innerText = '자리에 있습니다';
        notifyServer('IN');
    }

    function resetTimer() {
        if (isManualAway) return;
        
        idleTime = 0;
        if (currentStatus !== 'IN') {
            setActiveState();
        }
        updateUI();
    }

    function notifyServer(status) {
        fetch('/api/status', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status: status })
        }).catch(e => console.error('Status sync failed', e));
    }

    ['mousemove', 'keydown', 'scroll', 'click', 'touchstart'].forEach(evt => {
        window.addEventListener(evt, resetTimer, { passive: true });
    });

    setInterval(() => {
        if (!isManualAway) {
            idleTime++;
            updateUI();
        }
    }, 1000);

    btnAway.addEventListener('click', () => {
        isManualAway = !isManualAway;
        btnAway.classList.toggle('active');
        
        if (isManualAway) {
            currentStatus = 'AWAY';
            body.className = 'is-away';
            statusText.innerText = '잠시 자리 비움';
            btnAway.innerText = '돌아왔습니다';
            notifyServer('AWAY');
        } else {
            idleTime = 0;
            setActiveState();
            btnAway.innerText = '잠시 자리 비움';
        }
    });
    
    notifyServer('IN');
});
